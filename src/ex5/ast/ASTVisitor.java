package ex5.ast;

import ex5.ast.expressions.LiteralExpression;
import ex5.ast.expressions.MethodCall;
import ex5.ast.expressions.VariableExpression;
import ex5.ast.statements.*;

public interface ASTVisitor<R> {

	// Statements
	R visitBlock(Block bl);
	R visitIfStatement(IfStatement is);
	R visitMethodArgument(MethodArgument ma);
	R visitMethodDeclaration(MethodDeclaration md);
	R visitReturnStatement(ReturnStatement rs);
	R visitVariableAssignment(VariableAssignment va);
	R visitVariableDeclaration(VariableDeclaration vd);
	R visitWhileStatement(WhileStatement ws);

	// Expressions
	R visitLiteralExpression(LiteralExpression le);
	R visitVariableExpression(VariableExpression ve);
	R visitMethodCall(MethodCall mc);
}
