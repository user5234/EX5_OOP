package ex5.ast.statements;

import ex5.ast.expressions.Expression;

public class VariableAssignment extends Statement {

	private final String identifier;
	private final Expression expression;

	public VariableAssignment(String identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String print() {
		return identifier + " = " + expression.print();
	}
}
