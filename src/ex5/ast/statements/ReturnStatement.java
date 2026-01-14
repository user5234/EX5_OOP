package ex5.ast.statements;

import ex5.ast.ASTVisitor;

public class ReturnStatement extends Statement {

	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitReturnStatement(this);
	}

	@Override
	public String print() {
		return "return";
	}
}
