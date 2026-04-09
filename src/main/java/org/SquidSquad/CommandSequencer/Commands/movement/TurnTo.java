package org.SquidSquad.CommandSequencer.Commands.movement;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class TurnTo extends Command {
    public TurnTo(int line){
        super(line, CommandType.TurnTo, new String[0],"");
    }
}
