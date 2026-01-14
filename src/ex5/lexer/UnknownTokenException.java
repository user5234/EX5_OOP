package ex5.lexer;

/**
 * An exception thrown when an unknown token is encountered during lexing.
 */
public class UnknownTokenException extends Exception {

	/**
	 * Constructs an UnknownTokenException with the given message.
	 * @param message the exception message
	 */
	public UnknownTokenException(String message) {
		super(message);
	}
}
