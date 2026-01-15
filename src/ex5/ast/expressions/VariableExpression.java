package ex5.ast.expressions;

import ex5.ast.ASTVisitor;

/**
 * A class representing a variable expression in the AST.
 */
public class VariableExpression extends Expression {

	private final String identifier;

	/**
	 * Constructs a VariableExpression with the given identifier.
	 * @param identifier the name of the variable
	 */
	public VariableExpression(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the name of the variable.
	 * @return the variable name
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Accepts a visitor that implements the ASTVisitor interface.
	 * @param visitor the visitor to accept
	 * @param <R> the return type of the visitor's visit method
	 * @return the result of the visitor's visit method
	 */
	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitVariableExpression(this);
	}

	/**
	 * Returns a string representation of the variable expression.
	 * @return the string representation of the variable
	 */
	@Override
	public String print() {
		return identifier;
	}
}
