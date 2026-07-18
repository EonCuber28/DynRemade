package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.variables.Variable;
import org.SquidSquad.CommandSequencer.variables.VariableTypes;

public class RngInteger extends Command {
    int min;
    int max;
    String MIN;
    String MAX;
    public RngInteger(int line, double min, double max, String var){
        super(line, CommandType.RngInteger, var);
        int newMin = (int)min;
        int newMax = (int)max;
        this.min = newMin;
        this.max = newMax;
    }
    public RngInteger(int line, double min, String max, String var){
        super(line, CommandType.RngInteger, var);
        MAX = max;
    }
    public RngInteger(int line, String min, double max, String var){
        super(line, CommandType.RngInteger, var);
        MIN = min;
    }
    public RngInteger(int line, String min, String max, String var){
        super(line, CommandType.RngInteger, var);
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
        int value = (int)(Math.random()*(max-min+1))+min;
        varManager.getVar(OutVarID).setValue(value);
    }
}
