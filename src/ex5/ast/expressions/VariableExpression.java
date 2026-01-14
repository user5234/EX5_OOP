package ex5.ast.expressions;

public class VariableExpression extends Expression {

	private final String identifier;

	public VariableExpression(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String print() {
		return identifier;
	}
}
