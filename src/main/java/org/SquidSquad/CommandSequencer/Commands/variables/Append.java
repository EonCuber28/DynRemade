package org.SquidSquad.CommandSequencer.Commands.variables;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.Tokenizer.Token;

public class Append extends Command{
    private final Token inTk;
    private Token idx = null;
    public Append(int line, Token in, String out){
        super(line, CommandType.Append, new String[]{String.valueOf(in.getValue())},out);
        inTk = in;
    }
    public Append(int line, Token in, Token InDex, String out){
        super(line, CommandType.Append, new String[]{String.valueOf(in.getValue()),String.valueOf(InDex.getValue())},out);
        inTk = in;
        idx = InDex;
    }

    public void run(){
        super.run();
    }
}
