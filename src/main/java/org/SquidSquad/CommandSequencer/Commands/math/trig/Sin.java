package org.SquidSquad.CommandSequencer.Commands.math.trig;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Sin extends Command {
    public Sin(int line){
        super(line, CommandType.Sin,new String[0],"");
    }
}
