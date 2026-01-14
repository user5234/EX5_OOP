package ex5.semantic;

import java.util.HashMap;
import java.util.Map;

public class Scope {

	private final Map<String, Symbol> symbols = new HashMap<>();
	private final Scope parent;

	public Scope(Scope parent) {
		this.parent = parent;
	}

	public void define(Symbol symbol) {
		if (symbols.containsKey(symbol.getIdentifier())) {
			throw new SemanticException("Variable already declared: " + symbol.getIdentifier());
		}
		symbols.put(symbol.getIdentifier(), symbol);
	}

	public Symbol resolve(String name) {
		if (symbols.containsKey(name)) {
			return symbols.get(name);
		}
		if (parent != null) {
			return parent.resolve(name);
		}
		throw new SemanticException("Undefined variable: " + name);
	}

	public boolean hasParent() {
		return parent != null;
	}
}
