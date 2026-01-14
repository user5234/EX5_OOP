package ex5.semantic;

import ex5.lexer.TokenType;

public class Symbol {

	private final String identifier;
	private final TokenType type;

	public Symbol(String identifier, TokenType type) {
		this.identifier = identifier;
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public TokenType getType() {
		return type;
	}
}
