package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class Cos extends Command {
    private MathInCon inCon;
    public Cos(int line, String in, String out){
        super(line, CommandType.Cos,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Cos(int line, String var){
        super(line, CommandType.Cos,new String[]{var},var);
        inCon = MathInCon.I1;
    }
    @Override
    public void run(){
        switch (inCon){
            case I1O1 -> varManager.getVar(OutVarID).Cos(varManager.getVar(InVarIDs[0]));
            case I1 -> varManager.getVar(OutVarID).Cos();
        }
    }
}
