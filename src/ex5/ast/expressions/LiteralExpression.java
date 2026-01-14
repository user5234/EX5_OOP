package ex5.ast.expressions;

import ex5.lexer.Token;

public class LiteralExpression extends Expression {

	public final Token literal;

	public LiteralExpression(Token literal) {
		this.literal = literal;
	}
}
