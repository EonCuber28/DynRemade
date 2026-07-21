package org.SquidSquad.CommandSequencer;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.function.DynPath;

import java.util.Map;

public class CommandRunner {
    private final CommandConstructor constructor;
    public CommandRunner(CommandConstructor constructor){
        this.constructor = constructor;
    }

    public void run(){
        Map<String,DynPath> mp = constructor.getFuncIDmap();
        mp.get(constructor.getMainFuncName()).run();
    }
}
