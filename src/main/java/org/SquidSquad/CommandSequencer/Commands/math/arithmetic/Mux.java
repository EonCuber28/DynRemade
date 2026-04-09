package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Mux extends Command {
    public Mux(int line){
        super(line, CommandType.Mux,new String[0],"");
    }
}
