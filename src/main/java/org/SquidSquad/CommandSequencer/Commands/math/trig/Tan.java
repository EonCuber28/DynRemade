package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;
import org.SquidSquad.CommandSequencer.variables.primitives.DynNumber;

public class Tan extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public Tan(int line, String in, String out){
        super(line, CommandType.Tan,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Tan(int line, String var){
        super(line, CommandType.Tan,new String[]{var},var);
        inCon = MathInCon.I1;
    }
    public Tan(int line, double in, String out){
        super(line, CommandType.Tan,new String[]{String.valueOf(in)},out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum) {
            varManager.getVar(OutVarID).Tan(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> varManager.getVar(OutVarID).Tan(varManager.getVar(InVarIDs[0]));
                case I1 -> varManager.getVar(OutVarID).Tan();
            }
        }
    }
}
