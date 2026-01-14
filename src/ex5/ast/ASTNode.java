package ex5.ast;

public abstract class ASTNode {

	public abstract String print();

	public abstract <R> R accept(ASTVisitor<R> visitor);
}
