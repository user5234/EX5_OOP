package ex5.ast.statements;

import java.util.List;

public class MethodDeclaration extends Statement {

	private final String identifier;
	private final List<MethodArgument> arguments;
	private final Block body;

	public MethodDeclaration(String identifier, List<MethodArgument> arguments, Block body) {
		this.identifier = identifier;
		this.arguments = arguments;
		this.body = body;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<MethodArgument> getArguments() {
		return arguments;
	}

	public Block getBody() {
		return body;
	}

	@Override
	public String print() {
		var args = new StringBuilder();
		for (int i = 0; i < arguments.size(); i++) {
			args.append(arguments.get(i).print());
			if (i < arguments.size() - 1) {
				args.append(", ");
			}
		}

		return "method " + identifier + "(" + args + ")\n" + "\t" + body.print();
	}
}
