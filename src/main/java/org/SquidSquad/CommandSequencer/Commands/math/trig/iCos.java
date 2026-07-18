package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;
import org.SquidSquad.CommandSequencer.variables.primitives.DynNumber;

public class iCos extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public iCos(int line, String in, String out){
        super(line, CommandType.iCos,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public iCos(int line, String in){
        super(line, CommandType.iCos, new String[]{in},in);
        inCon = MathInCon.I1;
    }
    public iCos(int line, double in, String out){
        super(line, CommandType.iCos, new String[]{String.valueOf(in)},out);
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum){
            varManager.getVar(OutVarID).iCos(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> varManager.getVar(OutVarID).iCos(varManager.getVar(InVarIDs[0]));
                case I1 -> varManager.getVar(OutVarID).iCos();
            }
        }
    }
}
