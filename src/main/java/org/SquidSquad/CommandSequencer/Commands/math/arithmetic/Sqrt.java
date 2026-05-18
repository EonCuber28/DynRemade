package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;

public class Sqrt extends Command {
    private MathInCon inCon;
    public Sqrt(int line, String in, String out){
        super(line, CommandType.Sqrt,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Sqrt(int line, String var){
        super(line, CommandType.Sqrt, new String[]{var},var);
        inCon = MathInCon.I1;
    }
    @Override
    public void run(){
        switch (inCon){
            case I1O1 -> varManager.getVar(OutVarID).Sqrt(varManager.getVar(InVarIDs[0]));
            case I1 -> varManager.getVar(OutVarID).Sqrt();
        }
    }
}
