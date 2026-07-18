package org.SquidSquad.CommandSequencer.Commands.variables;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.Tokenizer.Token;

public class Insert extends Command{
    private final String target;
    private final Token InDex;
    private final Token in;
    public Insert(int line, Token in, Token InDex, String target){
        super(line,CommandType.Insert,new String[]{String.valueOf(in.getValue()),String.valueOf(InDex.getValue())},target);
        this.target = target;
        this.InDex = InDex;
        this.in = in;
    }

    public void run(){
        super.run();
    }
}
