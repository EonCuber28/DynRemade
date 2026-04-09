package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngFloat extends Command {
    public RngFloat(int line){
        super(line, CommandType.RngFloat, new String[0],"");
    }
}
