package ex5.semantic;

import ex5.ast.statements.MethodArgument;

import java.util.List;

public class MethodSymbol {

	private final String identifier;
	private final List<MethodArgument> parameters;

	public MethodSymbol(String identifier, List<MethodArgument> parameters) {
		this.identifier = identifier;
		this.parameters = parameters;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<MethodArgument> getParameters() {
		return parameters;
	}
}
