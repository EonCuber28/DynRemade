package org.SquidSquad.CommandSequencer.Commands.controlFlow;

import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.variables.Variable;
import org.SquidSquad.CommandSequencer.variables.primitives.DynBoolean;
import org.SquidSquad.CommandSequencer.variables.primitives.DynNumber;
import org.SquidSquad.CommandSequencer.variables.primitives.DynString;
import org.SquidSquad.Tokenizer.Token;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class For extends Command {
    private final Token looped;
    private final ArrayList<Command> innerCommands = new ArrayList<>();
    public For(int line, Token looped, String target){
        super(line, CommandType.For,new String[]{looped.toString()},target); // tell the Command class our inputs
        this.looped = looped;
    }

    public void addCommand(Command command){innerCommands.add(command);}

    @Override
    public void run(){
        super.run();
        Variable loopedVar = null;
        switch (looped.type()){
            case Boolean -> loopedVar = new DynBoolean((boolean)looped.getValue());
            case Number -> loopedVar = new DynNumber((double)looped.getValue());
            case String -> loopedVar = new DynString((String)looped.getValue());
            case Name -> {
                Variable l = varManager.getVar((String)looped.getValue());
                if (l == null) throw new CommandException(line,"For","Variable "+looped.getValue()+" not defined!");
                loopedVar = l;
            }
            default -> throw new CommandException(line,"For","Cannot use "+looped.type()+" as looped value!");
        }
        switch (loopedVar.getType()){
            case List -> {
                ArrayList<Variable> arrayItems = (ArrayList<Variable>)loopedVar.getValue();
                for (Variable item : arrayItems){
                    varManager.getVar(super.getOutVarID()).setVariable(item);
                    for (Command cmd : innerCommands){
                        cmd.run();
                    }
                }
            }
            case Number -> {
                int Number = (int) loopedVar.getValue();
                int[] NumberRange = IntStream.rangeClosed(0, Number).toArray();
                for (int number : NumberRange){
                    varManager.getVar(super.getOutVarID()).setValue(number);
                    for (Command cmd : innerCommands){
                        cmd.run();
                    }
                }
            }
            case String -> {
                for (char chunk : ((String)loopedVar.getValue()).toCharArray()){
                    varManager.getVar(super.getOutVarID()).setValue(chunk);
                    for (Command cmd : innerCommands){
                        cmd.run();
                    }
                }
            }
        }
    }
}
