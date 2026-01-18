package ex5.ast;

import ex5.ast.expressions.LiteralExpression;
import ex5.ast.expressions.LogicalExpression;
import ex5.ast.statements.MethodCall;
import ex5.ast.expressions.VariableExpression;
import ex5.ast.statements.*;

/**
 * An interface for visiting AST nodes.
 * @param <R> the return type of the visit methods
 */
public interface ASTVisitor<R> {

	// Statements
	void visitBlock(Block bl);
	void visitIfStatement(IfStatement is);
	void visitMethodArgument(MethodArgument ma);
	void visitMethodCall(MethodCall mc);
	void visitMethodDeclaration(MethodDeclaration md);
	void visitReturnStatement(ReturnStatement rs);
	void visitVariableAssignment(VariableAssignment va);
	void visitVariableDeclaration(VariableDeclaration vd);
	void visitWhileStatement(WhileStatement ws);
	void visitMethodCallStatement(MethodCallStatement mcs);

	// Expressions
	R visitLiteralExpression(LiteralExpression le);
	R visitVariableExpression(VariableExpression ve);
	R visitLogicalExpression(LogicalExpression le);
}
