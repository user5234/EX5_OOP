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
	public List<Token> tokenize(String line) throws UnknownTokenException {
    	var tokens = new ArrayList<Token>();
    	pos = 0;

    	// (Recommended) remove inline comments too
    	int commentIdx = line.indexOf("//");
    	if (commentIdx >= 0) {
    	    line = line.substring(0, commentIdx);
    	}

    	skipWhitespace(line);

    	while (pos < line.length()) {
    	    var remaining = line.substring(pos);

    	    var token = currentToken(remaining);
    	    tokens.add(token);
    	    pos += token.getValue().length();

    	    // Require newline after '}' always:
    	    // meaning: after a right brace, the rest of the line must be only whitespace.
    	    if (token.getType() == TokenType.RBRACE) { // adjust name to your enum
    	        skipWhitespace(line);
    	        if (pos < line.length()) {
    	            throw new UnknownTokenException("Expected newline after '}'");
    	        }
    	        break; // done with this line
    	    }

    	    skipWhitespace(line);
    	}

    	// Always end each input line with a NEWLINE token
    	tokens.add(new Token(TokenType.NEWLINE, "\n"));
    	return tokens;
	}


	/**
	 * Identifies the current token from the input string.
	 * @param input the remaining input string
	 * @return the identified token
	 * @throws UnknownTokenException if no valid token is found
	 */
	private Token currentToken(String input) throws UnknownTokenException {
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
