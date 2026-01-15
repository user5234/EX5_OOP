package ex5.ast.expressions;

import ex5.ast.ASTVisitor;

import java.util.List;

/**
 * A class representing a method call expression in the AST.
 */
public class MethodCall extends Expression {

	private final String identifier;
	private final List<Expression> arguments;

	/**
	 * Constructs a MethodCall with the given method name and arguments.
	 * @param methodName the name of the method being called
	 * @param arguments the list of argument expressions
	 */
	public MethodCall(String methodName, List<Expression> arguments) {
		this.identifier = methodName;
		this.arguments = arguments;
	}

	/**
	 * Returns the name of the method being called.
	 * @return the method name
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the list of argument expressions.
	 * @return the argument expressions
	 */
	public List<Expression> getArguments() {
		return arguments;
	}

	/**
	 * Accepts a visitor that implements the ASTVisitor interface.
	 * @param visitor the visitor to accept
	 * @param <R> the return type of the visitor's visit method
	 * @return the result of the visitor's visit method
	 */
	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitMethodCall(this);
	}

	/**
	 * Returns a string representation of the method call.
	 * @return the string representation of the method call
	 */
	@Override
	public String print() {
		StringBuilder argsPrint = new StringBuilder();
		for (int i = 0; i < arguments.size(); i++) {
			argsPrint.append(arguments.get(i).print());
			if (i < arguments.size() - 1) {
				argsPrint.append(", ");
			}
		}
		return identifier + "(" + argsPrint + ")";
	}
}
