package org.SquidSquad.CommandSequencer.Commands.telemetry;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Update extends Command {
    public Update(int line){
        super(line, CommandType.Clear,new String[0],"");
    }
}
