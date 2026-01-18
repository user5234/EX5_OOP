package ex5.ast.statements;

import ex5.ast.ASTVisitor;

import java.util.List;

/**
 * Represents a method declaration statement in the AST.
 *
 * @author galart27
 * @author noam_wein
 */
public class MethodDeclaration extends Statement {

	private final String identifier;
	private final List<MethodArgument> arguments;
	private final Block body;

	/**
	 * Constructs a MethodDeclaration with the given identifier, arguments, and body.
	 *
	 * @param identifier The name of the method.
	 * @param arguments  The list of method arguments.
	 * @param body       The body of the method as a Block.
	 */
	public MethodDeclaration(String identifier, List<MethodArgument> arguments, Block body) {
		this.identifier = identifier;
		this.arguments = arguments;
		this.body = body;
	}

	/**
	 * Returns the identifier of the method.
	 *
	 * @return The method's name.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the list of method arguments.
	 *
	 * @return The method's arguments.
	 */
	public List<MethodArgument> getArguments() {
		return arguments;
	}

	/**
	 * Returns the body of the method.
	 *
	 * @return The method's body as a Block.
	 */
	public Block getBody() {
		return body;
	}

	/**
	 * Accepts a visitor to process this MethodDeclaration node.
	 *
	 * @param visitor The ASTVisitor to accept.
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitMethodDeclaration(this);
	}

	/**
	 * Returns a string representation of the method declaration.
	 *
	 * @return A formatted string representing the method declaration.
	 */
	@Override
	public String print() {
		var args = new StringBuilder();
		for (int i = 0; i < arguments.size(); i++) {
			args.append(arguments.get(i).print());
			if (i < arguments.size() - 1) {
				args.append(", ");
			}
		}

		return "method " + identifier + "(" + args + ")\n" + body.print().indent(4);
	}
}
