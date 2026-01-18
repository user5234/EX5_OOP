package ex5.semantic;

/**
 * Represents a semantic exception that occurs during semantic analysis.
 *
 * @author galart27
 * @author noam_wein
 */
public class SemanticException extends RuntimeException {
	/**
	 * Constructs a SemanticException with the given message.
	 *
	 * @param message The error message.
	 */
	public SemanticException(String message) {
		super(message);
	}
}
