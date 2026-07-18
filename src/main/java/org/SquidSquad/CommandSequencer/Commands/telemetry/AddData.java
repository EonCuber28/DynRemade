package org.SquidSquad.CommandSequencer.Commands.telemetry;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class AddData extends Command {
    private Object literal;
    public AddData(int line, String value){
        super(line, CommandType.AddData,new String[]{value});
    }
    public AddData(int line, Object literal){
        super(line, CommandType.AddData,new String[]{String.valueOf(literal)});
        this.literal = literal;
    }
    @Override
    public void run(){
        super.run();
        if (literal != null) telemBuffer.add(String.valueOf(literal));
        else telemBuffer.add(String.valueOf(varManager.getVar(InVarIDs[0]).getValue()));
    }
}
