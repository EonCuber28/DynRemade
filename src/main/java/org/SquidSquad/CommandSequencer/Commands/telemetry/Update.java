package org.SquidSquad.CommandSequencer.Commands.telemetry;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

public class Update extends Command {
    public Update(int line){
        super(line, CommandType.Clear,new String[0],"");
    }
    @Override
    public void run(){
        // clear the terminal for UNIX system (I don't care about windows)
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (String telemChunk : telemBuffer){
            System.out.println("DYN: "+telemChunk);
        }
    }
}
