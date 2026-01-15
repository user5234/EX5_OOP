package ex5.main;

import ex5.ast.statements.*;
import ex5.lexer.Lexer;
import ex5.lexer.Token;
import ex5.lexer.UnknownTokenException;
import ex5.parser.Parser;
import ex5.parser.UnexpectedTokenException;
import ex5.semantic.SemanticAnalyzer;
import ex5.semantic.SemanticException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Sjavac {

	public static void main(String[] args) {
		if (args.length != 1) {
    	   System.err.println("Usage: Sjavac <file.sjava>");
    	   System.out.println(2);
    	   return;
    	}
		
		try (var fileReader = new BufferedReader(new FileReader(args[0]))) {
			var lexer = new Lexer();
			var tokens = new ArrayList<Token>();

			var line = "";
			while ((line = fileReader.readLine()) != null) {
				if (line.startsWith("//")) continue;
				tokens.addAll(lexer.tokenize(line));
			}

			var statements = new Parser(tokens).parseProgram();

			for (var s : statements) {
				System.out.println(s.print());
			}

			new SemanticAnalyzer().analyze(statements);

			System.out.println(0);
		} catch (UnexpectedTokenException | UnknownTokenException | SemanticException e) {
			System.err.println(e.getMessage());
			System.out.println(1);
		} catch (IOException e) {
			System.err.println("IO Error: " + e.getMessage());
			System.out.println(2);
		}
	}
}
