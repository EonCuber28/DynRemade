package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class iCos extends Command {
    private MathInCon inCon;
    public iCos(int line, String in, String out){
        super(line, CommandType.iCos,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public iCos(int line, String in){
        super(line, CommandType.iCos, new String[]{in},in);
        inCon = MathInCon.I1;
    }
    @Override
    public void run(){
        switch (inCon){
            case I1O1 -> varManager.getVar(OutVarID).iCos(varManager.getVar(InVarIDs[0]));
            case I1 -> varManager.getVar(OutVarID).iCos();
        }
    }
}
