package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Cos extends Command {
    public Cos(int line){
        super(line, CommandType.Cos,new String[0],"");
    }
}
