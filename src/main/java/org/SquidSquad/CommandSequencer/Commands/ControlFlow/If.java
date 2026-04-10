package org.SquidSquad.CommandSequencer.Commands.ControlFlow;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

import java.util.ArrayList;

public class If extends Command {
    private Condition condition;
    private ArrayList<Command> innerCommands = new ArrayList<>();
    public If(int line, Condition condition){
        super(line, CommandType.If,"");
        this.condition = condition;
    }

    public void addCommand(Command command){
        innerCommands.add(command);
    }

    @Override
    public void run(){
        if (condition.getResult()){
            for (Command cmd : innerCommands){
                cmd.run();
            }
        }
    }
}