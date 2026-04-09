package org.SquidSquad.CommandSequencer.Commands.ControlFlow;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class If extends Command {
    public If(int line){
        super(line, CommandType.If,new String[0],"");
    }
}