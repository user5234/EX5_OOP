package ex5.semantic;

import ex5.ast.statements.MethodArgument;

import java.util.List;

/**
 * Represents a method symbol in the semantic analysis phase.
 *
 * @author galart27
 * @author noam_wein
 */
public class MethodSymbol {

	private final String identifier;
	private final List<MethodArgument> parameters;

	/**
	 * Constructs a MethodSymbol with the given identifier and parameters.
	 *
	 * @param identifier The name of the method.
	 * @param parameters The list of method arguments.
	 */
	public MethodSymbol(String identifier, List<MethodArgument> parameters) {
		this.identifier = identifier;
		this.parameters = parameters;
	}

	/**
	 * Returns the identifier of the method.
	 *
	 * @return The method's name.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the list of method parameters.
	 *
	 * @return The method's parameters.
	 */
	public List<MethodArgument> getParameters() {
		return parameters;
	}
}
