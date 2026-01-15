package ex5.ast.expressions;

import ex5.ast.ASTVisitor;
import ex5.lexer.TokenType;

public class LogicalExpression extends Expression {
    private final Expression left;
    private final TokenType op;   // AND / OR
    private final Expression right;

    public LogicalExpression(Expression left, TokenType op, Expression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public Expression getLeft() { return left; }
    public TokenType getOp() { return op; }
    public Expression getRight() { return right; }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitLogicalExpression(this);
    }

    @Override
    public String print() {
        return left.print() + " " + op + " " + right.print();
    }
}
