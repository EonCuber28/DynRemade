package org.SquidSquad.CommandSequencer;

import org.SquidSquad.CommandSequencer.Variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableManager {
    private final Map<String, Variable> VarIdMap = new HashMap<>();
    private final ArrayList<String> registeredIDs = new ArrayList<>();

    public Variable getVar(String ID){
        if (registeredIDs.contains(ID)) return VarIdMap.get(ID);
        return null;
    }
    public void addVar(String ID, Variable value){
        if (!registeredIDs.contains(ID)) {
            VarIdMap.put(ID, value);
            registeredIDs.add(ID);
        }
    }
    public String toString(){return null;}
}
