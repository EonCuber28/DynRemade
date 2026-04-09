package org.SquidSquad.CommandSequencer.Commands.movement;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class GoTo extends Command {
    public GoTo(int line){
        super(line, CommandType.GoTo,new String[0],"");
    }
}
