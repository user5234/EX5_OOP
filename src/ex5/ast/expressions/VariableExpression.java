package ex5.ast.expressions;

import ex5.ast.ASTVisitor;

public class VariableExpression extends Expression {

	private final String identifier;

	public VariableExpression(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitVariableExpression(this);
	}

	@Override
	public String print() {
		return identifier;
	}
}
