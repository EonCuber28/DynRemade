package org.SquidSquad.CommandSequencer.Commands.movement;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.variables.VariableTypes;

public class TurnTo extends Command {
    private final boolean Literal;
    private double angle;
    public TurnTo(int line, String var){
        super(line, CommandType.TurnTo, new String[]{var});
        Literal = false;
    }
    public TurnTo(int line, double value){
        super(line, CommandType.TurnTo, new String[]{String.valueOf(value)});
        Literal = true;
        angle = value;
    }

    @Override
    public void run(){
        super.run();
        if (Literal){
            turnTo.accept(angle);
        } else {
            if (varManager.getVar(InVarIDs[0]).getType() == VariableTypes.Number){
                turnTo.accept((double)varManager.getVar(InVarIDs[0]).getValue());
            } else {
                throw new CommandException(line, "Turn To", "cannot use variable type "+varManager.getVar(InVarIDs[0]).getType().toString()+" as angle to go to.");
            }
        }
    }
}
