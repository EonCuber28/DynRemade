package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngDouble extends Command {
    private double min;
    private double max;
    public RngDouble(int line, double min, double max, String var){
        super(line, CommandType.RngDouble,new String[]{((Double)min).toString(),((Double)max).toString()},var);
        this.min = min;
        this.max = max;
    }
    @Override
    public void run(){
        double value = min+Math.random()*(max-min);
        varManager.getVar(OutVarID).setValue(value);
    }
}
