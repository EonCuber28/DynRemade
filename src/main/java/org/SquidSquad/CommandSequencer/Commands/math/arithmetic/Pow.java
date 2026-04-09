package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Pow extends Command {
    public Pow(int line){
        super(line, CommandType.Pow,new String[0],"");
    }
}
