package ex5.lexer;

/**
 * An exception thrown when an unknown token is encountered during lexing.
 *
 * @author galart27
 * @author noam_wein
 */
public class UnknownTokenException extends RuntimeException {

	/**
	 * Constructs an UnknownTokenException with the given message.
	 *
	 * @param message the exception message
	 */
	public UnknownTokenException(String message) {
		super(message);
	}
}
