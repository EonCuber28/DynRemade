package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class toDeg extends Command {
    public toDeg(int line){
        super(line, CommandType.toDeg,new String[0],"");
    }
}
