package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngBoolean extends Command {
    public RngBoolean(int line, String out){
        super(line, CommandType.RngBoolean,out);
    }
    @Override
    public void run(){
        int decision = (int)(Math.random()*2);
        if (decision == 0) varManager.getVar(OutVarID).setValue(false);
        else varManager.getVar(OutVarID).setValue(true);
    }
}
