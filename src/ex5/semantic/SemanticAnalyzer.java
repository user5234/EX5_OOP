package ex5.semantic;

import ex5.ast.*;
import ex5.ast.expressions.LiteralExpression;
import ex5.ast.statements.MethodCall;
import ex5.ast.expressions.VariableExpression;
import ex5.ast.expressions.LogicalExpression;
import ex5.ast.statements.*;
import ex5.lexer.Token;
import ex5.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs semantic analysis on the AST.
 */
public class SemanticAnalyzer implements ASTVisitor<TokenType> {

	private final MethodTable methodTable;
	private final List<MethodDeclaration> deferredMethods;
	private Scope currentScope;

	/**
	 * Constructs a SemanticAnalyzer.
	 */
	public SemanticAnalyzer() {
		methodTable = new MethodTable();
		deferredMethods = new ArrayList<>();
		currentScope = new Scope(null);
	}

	/**
	 * Analyzes a list of statements for semantic correctness.
	 *
	 * @param statements The list of statements to analyze.
	 */
	public void analyze(List<Statement> statements) {
		// Collect method declarations first
		for (var s : statements) {
			if (s instanceof MethodDeclaration md) {
				deferredMethods.add(md);
				methodTable.define(new MethodSymbol(md.getIdentifier(), md.getArguments()));
			}
			else {
				s.accept(this);
			}
		}

		// Now analyze method bodies
		for (var md : deferredMethods) {
			md.accept(this);
		}
	}

	/**
	 * Visits a block statement, creating a new scope for its variables.
	 *
	 * @param bl The block statement to visit.
	 */
	@Override
	public void visitBlock(Block bl) {
		var scope = currentScope;
		currentScope = new Scope(scope);

		for (var s : bl.getStatements()) {
			s.accept(this);
		}

		currentScope = scope;
	}

	/**
	 * Visits an if statement, checking the condition type and visiting the body.
	 *
	 * @param is The if statement to visit.
	 */
	@Override
	public void visitIfStatement(IfStatement is) {
		var conditionType = is.getCondition().accept(this);
		if (!isConditionOperandType(conditionType)) {
			throw new SemanticException("If condition must be boolean");
		}

		is.getBody().accept(this);
	}

	/**
	 * Visits a method argument, defining it in the current scope.
	 *
	 * @param ma The method argument to visit.
	 */
	@Override
	public void visitMethodArgument(MethodArgument ma) {
		currentScope.define(new Symbol(ma.getIdentifier(), ma.getType(), false, true));
	}

	/**
	 * Visits a method declaration, creating a new scope for its parameters and body.
	 *
	 * @param md The method declaration to visit.
	 */
	@Override
	public void visitMethodDeclaration(MethodDeclaration md) {
		if (currentScope.hasParent()) {
			throw new SemanticException("Method " +
			                            md.getIdentifier() +
			                            " cannot be declared inside another method");
		}

		var scope = currentScope;

		currentScope = new Scope(scope);
		for (var param : md.getArguments()) {
			param.accept(this);
		}

		md.getBody().accept(this);

		var statements = md.getBody().getStatements();
		if (statements.isEmpty() ||
		    !(statements.get(statements.size() - 1) instanceof ReturnStatement)) {
			throw new SemanticException("Method " +
			                            md.getIdentifier() +
			                            " must end with a return statement");
		}

		currentScope = scope;
	}

	/**
	 * Visits a return statement.
	 *
	 * @param rs The return statement to visit.
	 */
	@Override
	public void visitReturnStatement(ReturnStatement rs) {}

	/**
	 * Visits a variable assignment, checking for final and initialization rules.
	 *
	 * @param va The variable assignment to visit.
	 */
	@Override
	public void visitVariableAssignment(VariableAssignment va) {
		var symbol = currentScope.resolve(va.getIdentifier());

		// final rule: cannot change after initialized
		if (symbol.isFinal() && symbol.isInitialized()) {
			throw new SemanticException("Cannot assign to final variable: " + va.getIdentifier());
		}

		var exprType = va.getExpression().accept(this);

		if (!isAssignable(symbol.getType(), exprType)) {
			throw new SemanticException("Type mismatch: cannot assign " +
			                            exprType + " to " + symbol.getType());
		}

		symbol.setInitialized(true);
	}

	/**
	 * Visits a variable declaration, defining it in the current scope.
	 *
	 * @param vs The variable declaration to visit.
	 */
	@Override
	public void visitVariableDeclaration(VariableDeclaration vs) {
		boolean isInitialized = vs.getInitializer() != null;

		if (isInitialized) {
			var initType = vs.getInitializer().accept(this);
			if (!isAssignable(vs.getType(), initType)) {
				throw new SemanticException("Type mismatch: cannot assign " +
				                            initType + " to " + vs.getType());
			}
		}

		currentScope.define(
				new Symbol(vs.getIdentifier(), vs.getType(), vs.isFinal(), isInitialized)
		);
	}

	/**
	 * Visits a while statement, checking the condition type and visiting the body.
	 *
	 * @param ws The while statement to visit.
	 */
	public void visitWhileStatement(WhileStatement ws) {
		var conditionType = ws.getCondition().accept(this);
		if (!isConditionOperandType(conditionType)) {
			throw new SemanticException("While condition must be boolean");
		}

		ws.getBody().accept(this);
	}

	// ───────── EXPRESSIONS ─────────

	/**
	 * Visits a literal expression, returning its type.
	 *
	 * @param le The literal expression to visit.
	 */
	@Override
	public TokenType visitLiteralExpression(LiteralExpression le) {
		return literalType(le.getLiteral());
	}

	/**
	 * Visits a variable expression, returning its type.
	 *
	 * @param ve The variable expression to visit.
	 */
	@Override
	public TokenType visitVariableExpression(VariableExpression ve) {
		var sym = currentScope.resolve(ve.getIdentifier());
		if (!sym.isInitialized()) {
			throw new SemanticException("Variable " +
			                            ve.getIdentifier() +
			                            " used before initialization");
		}
		return sym.getType();
	}

	/**
	 * Visits a method call expression, checking argument types and returning the method's return
	 * type.
	 *
	 * @param mc The method call to visit.
	 */
	@Override
	public void visitMethodCall(MethodCall mc) {
		var method = methodTable.resolve(mc.getIdentifier());

		if (mc.getArguments().size() != method.getParameters().size()) {
			throw new SemanticException("Method " +
			                            mc.getIdentifier() +
			                            " expects " +
			                            method.getParameters().size() +
			                            " arguments, got " +
			                            mc.getArguments().size());
		}

		for (int i = 0; i < mc.getArguments().size(); i++) {
			TokenType argType = mc.getArguments().get(i).accept(this);
			TokenType paramType = method.getParameters().get(i).getType();
			if (!isAssignable(paramType, argType)) {
				throw new SemanticException("Argument " + (i + 1) +
				                            " of method " + mc.getIdentifier() +
				                            " expects " + paramType + ", got " + argType);
			}
		}
	}

	/**
	 * Visits a logical expression, checking operand types and returning boolean type.
	 *
	 * @param le The logical expression to visit.
	 */
	@Override
	public TokenType visitLogicalExpression(LogicalExpression le) {
		TokenType left = le.getLeft().accept(this);
		TokenType right = le.getRight().accept(this);

		if (!isConditionOperandType(left) || !isConditionOperandType(right)) {
			throw new SemanticException("Operands of &&/|| must be boolean/int/double");
		}

		return TokenType.BOOLEAN;
	}

	/**
	 * Visits a method call statement.
	 *
	 * @param mcs The method call statement to visit.
	 */
	@Override
	public void visitMethodCallStatement(MethodCallStatement mcs) {
		mcs.getCall().accept(this);
	}

	// ───────── HELPERS ─────────

	/**
	 * Determines the TokenType of a literal token.
	 *
	 * @param token The literal token.
	 * @return The corresponding TokenType.
	 */
	private TokenType literalType(Token token) {
		return switch (token.getType()) {
			case INT_LITERAL -> TokenType.INT;
			case DOUBLE_LITERAL -> TokenType.DOUBLE;
			case STRING_LITERAL -> TokenType.STRING;
			case BOOLEAN_LITERAL -> TokenType.BOOLEAN;
			case CHAR_LITERAL -> TokenType.CHAR;
			default -> throw new SemanticException("Invalid literal");
		};
	}

	/**
	 * Checks if a TokenType is valid for condition operands (boolean, int, double).
	 *
	 * @param t The TokenType to check.
	 * @return True if valid for conditions, false otherwise.
	 */
	private boolean isConditionOperandType(TokenType t) {
		return t == TokenType.BOOLEAN || t == TokenType.INT || t == TokenType.DOUBLE;
	}

	/**
	 * Checks if a value of source type can be assigned to a target type.
	 *
	 * @param target The target TokenType.
	 * @param source The source TokenType.
	 * @return True if assignable, false otherwise.
	 */
	private boolean isAssignable(TokenType target, TokenType source) {
		if (target == source) return true;

		// numeric promotion
		if (target == TokenType.DOUBLE && source == TokenType.INT) return true;

		// boolean can accept int/double
		return target == TokenType.BOOLEAN &&
		       (source == TokenType.INT || source == TokenType.DOUBLE);
	}
}