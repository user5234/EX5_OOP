package ex5.parser;

/**
 * Exception thrown when an unexpected token is encountered during parsing.
 */
public class UnexpectedTokenException extends Exception {
	public UnexpectedTokenException(String message) {
		super(message);
	}
}
