package ex5.semantic;

import java.util.*;

/**
 * Represents a table of method symbols for semantic analysis.
 *
 * @author galart27
 * @author noam_wein
 */
public class MethodTable {

	private final Map<String, MethodSymbol> methods = new HashMap<>();

	/**
	 * Defines a new method in the method table.
	 *
	 * @param method The method symbol to define.
	 * @throws SemanticException If the method is already defined.
	 */
	public void define(MethodSymbol method) {
		if (methods.containsKey(method.getIdentifier())) {
			throw new SemanticException("Method already declared: " + method.getIdentifier());
		}
		methods.put(method.getIdentifier(), method);
	}

	/**
	 * Resolves a method by its name.
	 *
	 * @param name The name of the method to resolve.
	 * @return The corresponding MethodSymbol.
	 * @throws SemanticException If the method is not defined.
	 */
	public MethodSymbol resolve(String name) {
		if (!methods.containsKey(name)) {
			throw new SemanticException("Undefined method: " + name);
		}
		return methods.get(name);
	}
}
