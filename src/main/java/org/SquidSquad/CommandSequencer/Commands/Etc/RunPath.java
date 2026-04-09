package org.SquidSquad.CommandSequencer.Commands.Etc;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RunPath extends Command {
    public RunPath(int line){
        super(line, CommandType.RunPath,new String[0],"");
    }
}
