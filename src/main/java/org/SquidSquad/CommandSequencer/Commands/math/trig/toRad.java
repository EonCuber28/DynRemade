package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class toRad extends Command {
    public toRad(int line){
        super(line, CommandType.toRad, new String[0],"");
    }
}
