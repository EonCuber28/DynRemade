package org.SquidSquad.CommandSequencer.Commands.math.arithmetic;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Decrement extends Command {
    public Decrement(int line, String var){
        super(line, CommandType.Decrement,new String[]{var});
    }
    @Override
    public void run(){
        super.run();
        varManager.getVar(super.InVarIDs[0]).Dec();
    }
}
