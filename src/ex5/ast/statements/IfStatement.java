package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.ast.expressions.Expression;

/**
 * Represents an if statement in the AST.
 */
public class IfStatement extends Statement {

	private final Expression condition;
	private final Block body;

	/**
	 * Constructs an IfStatement with the given condition and body.
	 *
	 * @param condition the condition expression
	 * @param body      the body block to execute if the condition is true
	 */
	public IfStatement(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	/**
	 * Returns the condition expression of the if statement.
	 *
	 * @return the condition expression
	 */
	public Expression getCondition() {
		return condition;
	}

	/**
	 * Returns the body block of the if statement.
	 *
	 * @return the body block
	 */
	public Block getBody() {
		return body;
	}

	/**
	 * Accepts a visitor to process this if statement.
	 * @param visitor the AST visitor
	 * @return the result of the visitor's processing
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitIfStatement(this);
	}

	/**
	 * Returns a string representation of the if statement.
	 *
	 * @return a string representation of the if statement
	 */
	@Override
	public String print() {
		return "if\n" + "(" + condition.print() + ")\n" + body.print().indent(4);
	}
}
