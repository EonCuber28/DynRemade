package org.SquidSquad.CommandSequencer.Commands.controlFlow;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

import java.util.ArrayList;

public class If extends Command {
    private final Condition condition;
    private final ArrayList<Command> innerCommands = new ArrayList<>();
    public If(int line, Condition condition){
        super(line, CommandType.If,"");
        this.condition = condition;
    }

    public void addCommand(Command command){
        innerCommands.add(command);
    }

    @Override
    public void run(){
        super.run();
        if (condition.getResult()){
            for (Command cmd : innerCommands){
                cmd.run();
            }
        }
    }
}