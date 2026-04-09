package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngBoolean extends Command {
    public RngBoolean(int line){
        super(line, CommandType.RngBoolean, new String[0],"");
    }
}
