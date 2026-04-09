package org.SquidSquad.CommandSequencer.Commands.ControlFlow;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class While extends Command {
    public While(int line){
        super(line, CommandType.While, new String[0],"");
    }
}
