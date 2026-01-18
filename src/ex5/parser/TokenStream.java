package ex5.parser;

import ex5.lexer.Token;
import ex5.lexer.TokenType;

import java.util.List;

/**
 * A stream of tokens for parsing.
 *
 * @author galart27
 * @author noam_wein
 */
public class TokenStream {

	private final List<Token> tokens;
	private int pos = 0;

	/**
	 * Constructs a TokenStream with the given list of tokens.
	 *
	 * @param tokens the list of tokens
	 */
	public TokenStream(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Peeks at the current token without consuming it.
	 *
	 * @return the current token
	 */
	public Token peek() {
		return tokens.get(pos);
	}

	/**
	 * Peeks at the k-th token ahead without consuming it.
	 *
	 * @param k the number of tokens to look ahead
	 * @return the k-th token ahead
	 */
	public Token peek(int k) {
		int idx = Math.min(pos + k, tokens.size() - 1);
		return tokens.get(idx);
	}

	/**
	 * Checks if the end of the token stream has been reached.
	 *
	 * @return true if at the end, false otherwise
	 */
	public boolean isAtEnd() {
		return pos >= tokens.size();
	}

	/**
	 * Consumes and returns the current token.
	 *
	 * @return the consumed token
	 */
	public Token consume() {
		return tokens.get(pos++);
	}

	/**
	 * Matches the current token against the given type.
	 * If it matches, consumes the token and returns true.
	 * Otherwise, returns false without consuming.
	 *
	 * @param type the token type to match
	 * @return true if matched and consumed, false otherwise
	 */
	public boolean match(TokenType type) {
		if (!isAtEnd() && peek().getType() == type) {
			consume();
			return true;
		}
		return false;
	}

	/**
	 * Expects the current token to be of the given type.
	 * If it matches, consumes and returns the token.
	 * Otherwise, throws an UnexpectedTokenException.
	 *
	 * @param type the expected token type
	 * @return the consumed token
	 * @throws UnexpectedTokenException if the token does not match
	 */
	public Token expect(TokenType type) {
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
