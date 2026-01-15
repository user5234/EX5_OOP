package ex5.semantic;

import ex5.lexer.TokenType;

public class Symbol {

    private final String identifier;
    private final TokenType type;
	private final boolean isFinal;
    private boolean initialized;

    public Symbol(String identifier, TokenType type, boolean isFinal, boolean initialized) {
        this.identifier = identifier;
        this.type = type;
        this.isFinal = isFinal;
        this.initialized = initialized;
    }

    public Symbol(String identifier, TokenType type) {
        this(identifier, type, false, false); 
    }

    public String getIdentifier() {
        return identifier;
    }

    public TokenType getType() {
        return type;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

	public boolean isFinal() {
		return isFinal;
	}
}
