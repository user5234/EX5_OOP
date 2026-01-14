package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.lexer.TokenType;
import ex5.ast.expressions.Expression;

public class VariableDeclaration extends Statement {

	private final TokenType type;
	private final String identifier;
	private final Expression initializer;

	public VariableDeclaration(TokenType type, String identifier, Expression initializer) {
		this.type = type;
		this.identifier = identifier;
		this.initializer = initializer;
	}

	public  TokenType getType() {
		return type;
	}
	public String getIdentifier() {
		return identifier;
	}

	public Expression getInitializer() {
		return initializer;
	}

	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitVariableDeclaration(this);
	}

	@Override
	public String print() {
		if (initializer != null) {
			return type + " " + identifier + " = " + initializer.print();
		}
		else {
			return type + " " + identifier;
		}
	}
}
