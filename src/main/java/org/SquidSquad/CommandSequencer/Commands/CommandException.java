package org.SquidSquad.CommandSequencer.Commands;

public class CommandException extends RuntimeException {
    public CommandException(int line, String command, String message) {
        super("@line"+line+"with command \""+command+"\" got error: "+message);
    }
}
