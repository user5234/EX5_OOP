package ex5.ast.expressions;

public class VariableExpression extends Expression {

	public final String varName;

	public VariableExpression(String varName) {
		this.varName = varName;
	}
}
