package ex5.parser;

/**
 * Exception thrown when an unexpected token is encountered during parsing.
 *
 * @author galart27
 * @author noam_wein
 */
public class UnexpectedTokenException extends RuntimeException {
	public UnexpectedTokenException(String message) {
		super(message);
	}
}
