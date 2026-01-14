package ex5.ast.statements;

import ex5.lexer.TokenType;
import ex5.ast.expressions.Expression;

public class VariableDeclaration extends Statement {

	public final TokenType type;
	public final String name;
	public final Expression initializer;

	public VariableDeclaration(TokenType type, String name, Expression initializer) {
		this.type = type;
		this.name = name;
		this.initializer = initializer;
	}
}
