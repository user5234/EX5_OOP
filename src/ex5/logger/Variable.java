package ex5.logger;

/**
 * A class representing a variable with its properties.
 */
public class Variable {
    private String name;
    private String type;
    private String scope; //This will be a string representing the functions the variable is in example: "main.innerFunc"
    private boolean isFinal;

    public Variable(String name, String type, String scope, boolean isFinal) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
