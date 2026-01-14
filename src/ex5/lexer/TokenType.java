package ex5.lexer;

import java.util.regex.*;

/**
 * An enumeration of token types with their corresponding regex patterns.
 */
public enum TokenType {

	// Keywords
	IF("if"),
	WHILE("while"),
	RETURN("return"),
	FINAL("final"),

	// Types
	VOID("void"),
	INT("int"),
	DOUBLE("double"),
	STRING("string"),
	BOOLEAN("boolean"),
	CHAR("char"),

	// Literals
	INT_LITERAL("-?\\d+"),
	DOUBLE_LITERAL("-?\\d+\\.\\d+"),
	STRING_LITERAL("\".*\""),
	BOOLEAN_LITERAL("(true|false)"),
	CHAR_LITERAL("'.*'"),

	// Identifier
	IDENTIFIER("\\w+"),

	// Symbols
	OR("\\|\\|"),
	AND("&&"),
	LPAREN("\\("),
	RPAREN("\\)"),
	LBRACE("\\{"),
	RBRACE("\\}"),
	SEMICOLON(";"),
	COMMA(","),
	ASSIGN("=");

	private final Pattern pattern;

	/**
	 * Constructs a TokenType with the given regex pattern.
	 * @param regex the regex pattern for the token type
	 */
	TokenType(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	/**
	 * Returns the regex pattern associated with the token type.
	 * @return the regex pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}
}
