package org.SquidSquad.CommandSequencer.Commands.telemetry;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class AddData extends Command {
    private boolean isLiteral;
    public AddData(int line, String value, boolean isLiteral){
        super(line, CommandType.AddData,new String[]{value,"Is Literal"});
        this.isLiteral = isLiteral;
    }
    @Override
    public void run(){
        if (isLiteral){
            telemBuffer.add(InVarIDs[0]);
        } else {
            telemBuffer.add(String.valueOf(varManager.getVar(InVarIDs[0]).getValue()));
        }
    }
}
