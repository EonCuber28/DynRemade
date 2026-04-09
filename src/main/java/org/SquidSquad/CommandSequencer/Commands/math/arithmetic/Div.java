package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Div extends Command {
    public Div(int line){
        super(line, CommandType.Div,new String[0],"");
    }
}
