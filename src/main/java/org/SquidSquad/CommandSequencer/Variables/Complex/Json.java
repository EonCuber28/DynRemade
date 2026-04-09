package org.SquidSquad.CommandSequencer.Variables.Complex;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

import java.util.Map;

public class Json extends Variable {
    public Json(Map<Variable,Variable> leJson){
        super(VariableTypes.Json, leJson);
    }
}
