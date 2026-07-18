package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Increment extends Command {
    public Increment(int line, String var){
        super(line, CommandType.Increment,new String[]{var});
    }
    @Override
    public void run(){
        super.run();
        varManager.getVar(InVarIDs[0]).Inc();
    }
}
