package ex5.semantic;

import ex5.ast.*;
import ex5.ast.expressions.LiteralExpression;
import ex5.ast.expressions.MethodCall;
import ex5.ast.expressions.VariableExpression;
import ex5.ast.expressions.LogicalExpression;
import ex5.ast.statements.*;
import ex5.lexer.Token;
import ex5.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer implements ASTVisitor<TokenType> {

	private final MethodTable methodTable;
	private final List<MethodDeclaration> deferredMethods;
	private Scope currentScope;

	public SemanticAnalyzer() {
		methodTable = new MethodTable();
		deferredMethods = new ArrayList<>();
		currentScope = new Scope(null);
	}

	public void analyze(List<Statement> statements) {

		for (var s : statements) {
			if (s instanceof MethodDeclaration md) {
				deferredMethods.add(md);
			}
			else {
				s.accept(this);
			}
		}

		for (var md : deferredMethods) {
			md.accept(this);
		}
	}

	@Override
	public void visitBlock(Block bl) {
		var scope = currentScope;
		currentScope = new Scope(scope);

		for (var s : bl.getStatements()) {
			s.accept(this);
		}

		currentScope = scope;
	}

	@Override
	public void visitIfStatement(IfStatement is) {
		var conditionType = is.getCondition().accept(this);
		if (!isConditionOperandType(conditionType)) {
			throw new SemanticException("If condition must be boolean");
		}

		is.getBody().accept(this);
	}


	@Override
	public void visitMethodArgument(MethodArgument ma) {
	    currentScope.define(new Symbol(ma.getIdentifier(), ma.getType(), false, true));
	}


	@Override
	public void visitMethodDeclaration(MethodDeclaration md) {
		if (currentScope.hasParent()) {
			throw new SemanticException("Method " +
			                            md.getIdentifier() +
			                            " cannot be declared inside another method");
		}

		var scope = currentScope;
		currentScope = new Scope(currentScope);
		methodTable.define(new MethodSymbol(md.getIdentifier(), md.getArguments()));

		for (var param : md.getArguments()) {
			param.accept(this);
		}

		md.getBody().accept(this);

		var statements = md.getBody().getStatements();

		if (
				statements.isEmpty() ||
				!(statements.get(statements.size() - 1) instanceof ReturnStatement)
		) {
			throw new SemanticException("Method " +
			                            md.getIdentifier() +
			                            " must end with a return statement");
		}

		currentScope = scope;
	}


	@Override
	public void visitReturnStatement(ReturnStatement rs) {}


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



	@Override
	public void visitVariableDeclaration(VariableDeclaration vs) {
	    boolean initd = (vs.getInitializer() != null);	

	    if (initd) {
	        var initType = vs.getInitializer().accept(this);
	        if (!isAssignable(vs.getType(), initType)) {
	            throw new SemanticException("Type mismatch: cannot assign " +
	                    initType + " to " + vs.getType());
	        }
	    }	

	    // define symbol with init state and final
	    currentScope.define(new Symbol(vs.getIdentifier(), vs.getType(), vs.isFinal(), initd));
	}



	public void visitWhileStatement(WhileStatement ws) {
		var conditionType = ws.getCondition().accept(this);
		if (!isConditionOperandType(conditionType)) {
			throw new SemanticException("While condition must be boolean");
		}

		ws.getBody().accept(this);
	}

	@Override
	public TokenType visitLiteralExpression(LiteralExpression le) {
		return literalType(le.getLiteral());
	}

	@Override
	public TokenType visitVariableExpression(VariableExpression ve) {
	    var sym = currentScope.resolve(ve.getIdentifier());
	    if (!sym.isInitialized()) {
	        throw new SemanticException("Variable " + ve.getIdentifier() + " used before initialization");
	    }
	    return sym.getType();
	}


	@Override
	public TokenType visitMethodCall(MethodCall mc) {
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

		return TokenType.VOID;
	}

	@Override
	public TokenType visitLogicalExpression(LogicalExpression le) {
	    TokenType left = le.getLeft().accept(this);
	    TokenType right = le.getRight().accept(this);

	    if (!isConditionOperandType(left) || !isConditionOperandType(right)) {
	        throw new SemanticException("Operands of &&/|| must be boolean/int/double");
	    }

	    return TokenType.BOOLEAN;
	}


	// ───────── HELPERS ─────────

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

	private boolean isConditionOperandType(TokenType t) {
	  return t == TokenType.BOOLEAN || t == TokenType.INT || t == TokenType.DOUBLE;
	}

	private boolean isAssignable(TokenType target, TokenType source) {
    	if (target == source) return true;
		
    	// numeric promotion
    	if (target == TokenType.DOUBLE && source == TokenType.INT) return true;
		
    	// your rule: boolean can accept int/double
    	if (target == TokenType.BOOLEAN && (source == TokenType.INT || source == TokenType.DOUBLE)) return true;
		
    	return false;
	}
}