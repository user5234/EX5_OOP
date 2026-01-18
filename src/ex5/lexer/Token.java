package ex5.lexer;

/**
 * A token representing a lexical unit with a type and value.
 *
 * @author galart27
 * @author noam_wein
 */
public final class Token {

	private final TokenType type;
	private final String value;

	/**
	 * Constructs a Token with the given type and value.
	 *
	 * @param type  the type of the token
	 * @param value the string value of the token
	 */
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the type of the token.
	 *
	 * @return the token type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns the string value of the token.
	 *
	 * @return the token value
	 */
	public String getValue() {
		return value;
	}
}

