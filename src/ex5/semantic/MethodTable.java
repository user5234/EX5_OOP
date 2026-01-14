package ex5.semantic;

import java.util.*;

public class MethodTable {

	private final Map<String, MethodSymbol> methods = new HashMap<>();

	public void define(MethodSymbol method) {
		if (methods.containsKey(method.getIdentifier())) {
			throw new SemanticException("Method already declared: " + method.getIdentifier());
		}
		methods.put(method.getIdentifier(), method);
	}

	public MethodSymbol resolve(String name) {
		if (!methods.containsKey(name)) {
			throw new SemanticException("Undefined method: " + name);
		}
		return methods.get(name);
	}
}
