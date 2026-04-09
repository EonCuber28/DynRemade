package org.SquidSquad.CommandSequencer.Commands.movement;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Bezier extends Command {
    public Bezier(int line){
        super(line, CommandType.Bezier,new String[0],"");
    }
}
