package ex5.semantic;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a scope for variable symbols in the semantic analysis phase.
 */
public class Scope {

	private final Map<String, Symbol> symbols = new HashMap<>();
	private final Scope parent;

	/**
	 * Constructs a new Scope with an optional parent scope.
	 *
	 * @param parent The parent scope, or null if this is the global scope.
	 */
	public Scope(Scope parent) {
		this.parent = parent;
	}

	/**
	 * Defines a new variable symbol in the current scope.
	 *
	 * @param symbol The symbol to define.
	 * @throws SemanticException If the symbol is already defined in the current scope.
	 */
	public void define(Symbol symbol) {
		if (symbols.containsKey(symbol.getIdentifier())) {
			throw new SemanticException("Variable already declared: " + symbol.getIdentifier());
		}
		symbols.put(symbol.getIdentifier(), symbol);
	}

	/**
	 * Resolves a variable symbol by its name, searching in the current scope and parent scopes.
	 *
	 * @param name The name of the variable to resolve.
	 * @return The corresponding Symbol.
	 * @throws SemanticException If the variable is not defined in any accessible scope.
	 */
	public Symbol resolve(String name) {
		if (symbols.containsKey(name)) {
			return symbols.get(name);
		}
		if (parent != null) {
			return parent.resolve(name);
		}
		throw new SemanticException("Undefined variable: " + name);
	}

	/**
	 * Checks if this scope has a parent scope.
	 *
	 * @return True if there is a parent scope, false otherwise.
	 */
	public boolean hasParent() {
		return parent != null;
	}

	/**
	 * Checks if a symbol is defined in the current scope or any parent scopes.
	 *
	 * @param name The name of the symbol to check.
	 * @return True if the symbol is defined, false otherwise.
	 */
	public boolean isDefinedInAnyScope(String name) {
	    if (symbols.containsKey(name)) return true;
	    return parent != null && parent.isDefinedInAnyScope(name);
	}
}