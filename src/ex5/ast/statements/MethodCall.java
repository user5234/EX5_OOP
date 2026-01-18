package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.ast.expressions.Expression;

import java.util.List;

/**
 * A class representing a method call expression in the AST.
 */
public class MethodCall extends Statement {

	private final String identifier;
	private final List<Expression> arguments;

	/**
	 * Constructs a MethodCall with the given method name and arguments.
	 *
	 * @param methodName the name of the method being called
	 * @param arguments  the list of argument expressions
	 */
	public MethodCall(String methodName, List<Expression> arguments) {
		this.identifier = methodName;
		this.arguments = arguments;
	}

	/**
	 * Returns the name of the method being called.
	 *
	 * @return the method name
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the list of argument expressions.
	 *
	 * @return the argument expressions
	 */
	public List<Expression> getArguments() {
		return arguments;
	}

	/**
	 * Accepts a visitor that implements the ASTVisitor interface.
	 *
	 * @param visitor the visitor to accept
	 * @param <R>     the return type of the visitor's visit method
	 */
	@Override
	public <R> void accept(ASTVisitor<R> visitor) {
		visitor.visitMethodCall(this);
	}

	/**
	 * Returns a string representation of the method call.
	 *
	 * @return the string representation of the method call
	 */
	@Override
	public String print() {
		var argsPrint = new StringBuilder();
		for (int i = 0; i < arguments.size(); i++) {
			argsPrint.append(arguments.get(i).print());
			if (i < arguments.size() - 1) {
				argsPrint.append(", ");
			}
		}
		return identifier + "(" + argsPrint + ")";
	}
}
