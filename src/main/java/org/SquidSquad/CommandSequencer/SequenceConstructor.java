package org.SquidSquad.CommandSequencer;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.Tokenizer.Token;

import java.util.ArrayList;

public class SequenceConstructor {
    private ArrayList<Token[]> lines;
    private VariableManager varMan;

    public SequenceConstructor(Token[] inTokens, PathplannerLinker PPL){
        lines = new ArrayList<>();
        varMan = new VariableManager();
        processInTokens2Lines(inTokens);
    }

    private void processInTokens2Lines(Token[] inTokens){

    }


    public Command[] getProcessedCommands(){
        return null; // TODO
    }
}
