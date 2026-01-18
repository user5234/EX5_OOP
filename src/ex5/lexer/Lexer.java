package ex5.lexer;

import java.util.*;

/**
 * A simple lexer that tokenizes an input string into a list of tokens.
 */
public final class Lexer {

	private int pos;

	/**
	 * Constructs a Lexer
	 */
	public Lexer() {}

	/**
	 * Tokenizes the input line into a list of tokens.
	 * @return a list of tokens
	 * @throws UnknownTokenException if an unknown token is encountered
	 */
	public List<Token> tokenize(String line) {
    	var tokens = new ArrayList<Token>();
    	pos = 0;

    	skipWhitespace(line);

    	while (pos < line.length()) {
    	    var remaining = line.substring(pos);

    	    var token = currentToken(remaining);
    	    tokens.add(token);
    	    pos += token.getValue().length();

    	    skipWhitespace(line);
    	}

    	// Add a newline token at the end of each line
    	tokens.add(new Token(TokenType.NEWLINE, "\n"));
    	return tokens;
	}


	/**
	 * Identifies the current token from the input string.
	 * @param input the remaining input string
	 * @return the identified token
	 * @throws UnknownTokenException if no valid token is found
	 */
	private Token currentToken(String input) {
		for (var type : TokenType.values()) {
			var matcher = type.getPattern().matcher(input);
			if (matcher.lookingAt()) {
				var value = matcher.group();
				return new Token(type, value);
			}
		}
		var token = input.split("\\s+")[0];
		throw new UnknownTokenException("Unknown token " + token + " at position " + pos);
	}   

	/**
	 * Skips whitespace characters in the input line.
	 */
	private void skipWhitespace(String line) {
		while (pos < line.length() && Character.isWhitespace(line.charAt(pos))) {
			pos++;
		}
	}
}
