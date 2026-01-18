package ex5.semantic;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a scope for variable symbols in the semantic analysis phase.
 */
public class Scope {

	private final Map<String, Symbol> symbols = new HashMap<>();
	private final Map<String, Boolean> initialized = new HashMap<>();
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
	 * Resolves a variable symbol by its identifier, searching in the current scope and parent
	 * scopes.
	 *
	 * @param identifier The identifier of the variable to resolve.
	 * @return The corresponding Symbol.
	 * @throws SemanticException If the variable is not defined in any accessible scope.
	 */
	public Symbol resolve(String identifier) {
		if (symbols.containsKey(identifier)) {
			return symbols.get(identifier);
		}
		if (parent != null) {
			return parent.resolve(identifier);
		}
		throw new SemanticException("Undefined variable: " + identifier);
	}

	public void setInitialized(String identifier, boolean isInitialized) {
		if (symbols.containsKey(identifier)) {
			initialized.put(identifier, isInitialized);
			return;
		}
		if (parent != null) {
			parent.setInitialized(identifier, isInitialized);
			return;
		}
		throw new SemanticException("Undefined variable: " + identifier);
	}

	public boolean isInitialized(String identifier) {
		if (initialized.containsKey(identifier)) {
			return initialized.get(identifier);
		}
		if (parent != null) {
			return parent.isInitialized(identifier);
		}
		throw new SemanticException("Undefined variable: " + identifier);
	}

	/**
	 * Checks if this scope has a parent scope.
	 *
	 * @return True if there is a parent scope, false otherwise.
	 */
	public boolean hasParent() {
		return parent != null;
	}
}