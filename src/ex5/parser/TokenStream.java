package ex5.parser;

import ex5.lexer.Token;
import ex5.lexer.TokenType;

import java.util.List;

public class TokenStream {

	private final List<Token> tokens;
	private int pos = 0;

	public TokenStream(List<Token> tokens) {
		this.tokens = tokens;
	}

	public Token peek() {
		return tokens.get(pos);
	}

	public Token peek(int k) {
	    int idx = Math.min(pos + k, tokens.size() - 1);
	    return tokens.get(idx);
	}
	

	public boolean isAtEnd() {
		return pos >= tokens.size();
	}

	public Token consume() {
		return tokens.get(pos++);
	}

	public boolean match(TokenType type) {
		if (!isAtEnd() && peek().getType() == type) {
			consume();
			return true;
		}
		return false;
	}

	public Token expect(TokenType type) throws UnexpectedTokenException {
		if (isAtEnd()) {
			throw new UnexpectedTokenException("Expected " + type + " but reached end of input");
		}

		var t = consume();
		if (t.getType() != type) {
			throw new UnexpectedTokenException("Expected " + type + " but got " + t.getType());
		}
		return t;
	}
}
