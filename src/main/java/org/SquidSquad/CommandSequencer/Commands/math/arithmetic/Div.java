package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class Div extends Command {
    private MathInCon varCon;
    public Div(int line, String in1, String in2, String out){
        super(line, CommandType.Div,new String[]{in1,in2},out);
        varCon = MathInCon.I2O1;
    }
    public Div(int line, String in1, String in2){
        super(line, CommandType.Div,new String[]{in1,in2},in1);
        varCon = MathInCon.I2;
    }

    @Override
    public void run(){
        switch (varCon){
            case I2O1 -> varManager.getVar(getOutVarID()).Div(varManager.getVar(InVarIDs[0]), varManager.getVar(InVarIDs[1]));
            case I2 -> varManager.getVar(getOutVarID()).Div(varManager.getVar(InVarIDs[0]));
        }
    }
}
