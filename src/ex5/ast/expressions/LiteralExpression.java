package ex5.ast.expressions;

import ex5.lexer.Token;

public class LiteralExpression extends Expression {

	private final Token literal;

	public LiteralExpression(Token literal) {
		this.literal = literal;
	}

	public String getLiteral() {
		return literal.getValue();
	}

	@Override
	public String print() {
		return literal.getValue();
	}
}
