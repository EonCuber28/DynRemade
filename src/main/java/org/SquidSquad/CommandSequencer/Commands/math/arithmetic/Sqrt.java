package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Commands.math.MathInCon;
import org.SquidSquad.CommandSequencer.variables.Variable;
import org.SquidSquad.CommandSequencer.variables.VariableTypes;
import org.SquidSquad.CommandSequencer.variables.primitives.DynNumber;

public class Sqrt extends Command {
    private final MathInCon inCon;
    private boolean inIsNum = false;
    private double inNum;
    public Sqrt(int line, String in, String out){
        super(line, CommandType.Sqrt,new String[]{in},out);
        inCon = MathInCon.I1O1;
    }
    public Sqrt(int line, String var){
        super(line, CommandType.Sqrt, new String[]{var},var);
        inCon = MathInCon.I1;
    }

    public Sqrt(int line, double in, String out) {
        super(line, CommandType.Sqrt, new String[]{out}, String.valueOf(in));
        inCon = MathInCon.I1O1;
        inIsNum = true;
        inNum = in;
    }

    @Override
    public void run(){
        super.run();
        if (inIsNum){
            // https://media.tenor.com/q6DLf3ymgNAAAAAe/chrollo-chrollo-crying.png
            varManager.getVar(OutVarID).Sqrt(new DynNumber(inNum));
        } else {
            switch (inCon) {
                case I1O1 -> varManager.getVar(OutVarID).Sqrt(varManager.getVar(InVarIDs[0]));
                case I1 -> varManager.getVar(OutVarID).Sqrt();
            }
        }
    }
}
