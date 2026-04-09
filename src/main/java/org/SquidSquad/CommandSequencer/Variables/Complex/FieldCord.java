package org.SquidSquad.CommandSequencer.Variables.Complex;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class FieldCord extends Variable {
    public FieldCord(double[] coord){
        super(VariableTypes.FieldCord, coord);
    }
}
