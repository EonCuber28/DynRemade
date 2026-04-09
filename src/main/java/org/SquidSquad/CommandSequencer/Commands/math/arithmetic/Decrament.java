package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Decrament extends Command {
    public Decrament(int line){
        super(line, CommandType.Decrement,new String[0],"");
    }
}
