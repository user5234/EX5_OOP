package ex5.ast.expressions;

import ex5.ast.ASTNode;
import ex5.ast.ASTVisitor;

/**
 * An abstract class representing an expression in the AST.
 *
 * @author galart27
 * @author noam_wein
 */
public abstract class Expression extends ASTNode {

	/**
	 * Accepts a visitor that performs some operation on this expression.
	 *
	 * @param visitor the visitor to accept
	 * @param <R>     the return type of the visitor's operation
	 * @return the result of the visitor's operation
	 */
	public abstract <R> R accept(ASTVisitor<R> visitor);
}
