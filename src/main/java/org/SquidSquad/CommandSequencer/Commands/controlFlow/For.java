package org.SquidSquad.CommandSequencer.Commands.controlFlow;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.variables.Variable;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class For extends Command {
    private final ArrayList<Command> innerCommands = new ArrayList<>();
    public For(int line, String looped, String target){
        super(line, CommandType.For,new String[]{looped},target); // tell the Command class our inputs
    }

    public void addCommand(Command command){innerCommands.add(command);}

    @Override
    public void run(){
        super.run();
        Variable loopedVar = varManager.getVar(super.getInVarIDs()[0]);
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
