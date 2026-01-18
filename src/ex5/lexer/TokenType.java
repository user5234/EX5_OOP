package ex5.lexer;

import java.util.regex.*;

/**
 * An enumeration of token types with their corresponding regex patterns.
 *
 * @author galart27
 * @author noam_wein
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
	STRING("String"),
	BOOLEAN("boolean"),
	CHAR("char"),

	// Literals
	DOUBLE_LITERAL("[+-]?(\\d*\\.\\d+|\\d+\\.\\d*)"),
	INT_LITERAL("[+-]?\\d+"),
	STRING_LITERAL("\"[^\"]*\""),
	BOOLEAN_LITERAL("(true|false)"),
	CHAR_LITERAL("'[^']'"),

	// Identifier (must not start with a digit)
	IDENTIFIER("[A-Za-z_][A-Za-z0-9_]*"),


	// Symbols
	OR("\\|\\|"),
	AND("&&"),
	LPAREN("\\("),
	RPAREN("\\)"),
	LBRACE("\\{"),
	RBRACE("\\}"),
	SEMICOLON(";"),
	COMMA(","),
	NEWLINE("\\n"),
	ASSIGN("=");

	private final Pattern pattern;

	/**
	 * Constructs a TokenType with the given regex pattern.
	 *
	 * @param regex the regex pattern for the token type
	 */
	TokenType(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	/**
	 * Returns the regex pattern associated with the token type.
	 *
	 * @return the regex pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}
}
