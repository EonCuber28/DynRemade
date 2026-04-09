package org.SquidSquad.CommandSequencer.Commands.random;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class RngInteger extends Command {
    public RngInteger(int line){
        super(line, CommandType.RngInteger,new String[0],"");
    }
}
