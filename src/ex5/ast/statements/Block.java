package ex5.ast.statements;

import java.util.List;

public class Block extends Statement {
	public final List<Statement> statements;

	public Block(List<Statement> statements) {
		this.statements = statements;
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
