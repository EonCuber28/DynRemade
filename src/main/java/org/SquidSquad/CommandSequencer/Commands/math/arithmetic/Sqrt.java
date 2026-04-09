package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Sqrt extends Command {
    public Sqrt(int line){
        super(line, CommandType.Sqrt,new String[0],"");
    }
}
