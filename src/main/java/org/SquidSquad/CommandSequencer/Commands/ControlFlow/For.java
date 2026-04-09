package org.SquidSquad.CommandSequencer.Commands.ControlFlow;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class For extends Command {
    public For(int line){
        super(line, CommandType.For,new String[0],"");
    }
}
