package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngFloat extends Command {
    public RngFloat(int line, String var){
        super(line, CommandType.RngFloat, var);
    }
    @Override
    public void run(){
        float value = (float)(Math.random()*2.0 - 1.0);
        varManager.getVar(OutVarID).setValue(value);
    }
}
