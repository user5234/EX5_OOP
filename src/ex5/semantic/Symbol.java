package ex5.semantic;

import ex5.lexer.TokenType;

public class Symbol {

    private final String identifier;
    private final TokenType type;
    private boolean initialized;

    public Symbol(String identifier, TokenType type, boolean initialized) {
        this.identifier = identifier;
        this.type = type;
        this.initialized = initialized;
    }

    public Symbol(String identifier, TokenType type) {
        this(identifier, type, true); 
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
}
