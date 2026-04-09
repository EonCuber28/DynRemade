package org.SquidSquad.CommandSequencer.Variables.Primitives;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class String extends Variable {
    public String(String value){
        super(VariableTypes.String, value);
    }
}
