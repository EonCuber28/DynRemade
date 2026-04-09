package org.SquidSquad.CommandSequencer.Variables.Primitives;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class Boolean extends Variable {
    public Boolean(boolean state){
        super(VariableTypes.Boolean,state);
    }
}
