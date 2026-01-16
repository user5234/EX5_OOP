package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.lexer.TokenType;

/**
 * Represents a method argument in the AST.
 */
public class MethodArgument extends Statement {

	private final TokenType type;
	private final String identifier;

	/**
	 * Constructs a MethodArgument with the specified type and identifier.
	 *
	 * @param type       the type of the argument
	 * @param identifier the name of the argument
	 */
	public MethodArgument(TokenType type, String identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	/**
	 * Returns the type of the method argument.
	 *
	 * @return the type of the argument
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns the identifier of the method argument.
	 *
	 * @return the name of the argument
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Accepts a visitor to process this method argument.
	 * @param visitor the AST visitor
	 * @return the result of the visitor's processing
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitMethodArgument(this);
	}

	/**
	 * Returns a string representation of the method argument.
	 *
	 * @return a string in the format "type identifier"
	 */
	@Override
	public String print() {
		return type + " " + identifier;
	}
}
