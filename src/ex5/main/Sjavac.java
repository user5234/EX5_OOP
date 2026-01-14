package ex5.main;

import ex5.ast.ASTNode;
import ex5.ast.statements.*;
import ex5.lexer.Lexer;
import ex5.lexer.Token;
import ex5.lexer.UnknownTokenException;
import ex5.parser.Parser;
import ex5.parser.UnexpectedTokenException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Sjavac {

	public static void main(String[] args) {
		try(var fileReader = new BufferedReader(new FileReader(args[0]))) {
			Lexer lexer = new Lexer();
			var tokens = new ArrayList<Token>();

			var line = "";
			while ((line = fileReader.readLine()) != null) {
				if (line.startsWith("//")) continue;
				tokens.addAll(lexer.tokenize(line));
			}

			Parser parser = new Parser(tokens);

			var ast = parser.parseProgram();

			for (ASTNode node : ast) {
				System.out.println(node.print());
			}
		} catch (IOException | UnexpectedTokenException | UnknownTokenException e) {
			throw new RuntimeException(e);
		}
	}
}
