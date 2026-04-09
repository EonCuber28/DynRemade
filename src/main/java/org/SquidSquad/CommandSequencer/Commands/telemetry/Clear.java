package org.SquidSquad.CommandSequencer.Commands.telemetry;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Clear extends Command {
    public Clear(int line){
        super(line, CommandType.Clear, new String[0],"");
    }
}
