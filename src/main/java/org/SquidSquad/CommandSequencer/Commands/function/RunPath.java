package org.SquidSquad.CommandSequencer.Commands.function;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RunPath extends Command {
    public RunPath(int line, String pathID){
        super(line, CommandType.RunPath,new String[0],pathID);
    }
    @Override
    public void run(){
        super.run();
        runDynPath.accept(super.OutVarID);
    }
}
