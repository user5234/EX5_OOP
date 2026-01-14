package ex5.ast.statements;

import ex5.ast.ASTVisitor;

import java.util.List;

public class Block extends Statement {

	private final List<Statement> statements;

	public Block(List<Statement> statements) {
		this.statements = statements;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitBlock(this);
	}

	@Override
	public String print() {
		var sb = new StringBuilder();
		for (Statement statement : statements) {
			sb.append(statement.print()).append("\n");
		}
		return sb.toString();
	}
}
