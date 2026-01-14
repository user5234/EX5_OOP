package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.ast.expressions.Expression;

public class IfStatement extends Statement {

	private final Expression condition;
	private final Block body;

	public IfStatement(Expression condition, Block body) {
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
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitIfStatement(this);
	}

	@Override
	public String print() {
		return "if\n" + "(" + condition.print() + ")\n" + body.print().indent(4);
	}
}
