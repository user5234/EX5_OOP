package ex5.parser;

/**
 * Exception thrown when an unexpected token is encountered during parsing.
 */
public class UnexpectedTokenException extends RuntimeException {
	public UnexpectedTokenException(String message) {
		super(message);
	}
}
