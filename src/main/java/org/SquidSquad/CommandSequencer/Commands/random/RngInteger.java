package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngInteger extends Command {
    int min;
    int max;
    public RngInteger(int line, double min, double max, String var){
        super(line, CommandType.RngInteger, var);
        int newMin = (int)min;
        int newMax = (int)max;
        this.min = newMin;
        this.max = newMax;
    }
    @Override
    public void run(){
        int value = (int)(Math.random()*(max-min+1))+min;
        varManager.getVar(OutVarID).setValue(value);
    }
}
