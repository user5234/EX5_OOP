package ex5.parser;

import ex5.ast.expressions.*;
import ex5.ast.statements.*;
import ex5.lexer.*;

import java.util.*;

/**
 * Parser for the Sjavac language.
 */
public class Parser {

	private final TokenStream ts;

	/**
	 * Constructs a Parser with the given list of tokens.
	 *
	 * @param tokens List of tokens to parse.
	 */
	public Parser(List<Token> tokens) {
		ts = new TokenStream(tokens);
	}

	/**
	 * Parses the entire program and returns a list of statements.
	 *
	 * @return List of parsed statements.
	 * @throws UnexpectedTokenException if an unexpected token is encountered.
	 */
	public List<Statement> parseProgram() {
		var statements = new ArrayList<Statement>();
		while (!ts.isAtEnd()) {
			statements.addAll(parseStatement());
		}
		return statements;
	}

	/*
	 * Parses a single statement based on the next token type.
	 */
	private List<? extends Statement> parseStatement() {
		var type = ts.peek().getType();

		return switch (type) {
			case IF -> List.of(parseIf());
			case WHILE -> List.of(parseWhile());
			case RETURN -> List.of(parseReturn());
			case FINAL, INT, DOUBLE, STRING, BOOLEAN, CHAR -> parseVariableDeclarations();
			case VOID -> List.of(parseMethodDeclaration());
			case IDENTIFIER -> (ts.peek(1).getType() == TokenType.LPAREN) // method call
					? List.of(parseMethodCall())
					: parseVariableAssignments();
			default -> throw new UnexpectedTokenException("Unexpected token: " + ts.peek().getType());
		};
	}

	/*
	 * Parses a method call.
	 */
	private MethodCall parseMethodCall() {
		var name = ts.expect(TokenType.IDENTIFIER);

		ts.expect(TokenType.LPAREN);

		var args = new ArrayList<Expression>();
		if (ts.peek().getType() != TokenType.RPAREN) {
			do {
				args.add(parseExpression());
			} while (ts.match(TokenType.COMMA));
		}

		ts.expect(TokenType.RPAREN);
		ts.expect(TokenType.SEMICOLON);
		ts.expect(TokenType.NEWLINE);

		return new MethodCall(name.getValue(), args);
	}

	/*
	 * Parses an if statement.
	 */
	private IfStatement parseIf() {
		ts.expect(TokenType.IF);

		ts.expect(TokenType.LPAREN);
		var condition = parseCondition();
		ts.expect(TokenType.RPAREN);

		var body = parseBlock();

		return new IfStatement(condition, body);
	}

	/*
	 * Parses a while statement.
	 */
	private WhileStatement parseWhile() {
		ts.expect(TokenType.WHILE);

		ts.expect(TokenType.LPAREN);
		var condition = parseCondition();
		ts.expect(TokenType.RPAREN);

		var body = parseBlock();

		return new WhileStatement(condition, body);
	}

	/*
	 * Parses a return statement.
	 */
	private ReturnStatement parseReturn() {
		ts.expect(TokenType.RETURN);
		ts.expect(TokenType.SEMICOLON);
		ts.expect(TokenType.NEWLINE);
		return new ReturnStatement();
	}

	/*
	 * Parses a method declaration.
	 */
	private MethodDeclaration parseMethodDeclaration() {
		ts.expect(TokenType.VOID);
		var identifier = ts.expect(TokenType.IDENTIFIER);

		ts.expect(TokenType.LPAREN);
		var arguments = parseMethodArguments();
		ts.expect(TokenType.RPAREN);

		var body = parseBlock();

		return new MethodDeclaration(identifier.getValue(), arguments, body);
	}

	/*
	 * Parses a variable assignment statement.
	 */
	private List<VariableAssignment> parseVariableAssignments() {
		var assignments = new ArrayList<VariableAssignment>();

		do {
			var identifier = ts.expect(TokenType.IDENTIFIER);
			ts.expect(TokenType.ASSIGN);
			var value = parseExpression();

			assignments.add(new VariableAssignment(identifier.getValue(), value));
		} while (ts.match(TokenType.COMMA));

		ts.expect(TokenType.SEMICOLON);
		ts.expect(TokenType.NEWLINE);

		return assignments;
	}

	/*
	 * Parses a block of statements enclosed in braces.
	 */
	private Block parseBlock() {
		ts.expect(TokenType.LBRACE);
		ts.expect(TokenType.NEWLINE);

		var statements = new ArrayList<Statement>();
		while (!ts.match(TokenType.RBRACE)) {
			statements.addAll(parseStatement());
		}

		ts.expect(TokenType.NEWLINE);
		return new Block(statements);
	}

	/*
	 * Parses variable declarations, possibly multiple in one statement.
	 */
	private List<VariableDeclaration> parseVariableDeclarations() {
		boolean isFinal = ts.match(TokenType.FINAL);

		var type = ts.consume().getType();
		switch (type) {
			case INT, DOUBLE, STRING, BOOLEAN, CHAR -> {
			}
			default -> throw new UnexpectedTokenException("Invalid variable type: " + type);
		}

		var declarations = new ArrayList<VariableDeclaration>();

		do {
			var identifier = ts.expect(TokenType.IDENTIFIER);
			var initializer = ts.match(TokenType.ASSIGN) ? parseExpression() : null;

			declarations.add(
					new VariableDeclaration(type, identifier.getValue(), initializer, isFinal)
			);

		} while (ts.match(TokenType.COMMA));

		ts.expect(TokenType.SEMICOLON);
		ts.expect(TokenType.NEWLINE);
		return declarations;
	}


	/*
	 * Parses an expression.
	 */
	private Expression parseExpression() {
		var t = ts.peek();

		return switch (t.getType()) {
			case INT_LITERAL, DOUBLE_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL, CHAR_LITERAL ->
					new LiteralExpression(ts.consume());

			case IDENTIFIER -> new VariableExpression(ts.consume().getValue());

			default -> throw new UnexpectedTokenException("Invalid expression: " + t);
		};
	}

	/*
	 * Parses a condition with logical operators.
	 */
	private Expression parseCondition() {
		var left = parseConditionAtom();

		while (true) {

			if (ts.match(TokenType.AND)) {
				var right = parseConditionAtom();
				left = new LogicalExpression(left, TokenType.AND, right);
			}
			else if (ts.match(TokenType.OR)) {
				var right = parseConditionAtom();
				left = new LogicalExpression(left, TokenType.OR, right);
			}
			else {
				break;
			}
		}
		return left;
	}

	/*
	 * Parses a condition atom (literal or variable).
	 */
	private Expression parseConditionAtom() {
		var t = ts.peek();

		return switch (t.getType()) {
			case BOOLEAN_LITERAL, INT_LITERAL, DOUBLE_LITERAL ->
					new LiteralExpression(ts.consume());

			case IDENTIFIER -> new VariableExpression(ts.consume().getValue());

			default -> throw new UnexpectedTokenException("Invalid condition atom: " + t);
		};
	}

	/*
	 * Parses method arguments within parentheses.
	 */
	private List<MethodArgument> parseMethodArguments() {
		var arguments = new ArrayList<MethodArgument>();

		if (ts.peek().getType() == TokenType.RPAREN) {
			return arguments;
		}

		do {
			var type = ts.consume().getType();
			switch (type) {
				case INT, DOUBLE, STRING, BOOLEAN, CHAR -> {
				}
				default -> throw new UnexpectedTokenException("Invalid argument type: " + type);
			}
			var name = ts.expect(TokenType.IDENTIFIER);
			arguments.add(new MethodArgument(type, name.getValue()));

		} while (ts.match(TokenType.COMMA));

		return arguments;
	}
}




