package ex5.ast.statements;

import ex5.ast.expressions.Expression;

public class IfStatement extends Statement {

	public final Expression condition;
	public final Block body;

	public IfStatement(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	@Override
	public String print() {
		return "if\n" + "\tcondition: " + condition.print() + "\n" + "\tbody:\n" + body.print();
	}
}
