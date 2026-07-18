package org.SquidSquad.CommandSequencer;

import org.SquidSquad.CommandSequencer.variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableManager {
    private final ArrayList<Variable> allVars = new ArrayList<>(); // this is dedicated to containing all created literals.
    private final ArrayList<String> registeredIDs = new ArrayList<>();
    private final Map<String, Variable> VarIdMap = new HashMap<>();

    public VariableManager(){
        Variable.registerVarSettersGetters(this::setVar, this::registerVar);
    }

    private void setVar(Variable target, Variable value){
        // TODO: there is a better version of this out there
        int targetIndex = allVars.indexOf(target);
        allVars.set(targetIndex,value);
    }

    public Variable getVar(String ID){
        if (registeredIDs.contains(ID)) return VarIdMap.get(ID);
        return null;
    }
    public void registerVar(Variable var){
        if (!allVars.contains(var)) {
            if (var.isLiteral()) {
                registeredIDs.add(var.getName());
                VarIdMap.put(var.getName(), var);
            }
            allVars.add(var);
        }
    }

    public String toString(){
        // TODO: make this for debugging, and make it dump the entire ID -> var (as their toString) map.
        return "No ToString defined. MemAddr: " + Integer.toHexString(System.identityHashCode(this));
    }
}
