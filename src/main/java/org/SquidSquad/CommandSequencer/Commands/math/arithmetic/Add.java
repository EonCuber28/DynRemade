package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class Add extends Command {
    private MathInCon varCon;
    public Add(int line, String in1, String in2, String out){
        super(line, CommandType.Add,new String[]{in1,in2},out);
        varCon = MathInCon.I2O1;
    }
    public Add(int line, String in1, String in2){
        super(line, CommandType.Add,new String[]{in1,in2},in1);
        varCon = MathInCon.I2;
    }

    @Override
    public void run(){
        switch (varCon){
            case I2O1 -> varManager.getVar(OutVarID).Add(
                    varManager.getVar(InVarIDs[0]),
                    varManager.getVar(InVarIDs[1]));
            case I2 -> varManager.getVar(OutVarID).Add(
                    varManager.getVar(InVarIDs[1]));
        }
    }
}
