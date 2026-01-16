package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.ast.expressions.MethodCall;

/**
 * Represents a method call statement in the AST.
 */
public class MethodCallStatement extends Statement {
    private final MethodCall call;

    /**
     * Constructs a MethodCallStatement with the given method call.
     *
     * @param call the method call expression
     */
    public MethodCallStatement(MethodCall call) {
        this.call = call;
    }

    /**
     * Returns the method call expression.
     *
     * @return the method call
     */
    public MethodCall getCall() {
        return call;
    }

    /**
     * Accepts a visitor to process this method call statement.
     * @param visitor the AST visitor
     * @return the result of the visitor's processing
     */
    @Override
    public <R> void accept(ASTVisitor<R> visitor) {
        visitor.visitMethodCallStatement(this);
    }

    /**
     * Returns a string representation of the method call statement.
     *
     * @return the string representation
     */
    @Override
    public String print() {
        return call.print();
    }
}
