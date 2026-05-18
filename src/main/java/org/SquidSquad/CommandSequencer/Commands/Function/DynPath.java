package org.SquidSquad.CommandSequencer.Commands.Function;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

import java.util.ArrayList;

public class DynPath extends Command {
    private final ArrayList<Command> commandList = new ArrayList<>();
    public DynPath(int line){
        super(line, CommandType.DynPath,new String[0]);
    }
    public void addCommand(Command cmd){
        commandList.add(cmd);
    }
    @Override
    public void run(){
        for (Command cmd : commandList){
            cmd.run();
        }
    }
}
