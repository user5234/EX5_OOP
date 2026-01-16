package ex5.ast.statements;

import ex5.ast.ASTVisitor;

import java.util.List;

/**
 * A block of statements.
 */
public class Block extends Statement {

	private final List<Statement> statements;

	/**
	 * Constructs a Block with the given list of statements.
	 *
	 * @param statements the list of statements in the block
	 */
	public Block(List<Statement> statements) {
		this.statements = statements;
	}

	/**
	 * Returns the list of statements in the block.
	 *
	 * @return the list of statements
	 */
	public List<Statement> getStatements() {
		return statements;
	}

	/**
	 * Accepts a visitor to process this block.
	 * @param visitor the AST visitor
	 * @return the result of the visitor's processing
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitBlock(this);
	}

	/**
	 * Returns a string representation of the block.
	 *
	 * @return the string representation
	 */
	@Override
	public String print() {
		var sb = new StringBuilder();
		for (Statement statement : statements) {
			sb.append(statement.print()).append("\n");
		}
		return sb.toString();
	}
}
