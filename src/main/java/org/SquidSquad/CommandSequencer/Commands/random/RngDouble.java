package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.variables.Variable;
import org.SquidSquad.CommandSequencer.variables.VariableTypes;

public class RngDouble extends Command {
    private double min;
    private double max;
    private String MIN = null;
    private String MAX = null;
    public RngDouble(int line, double min, double max, String var){
        super(line, CommandType.RngDouble,new String[]{String.valueOf(min),String.valueOf(max)},var);
        this.min = min;
        this.max = max;
    }
    public RngDouble(int line, double min, String max, String var){
        super(line, CommandType.RngDouble, new String[]{String.valueOf(min),max},var);
        this.min = min;
        MAX = max;
    }
    public RngDouble(int line, String min, double max, String var){
        super(line, CommandType.RngDouble, new String[]{min,String.valueOf(max)},var);
        MIN = min;
        this.max = max;
    }
    public RngDouble(int line, String min, String max, String var){
        super(line, CommandType.RngDouble, new String[]{min,max},var);
        MIN = min;
        MAX = max;
    }

    @Override
    public void run(){
        super.run();
        if (MIN != null){
            Variable Min = varManager.getVar(MIN);
            if (Min.getType() == VariableTypes.Number) min = (int)Min.getValue();
            else throw new CommandException(line,"RngInteger","Expected number variable, got: "+Min.getType());
        }
        if (MAX != null){
            Variable Max = varManager.getVar(MAX);
            if (Max.getType() == VariableTypes.Number) min = (int)Max.getValue();
            else throw new CommandException(line,"RngInteger","Expected number variable, got: "+Max.getType());
        }
        double value = min+Math.random()*(max-min);
        varManager.getVar(OutVarID).setValue(value);
    }
}
