package ex5.ast.statements;

import ex5.ast.ASTNode;
import ex5.ast.ASTVisitor;

public abstract class Statement extends ASTNode {

	public abstract <R> void accept(ASTVisitor<R> visitor);
}
