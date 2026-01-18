package ex5.semantic;

import ex5.lexer.TokenType;

/**
 * Represents a symbol in the semantic analysis phase.
 *
 * @author galart27
 * @author noam_wein
 */
public class Symbol {

	private final String identifier;
	private final TokenType type;
	private final boolean isFinal;

	/**
	 * Constructs a Symbol with the given properties.
	 *
	 * @param identifier  The name of the symbol.
	 * @param type        The type of the symbol.
	 * @param isFinal     Whether the symbol is final.
	 */
	public Symbol(String identifier, TokenType type, boolean isFinal) {
		this.identifier = identifier;
		this.type = type;
		this.isFinal = isFinal;
	}

	/**
	 * Constructs a Symbol with the given identifier and type.
	 * The symbol is not final and not initialized by default.
	 *
	 * @param identifier The name of the symbol.
	 * @param type       The type of the symbol.
	 */
	public Symbol(String identifier, TokenType type) {
		this(identifier, type, false);
	}

	/**
	 * Returns the identifier of the symbol.
	 *
	 * @return The symbol's name.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the type of the symbol.
	 *
	 * @return The symbol's type.
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Checks if the symbol is final.
	 *
	 * @return True if final, false otherwise.
	 */
	public boolean isFinal() {
		return isFinal;
	}
}
