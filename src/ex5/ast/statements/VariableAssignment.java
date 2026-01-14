package ex5.ast.statements;

import ex5.ast.expressions.Expression;

public class VariableAssignment extends Statement {

	public final String identifier;
	public final Expression expression;

	public VariableAssignment(String identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}
}
