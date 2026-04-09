package org.SquidSquad.CommandSequencer.Commands.variables;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class SetVar extends Command {
    public SetVar(int line){
        super(line, CommandType.SetVar,new String[0],"");
    }
}
