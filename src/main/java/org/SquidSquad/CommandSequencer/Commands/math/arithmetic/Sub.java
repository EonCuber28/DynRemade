package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class Sub extends Command {
    private MathInCon inCon;
    public Sub(int line, String in1, String in2, String out){
        super(line, CommandType.Sub,new String[]{in1,in2},out);
        inCon = MathInCon.I2O1;
    }
    public Sub(int line, String in1, String in2){
        super(line, CommandType.Sub,new String[]{in1,in2},in1);
        inCon = MathInCon.I2;
    }
    @Override
    public void run(){
        switch(inCon){
            case I2O1 -> varManager.getVar(OutVarID).Sub(
                    varManager.getVar(InVarIDs[0]),
                    varManager.getVar(InVarIDs[1]));
            case I2 -> varManager.getVar(OutVarID).Sub(
                    varManager.getVar(InVarIDs[1]));
        }
    }
}
