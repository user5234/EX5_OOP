package ex5.logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A table for storing variable information, their scope, types and modifiers.
 */
public class VariableTable {
    private Map<String, Variable> variables;

    public VariableTable() {
        this.variables = new HashMap<>();
    }

    public void addVariable(String name, Variable variable) {
        variables.put(name, variable);
    }

    public Variable getVariable(String name) {
        return variables.get(name);
    }

    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    public Collection<Variable> getAllVariables() {
        return variables.values();
    }

    public boolean isEmpty() {
        return variables.isEmpty();
    }

    public void clear() {
        variables.clear();
    }
}
