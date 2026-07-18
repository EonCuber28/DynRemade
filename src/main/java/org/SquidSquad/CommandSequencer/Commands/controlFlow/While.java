package org.SquidSquad.CommandSequencer.Commands.controlFlow;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

import java.util.ArrayList;

public class While extends Command {
    private final Condition condition;
    private final ArrayList<Command> innerCommands = new ArrayList<>();
    public While(int line, Condition condition){
        super(line, CommandType.While, new String[0],"");
        this.condition = condition;
    }
    public void addCommand(Command command){
        innerCommands.add(command);
    }
    @Override
    public void run(){
        super.run();
        while (condition.getResult()){
            for (Command cmd : innerCommands){
                cmd.run();
            }
        }
    }
}
