package ex5.ast.statements;

import ex5.ast.ASTVisitor;

/**
 * Represents a return statement in the AST.
 *
 * @author galart27
 * @author noam_wein
 */
public class ReturnStatement extends Statement {

	/**
	 * Accepts a visitor that processes this return statement.
	 *
	 * @param visitor the AST visitor
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitReturnStatement(this);
	}

	/**
	 * Returns a string representation of the return statement.
	 *
	 * @return the string "return"
	 */
	@Override
	public String print() {
		return "return";
	}
}
