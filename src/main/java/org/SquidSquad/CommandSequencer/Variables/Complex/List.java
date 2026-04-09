package org.SquidSquad.CommandSequencer.Variables.Complex;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

import java.util.ArrayList;

public class List extends Variable {
    public List(ArrayList<Variable> leList){
        super(VariableTypes.List, leList);
    }
}
