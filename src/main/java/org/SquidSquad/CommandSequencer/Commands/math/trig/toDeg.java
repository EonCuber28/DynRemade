package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class toDeg extends Command {
    private MathInCon inCon;
    public toDeg(int line, String in, String out){
        super(line, CommandType.toDeg,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public toDeg(int line, String var){
        super(line,CommandType.toDeg,new String[]{var},var);
        inCon = MathInCon.I1;
    }
    @Override
    public void run(){
        switch (inCon){
            case I1O1 -> varManager.getVar(OutVarID).toDeg(varManager.getVar(InVarIDs[0]));
            case I1 -> varManager.getVar(OutVarID).toDeg();
        }
    }
}
