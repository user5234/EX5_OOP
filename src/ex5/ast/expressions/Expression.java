package ex5.ast.expressions;

import ex5.ast.ASTNode;
import ex5.ast.ASTVisitor;

public abstract class Expression extends ASTNode {

	public abstract <R> R accept(ASTVisitor<R> visitor);
}
