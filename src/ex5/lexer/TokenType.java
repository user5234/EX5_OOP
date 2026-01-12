package ex5.lexer;

import java.util.regex.*;

public enum TokenType {

	// Keywords
	IF("if"),
	WHILE("while"),
	RETURN("return"),

	// Types
	VOID("void"),
	INT("int"),
	DOUBLE("double"),
	STRING("string"),
	BOOLEAN("boolean"),
	CHAR("char"),

	// Literals
	INT_LITERAL("\\d+"),
	DOUBLE_LITERAL("\\d+\\.\\d+"),
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
	LBRACE("{"),
	RBRACE("}"),
	SEMICOLON(";"),
	COMMA(","),
	ASSIGN("=");

	private final Pattern pattern;

	TokenType(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	public Pattern getPattern() {
		return pattern;
	}
}
