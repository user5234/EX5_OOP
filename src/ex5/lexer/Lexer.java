package ex5.lexer;

import java.util.*;
import java.util.regex.*;

public final class Lexer {

	private final String line;
	private int pos = 0;

	public Lexer(String line) {
		this.line = line;
	}

	public List<Token> tokenize() throws UnknownTokenException {
		var tokens = new ArrayList<Token>();

		skipWhitespace();

		while (pos < line.length()) {
			var remaining = line.substring(pos);

			var token = currentToken(remaining);
			tokens.add(token);
			pos += token.getValue().length();

			skipWhitespace();
		}
		return tokens;
	}

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

	private void skipWhitespace() {
		while (pos < line.length() && Character.isWhitespace(line.charAt(pos))) {
			pos++;
		}
	}
}
