package ex5.ast.expressions;

import ex5.ast.ASTVisitor;
import ex5.lexer.Token;

public class LiteralExpression extends Expression {

	private final Token literal;

	public LiteralExpression(Token literal) {
		this.literal = literal;
	}

	public Token getLiteral() {
		return literal;
	}

	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitLiteralExpression(this);
	}

	@Override
	public String print() {
		return literal.getValue();
	}
}
