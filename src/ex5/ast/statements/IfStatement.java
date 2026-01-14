package ex5.ast.statements;

import ex5.ast.expressions.Expression;

public class IfStatement extends Statement {

	public final Expression condition;
	public final Statement body;

	public IfStatement(Expression condition, Statement body) {
		this.condition = condition;
		this.body = body;
	}
}
