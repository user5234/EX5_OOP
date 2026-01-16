package ex5.ast.statements;

import ex5.ast.ASTVisitor;
import ex5.lexer.TokenType;
import ex5.ast.expressions.Expression;

/**
 * Represents a variable declaration statement in the AST.
 */
public class VariableDeclaration extends Statement {

    private final TokenType type;
    private final String identifier;
    private final Expression initializer; // may be null
    private final boolean isFinal;

    /**
     * Constructs a VariableDeclaration node.
     *
     * @param type        The data type of the variable.
     * @param identifier  The name of the variable.
     * @param initializer The expression assigned to the variable (may be null).
     * @param isFinal     Indicates if the variable is declared as final.
     */
    public VariableDeclaration(TokenType type, String identifier, Expression initializer, boolean isFinal) {
        this.type = type;
        this.identifier = identifier;
        this.initializer = initializer;
        this.isFinal = isFinal;
    }

    /**
     * Gets the data type of the variable.
     * @return The variable's data type.
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Gets the name of the variable.
     * @return The variable's identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the initializer expression of the variable.
     * @return The initializer expression, or null if none.
     */
    public Expression getInitializer() {
        return initializer;
    }

    /**
     * Checks if the variable is declared as final.
     * @return True if the variable is final, false otherwise.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Accepts a visitor that implements the ASTVisitor interface.
     * @param visitor the visitor to accept
     * @return the result of the visitor's operation
     */
    @Override
    public <R> void accept(ASTVisitor<R> visitor) {
        visitor.visitVariableDeclaration(this);
    }

    /**
     * Returns a string representation of the variable declaration.
     * @return A string representing the variable declaration.
     */
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
