package org.SquidSquad.CommandSequencer.Commands.variables;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.Tokenizer.Token;

public class Get extends Command{
    private final String in;
    private final String out;
    private final Token InDex;
    public Get(int line, String in, Token InDex, String out){
        super(line, CommandType.Get, new String[]{in,String.valueOf(InDex.getValue())},out);
        this.in = in;
        this.out = out;
        this.InDex = InDex;
    }

    public void run(){
        super.run();
    }
}
