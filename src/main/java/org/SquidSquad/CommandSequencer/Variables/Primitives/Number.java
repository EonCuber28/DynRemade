package org.SquidSquad.CommandSequencer.Variables.Primitives;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class Number extends Variable {
    public Number(double value){
        super(VariableTypes.Number,value);
    }
}
