package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Incrament extends Command {
    public Incrament(int line){
        super(line, CommandType.Increment,new String[0],"");
    }
}
