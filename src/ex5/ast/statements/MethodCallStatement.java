package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.ast.expressions.MethodCall;

public class MethodCallStatement extends Statement {
    private final MethodCall call;

    public MethodCallStatement(MethodCall call) {
        this.call = call;
    }

    public MethodCall getCall() {
        return call;
    }

    @Override
    public <R> void accept(ASTVisitor<R> visitor) {
        visitor.visitMethodCallStatement(this);
    }

    @Override
    public String print() {
        return call.print();
    }
}
