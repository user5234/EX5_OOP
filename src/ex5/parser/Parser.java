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
			default -> parseVariableAssignment();
		};
	}

	private Statement parseIf() throws UnexpectedTokenException {
		ts.expect(TokenType.IF);
		ts.expect(TokenType.LPAREN);

		var condition = parseExpression();

		ts.expect(TokenType.RPAREN);

		var thenBranch = parseBlock();

		return new IfStatement(condition, thenBranch);
	}

	private Statement parseWhile() throws UnexpectedTokenException {
		ts.expect(TokenType.WHILE);
		ts.expect(TokenType.LPAREN);

		var condition = parseExpression();

		ts.expect(TokenType.RPAREN);

		var body = parseBlock();

		return new WhileStatement(condition, body);
	}

	private Statement parseReturn() throws UnexpectedTokenException {
		ts.expect(TokenType.RETURN);
		ts.expect(TokenType.LPAREN);

		return new ReturnStatement();
	}

	private Statement parseVariableDeclaration() throws UnexpectedTokenException {
		var type = ts.consume().getType();
		var name = ts.expect(TokenType.IDENTIFIER);

		ts.expect(TokenType.ASSIGN);

		var value = parseExpression();
		ts.expect(TokenType.SEMICOLON);

		return new VariableDeclaration(type, name.getValue(), value);
	}

	private Statement parseVariableAssignment() throws UnexpectedTokenException {
		var name = ts.expect(TokenType.IDENTIFIER);
		ts.expect(TokenType.ASSIGN);

		var value = parseExpression();
		ts.expect(TokenType.SEMICOLON);

		return new VariableAssignment(name.getValue(), value);
	}

	private Statement parseBlock() throws UnexpectedTokenException {
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
}




