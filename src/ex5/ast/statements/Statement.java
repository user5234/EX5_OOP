package ex5.ast.statements;

import ex5.ast.ASTNode;
import ex5.ast.ASTVisitor;

/**
 * An abstract class representing a statement in the AST.
 *
 * @author galart27
 * @author noam_wein
 */
public abstract class Statement extends ASTNode {

	/**
	 * Accepts a visitor that implements the ASTVisitor interface.
	 *
	 * @param visitor the visitor to accept
	 * @param <R>     the return type of the visitor's visit method
	 */
	public abstract <R> void accept(ASTVisitor<R> visitor);
}
