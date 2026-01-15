package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.lexer.TokenType;
import ex5.ast.expressions.Expression;

public class VariableDeclaration extends Statement {

    private final TokenType type;
    private final String identifier;
    private final Expression initializer; // may be null
    private final boolean isFinal;

    public VariableDeclaration(TokenType type, String identifier, Expression initializer, boolean isFinal) {
        this.type = type;
        this.identifier = identifier;
        this.initializer = initializer;
        this.isFinal = isFinal;
    }

    public TokenType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getInitializer() {
        return initializer;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public <R> void accept(ASTVisitor<R> visitor) {
        visitor.visitVariableDeclaration(this);
    }

    @Override
    public String print() {
        String f = isFinal ? "final " : "";
        if (initializer != null) {
            return f + type + " " + identifier + " = " + initializer.print();
        } else {
            return f + type + " " + identifier;
        }
    }
}
