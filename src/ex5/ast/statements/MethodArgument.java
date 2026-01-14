package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.lexer.TokenType;

public class MethodArgument extends Statement {

	private final TokenType type;
	private final String identifier;

	public MethodArgument(TokenType type, String identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	public TokenType getType() {
		return type;
	}

	public String getIdentifier() {
		return identifier;
	}

	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitMethodArgument(this);
	}

	@Override
	public String print() {
		return type + " " + identifier;
	}
}
