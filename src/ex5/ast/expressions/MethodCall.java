package ex5.ast.expressions;

import ex5.ast.ASTVisitor;

import java.util.List;

public class MethodCall extends Expression {

	private final String identifier;
	private final List<Expression> arguments;

	public MethodCall(String methodName, List<Expression> arguments) {
		this.identifier = methodName;
		this.arguments = arguments;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<Expression> getArguments() {
		return arguments;
	}

	@Override
	public <R> R accept(ASTVisitor<R> visitor) {
		return visitor.visitMethodCall(this);
	}

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
