package ex5.semantic;

import ex5.lexer.TokenType;

/**
 * Represents a symbol in the semantic analysis phase.
 */
public class Symbol {

    private final String identifier;
    private final TokenType type;
	private final boolean isFinal;
    private boolean initialized;

    /**
     * Constructs a Symbol with the given properties.
     *
     * @param identifier  The name of the symbol.
     * @param type        The type of the symbol.
     * @param isFinal     Whether the symbol is final.
     * @param initialized Whether the symbol is initialized.
     */
    public Symbol(String identifier, TokenType type, boolean isFinal, boolean initialized) {
        this.identifier = identifier;
        this.type = type;
        this.isFinal = isFinal;
        this.initialized = initialized;
    }

    /**
     * Constructs a Symbol with the given identifier and type.
     * The symbol is not final and not initialized by default.
     *
     * @param identifier The name of the symbol.
     * @param type       The type of the symbol.
     */
    public Symbol(String identifier, TokenType type) {
        this(identifier, type, false, false); 
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
     * Checks if the symbol is initialized.
     *
     * @return True if initialized, false otherwise.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Sets the initialized status of the symbol.
     *
     * @param initialized The new initialized status.
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
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
