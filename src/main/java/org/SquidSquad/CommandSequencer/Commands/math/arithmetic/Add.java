package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Add extends Command {
    public Add(int line){
        super(line, CommandType.Add,new String[0],"");
    }
}
