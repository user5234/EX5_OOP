package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.ast.expressions.Expression;

/**
 * Represents a while statement in the AST.
 */
public class WhileStatement extends Statement {

	private final Expression condition;
	private final Block body;

	/**
	 * Constructs a WhileStatement node.
	 *
	 * @param condition The condition expression for the while loop.
	 * @param body      The body block of the while loop.
	 */
	public WhileStatement(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	/**
	 * Gets the condition expression of the while loop.
	 * @return The condition expression.
	 */
	public Expression getCondition() {
		return condition;
	}

	/**
	 * Gets the body block of the while loop.
	 * @return The body block.
	 */
	public Block getBody() {
		return body;
	}

	/**
	 * Accepts a visitor that implements the ASTVisitor interface.
	 * @param visitor the visitor to accept
	 * @return the result of the visitor's operation
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitWhileStatement(this);
	}

	/**
	 * Returns a string representation of the while statement.
	 * @return A string representing the while statement.
	 */
	@Override
	public String print() {
		return "while\n" + "(" + condition.print() + "}\n" + body.print().indent(4);
	}
}
