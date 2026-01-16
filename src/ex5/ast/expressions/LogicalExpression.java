package ex5.ast.expressions;

import ex5.ast.ASTVisitor;
import ex5.lexer.TokenType;

/**
 * Logical expression node in the AST.
 * Represents logical operations like AND, OR.
 */
public class LogicalExpression extends Expression {
    private final Expression left;
    private final TokenType op;   // AND / OR
    private final Expression right;

    /**
     * Constructs a LogicalExpression with the given left and right expressions and operator.
     *
     * @param left  the left expression
     * @param op    the logical operator (AND / OR)
     * @param right the right expression
     */
    public LogicalExpression(Expression left, TokenType op, Expression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    /**
     * Returns the left expression.
     *
     * @return the left expression
     */
    public Expression getLeft() { return left; }
    /**
     * Returns the logical operator.
     *
     * @return the logical operator
     */
    public TokenType getOp() { return op; }
    /**
     * Returns the right expression.
     *
     * @return the right expression
     */
    public Expression getRight() { return right; }

    /**
     * Accepts a visitor to process this logical expression.
     * @param visitor the AST visitor
     * @param <R> the return type of the visitor
     * @return the result of the visitor's processing
     */
    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitLogicalExpression(this);
    }

    /**
     * Returns a string representation of the logical expression.
     *
     * @return the string representation
     */
    @Override
    public String print() {
        return left.print() + " " + op + " " + right.print();
    }
}
