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
		consumeNewlines();
	    while (!ts.isAtEnd()) {
	        statements.addAll(parseStatement());
			consumeNewlines();
	    }
	    return statements;
	}

	private List<Statement> parseStatement() throws UnexpectedTokenException {
	    TokenType type = ts.peek().getType();

	    return switch (type) {
	        case IF -> List.of(parseIf());
	        case WHILE -> List.of(parseWhile());
	        case RETURN -> List.of(parseReturn());
	        case FINAL, INT, DOUBLE, STRING, BOOLEAN, CHAR -> parseVariableDeclarations();
	        case VOID -> List.of(parseMethodDeclaration());
	        case IDENTIFIER -> List.of(parseIdentifierStartingStatement()); // NEW
	        default -> throw new UnexpectedTokenException("Unexpected token: " + ts.peek());
	    };
	}

	private Statement parseIdentifierStartingStatement() throws UnexpectedTokenException {
	    // Lookahead: IDENTIFIER followed by '(' means method call statement
	    if (ts.peek(1).getType() == TokenType.LPAREN) {   // you need peek(int)
	        var call = parseMethodCall();
	        ts.expect(TokenType.SEMICOLON);
	        expectNewline();
	        return new MethodCallStatement(call);
	    }

	    // Otherwise must be assignment
	    return parseVariableAssignment();
	}

	private MethodCall parseMethodCall() throws UnexpectedTokenException {
	    var name = ts.expect(TokenType.IDENTIFIER);
		
	    ts.expect(TokenType.LPAREN);
		
	    var args = new ArrayList<Expression>();
	    if (ts.peek().getType() != TokenType.RPAREN) {
	        args.add(parseExpression());
	        while (ts.match(TokenType.COMMA)) {
	            args.add(parseExpression());
	        }
	    }
	
	    ts.expect(TokenType.RPAREN);
	
	    return new MethodCall(name.getValue(), args);
	}
	

	private IfStatement parseIf() throws UnexpectedTokenException {
		ts.expect(TokenType.IF);
		ts.expect(TokenType.LPAREN);

		var condition = parseCondition();
		ts.expect(TokenType.RPAREN);

		var thenBranch = parseBlock();

		return new IfStatement(condition, thenBranch);
	}

	private WhileStatement parseWhile() throws UnexpectedTokenException {
		ts.expect(TokenType.WHILE);
		ts.expect(TokenType.LPAREN);

		var condition = parseCondition();

		ts.expect(TokenType.RPAREN);

		var body = parseBlock();

		return new WhileStatement(condition, body);
	}

	private ReturnStatement parseReturn() throws UnexpectedTokenException {
	    ts.expect(TokenType.RETURN);
	    ts.expect(TokenType.SEMICOLON);
	    expectNewline();                 
	    return new ReturnStatement();
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
	    expectNewline();                 

	    return new VariableAssignment(name.getValue(), value);
	}

	private Block parseBlock() throws UnexpectedTokenException {
	ts.expect(TokenType.LBRACE);
	consumeNewlines();   
	var statements = new ArrayList<Statement>();
	while (!ts.match(TokenType.RBRACE)) {
	    statements.addAll(parseStatement());
	    consumeNewlines(); 
	}
	expectNewline();     
	return new Block(statements);
	}

	private List<Statement> parseVariableDeclarations() throws UnexpectedTokenException {
	   boolean isFinal = ts.match(TokenType.FINAL);

	   TokenType type = ts.consume().getType();
	   switch (type) {
	       case INT, DOUBLE, STRING, BOOLEAN, CHAR -> {}
	       default -> throw new UnexpectedTokenException("Invalid variable type: " + type);
	   }

	   var decls = new ArrayList<Statement>();

	   while (true) {
	       var nameTok = ts.expect(TokenType.IDENTIFIER);

	       Expression init = null;
	       if (ts.match(TokenType.ASSIGN)) {
	           init = parseExpression();
	       }

	       decls.add(new VariableDeclaration(type, nameTok.getValue(), init, isFinal));

	       if (ts.match(TokenType.COMMA)) {
	           continue;
	       }
	       break;
	   }

	   ts.expect(TokenType.SEMICOLON);
	   expectNewline();
	   return decls;
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

	private Expression parseCondition() throws UnexpectedTokenException {
    	Expression left = parseConditionAtom();

    	while (true) {
    	    if (ts.match(TokenType.AND)) {              // && token type
    	        Expression right = parseConditionAtom();
    	        left = new LogicalExpression(left, TokenType.AND, right);
    	    } else if (ts.match(TokenType.OR)) {        // || token type
    	        Expression right = parseConditionAtom();
    	        left = new LogicalExpression(left, TokenType.OR, right);
    	    } else {
    	        break;
    	    }
    	}
    	return left;
	}

	private Expression parseConditionAtom() throws UnexpectedTokenException {
	    Token t = ts.peek();

	    return switch (t.getType()) {
	        case BOOLEAN_LITERAL, INT_LITERAL, DOUBLE_LITERAL ->
	                new LiteralExpression(ts.consume());

	        case IDENTIFIER ->
	                new VariableExpression(ts.consume().getValue());

	        default -> throw new UnexpectedTokenException("Invalid condition atom: " + t);
	    };
	}


	private List<MethodArgument> parseMethodArguments() throws UnexpectedTokenException {
	    var arguments = new ArrayList<MethodArgument>();
	    ts.expect(TokenType.LPAREN);
	    // Empty parameter list: ()
	    if (ts.peek().getType() == TokenType.RPAREN) {
	        ts.expect(TokenType.RPAREN);
	        return arguments;
	    }
	    while (true) {
	        var type = ts.consume().getType();
	        switch (type) {
	            case INT, DOUBLE, STRING, BOOLEAN, CHAR -> {}
	            default -> throw new UnexpectedTokenException("Invalid argument type: " + type);
	        }
	        var name = ts.expect(TokenType.IDENTIFIER);
	        arguments.add(new MethodArgument(type, name.getValue()));
			
	        // If there's a comma, another parameter must follow
	        if (ts.match(TokenType.COMMA)) {
	            continue;
	        }

	        // Otherwise we must be at ')'
	        ts.expect(TokenType.RPAREN);
	        break;
	    }

	    return arguments;
	}


	private void consumeNewlines() {
	    while (ts.match(TokenType.NEWLINE)) {
	        // keep consuming
	    }
	}

	private void expectNewline() throws UnexpectedTokenException {
	    if (!ts.match(TokenType.NEWLINE)) {
	        throw new UnexpectedTokenException("Expected newline");
	    }
	    consumeNewlines(); // optionally allow multiple blank lines
	}

}




