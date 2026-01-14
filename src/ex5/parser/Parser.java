package ex5.parser;

import ex5.ast.expressions.*;
import ex5.ast.statements.*;
import ex5.lexer.*;

import java.util.*;

public class Parser {

	private final TokenStream ts;

	public Parser(List<Token> tokens) {
		this.ts = new TokenStream(tokens);
	}

	public List<Statement> parseProgram() throws UnexpectedTokenException {
		List<Statement> statements = new ArrayList<>();
		while (!ts.isAtEnd()) {
			statements.add(parseStatement());
		}
		return statements;
	}

	private Statement parseStatement() throws UnexpectedTokenException {
		TokenType type = ts.peek().getType();

		return switch (type) {
			case IF -> parseIf();
			case WHILE -> parseWhile();
			case RETURN -> parseReturn();
			case INT, DOUBLE, STRING, BOOLEAN, CHAR -> parseVariableDeclaration();
			case VOID -> parseMethodDeclaration();
			default -> parseVariableAssignment();
		};
	}

	private IfStatement parseIf() throws UnexpectedTokenException {
		ts.expect(TokenType.IF);
		ts.expect(TokenType.LPAREN);

		var condition = parseExpression();

		ts.expect(TokenType.RPAREN);

		var thenBranch = parseBlock();

		return new IfStatement(condition, thenBranch);
	}

	private WhileStatement parseWhile() throws UnexpectedTokenException {
		ts.expect(TokenType.WHILE);
		ts.expect(TokenType.LPAREN);

		var condition = parseExpression();

		ts.expect(TokenType.RPAREN);

		var body = parseBlock();

		return new WhileStatement(condition, body);
	}

	private ReturnStatement parseReturn() throws UnexpectedTokenException {
		ts.expect(TokenType.RETURN);
		ts.expect(TokenType.SEMICOLON);

		return new ReturnStatement();
	}

	private VariableDeclaration parseVariableDeclaration() throws UnexpectedTokenException {
		var type = ts.consume().getType();
		var name = ts.expect(TokenType.IDENTIFIER);

		ts.expect(TokenType.ASSIGN);

		var value = parseExpression();
		ts.expect(TokenType.SEMICOLON);

		return new VariableDeclaration(type, name.getValue(), value);
	}

	private MethodDeclaration parseMethodDeclaration() throws UnexpectedTokenException {
		ts.expect(TokenType.VOID);
		var name = ts.expect(TokenType.IDENTIFIER);

		var arguments = parseMethodArguments();
		var body = parseBlock();

		return new MethodDeclaration(name.getValue(), arguments, body);
	}

	private VariableAssignment parseVariableAssignment() throws UnexpectedTokenException {
		var name = ts.expect(TokenType.IDENTIFIER);
		ts.expect(TokenType.ASSIGN);

		var value = parseExpression();
		ts.expect(TokenType.SEMICOLON);

		return new VariableAssignment(name.getValue(), value);
	}

	private Block parseBlock() throws UnexpectedTokenException {
		ts.expect(TokenType.LBRACE);

		var statements = new ArrayList<Statement>();
		while (!ts.match(TokenType.RBRACE)) {
			statements.add(parseStatement());
		}

		return new Block(statements);
	}

	private Expression parseExpression() throws UnexpectedTokenException {
		var t = ts.peek();

		return switch (t.getType()) {
			case INT_LITERAL, DOUBLE_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL, CHAR_LITERAL ->
					new LiteralExpression(ts.consume());

			case IDENTIFIER -> new VariableExpression(ts.consume().getValue());

			default -> throw new UnexpectedTokenException("Invalid expression: " + t);
		};
	}

	private List<MethodArgument> parseMethodArguments() throws UnexpectedTokenException {
		var arguments = new ArrayList<MethodArgument>();

		ts.expect(TokenType.LPAREN);

		while (ts.peek().getType() != TokenType.RPAREN) {
			var type = ts.consume().getType();

			switch (type) {
				case INT, DOUBLE, STRING, BOOLEAN, CHAR -> {}
				default -> throw new UnexpectedTokenException("Invalid argument type: " + type);
			}

			var name = ts.expect(TokenType.IDENTIFIER);

			arguments.add(new MethodArgument(type, name.getValue()));
		}

		ts.expect(TokenType.RPAREN);

		return arguments;
	}
}




