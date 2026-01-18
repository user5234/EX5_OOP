package ex5.ast.expressions;

import ex5.ast.ASTVisitor;
import ex5.lexer.Token;

/**
 * A class representing a literal expression in the AST.
 *
 * @author galart27
 * @author noam_wein
 */
public class LiteralExpression extends Expression {

	private final Token literal;

	/**
	 * Constructs a LiteralExpression with the given token.
	 *
	 * @param literal the token representing the literal
	 */
	public LiteralExpression(Token literal) {
		this.literal = literal;
	}

	/**
	 * Returns the token representing the literal.
	 *
	 * @return the literal token
	 */
	public Token getLiteral() {
		return literal;
	}

	/**
	 * Accepts a visitor that implements the ASTVisitor interface.
	 *
	 * @param visitor the visitor to accept
	 * @param <R>     the return type of the visitor's visit method
	 * @return the result of the visitor's visit method
	 */
	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitLiteralExpression(this);
	}

	/**
	 * Returns a string representation of the literal expression.
	 *
	 * @return the string representation of the literal
	 */
	@Override
	public String print() {
		return literal.getValue();
	}
}
