package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.ast.expressions.Expression;

/**
 * Represents a variable assignment statement in the AST.
 * Example: x = 5 + 3;
 *
 * @author galart27
 * @author noam_wein
 */
public class VariableAssignment extends Statement {

	private final String identifier;
	private final Expression expression;

	/**
	 * Constructs a VariableAssignment with the given identifier and expression.
	 *
	 * @param identifier the name of the variable being assigned
	 * @param expression the expression whose value is assigned to the variable
	 */
	public VariableAssignment(String identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	/**
	 * Returns the identifier of the variable being assigned.
	 *
	 * @return the variable identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the expression whose value is assigned to the variable.
	 *
	 * @return the assigned expression
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * Accepts a visitor that implements the ASTVisitor interface.
	 *
	 * @param visitor the visitor to accept
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitVariableAssignment(this);
	}

	/**
	 * Returns a string representation of the variable assignment.
	 *
	 * @return a string in the format "identifier = expression"
	 */
	@Override
	public String print() {
		return identifier + " = " + expression.print();
	}
}
