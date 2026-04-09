package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngDouble extends Command {
    public RngDouble(int line){
        super(line, CommandType.RngDouble,new String[0],"");
    }
}
