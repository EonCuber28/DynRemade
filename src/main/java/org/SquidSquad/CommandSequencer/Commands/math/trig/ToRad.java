package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class ToRad extends Command {
    private final MathInCon inCon;
    public ToRad(int line, String in, String out){
        super(line, CommandType.toRad, new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public ToRad(int line, String var){
        super(line, CommandType.toRad, new String[]{var},var);
        inCon = MathInCon.I1;
    }
    @Override
    public void run(){
        super.run();
        switch(inCon){
            case I1O1 -> varManager.getVar(OutVarID).toRad(varManager.getVar(InVarIDs[0]));
            case I1 -> varManager.getVar(OutVarID).toRad();
        }
    }
}
