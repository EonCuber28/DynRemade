package org.SquidSquad.CommandSequencer.Variables.Complex;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class FieldPos extends Variable {
    public FieldPos(double[] pose){
        super(VariableTypes.FieldPos,pose);
    }
}
