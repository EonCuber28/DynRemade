package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class Tan extends Command {
    private MathInCon inCon;
    public Tan(int line, String in, String out){
        super(line, CommandType.Tan,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Tan(int line, String var){
        super(line, CommandType.Tan,new String[]{var},var);
        inCon = MathInCon.I1;
    }
    @Override
    public void run(){
        switch (inCon){
            case I1O1 -> varManager.getVar(OutVarID).Tan(varManager.getVar(InVarIDs[0]));
            case I1 -> varManager.getVar(OutVarID).Tan();
        }
    }
}
