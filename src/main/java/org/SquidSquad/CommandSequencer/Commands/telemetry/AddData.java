package org.SquidSquad.CommandSequencer.Commands.telemetry;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class AddData extends Command {
    public AddData(int line){
        super(line, CommandType.AddData,new String[0],"");
    }
}
