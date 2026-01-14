package ex5.ast.statements;

import ex5.ast.expressions.Expression;

public class WhileStatement extends Statement {

	private final Expression condition;
	private final Block body;

	public WhileStatement(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	public Expression getCondition() {
		return condition;
	}

	public Block getBody() {
		return body;
	}

	@Override
	public String print() {
		return "while\n" + "(" + condition.print() + "}\n" + body.print().indent(4);
	}
}
