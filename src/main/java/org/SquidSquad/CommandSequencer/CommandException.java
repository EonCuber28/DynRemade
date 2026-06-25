package org.SquidSquad.CommandSequencer;

public class CommandException extends RuntimeException {
    public CommandException(int line, String command, String message) {
        super("DYN ERROR!\n@line:"+line+" Command: "+command+" File:\n"+getFileStringError(line)+"\nReason: "+message);
    }
    public CommandException(int line, int column, String reason){
        super("DYN ERROR!\n@line:"+line+" Column:"+column+" File:\n"+getFileStringError(line,column)+"\nReason: "+reason);

    }

    private static String[] ogFileLines;
    public static void linkFile(String file){
        ogFileLines = file.split("\r\n|\r|\n");
    }

    private static String getFileStringError(int line){
        if (line >= 0 && line < ogFileLines.length) {
            String fileLine = ogFileLines[line];
            return fileLine+"\n^^^";
        } else {
            return "Unable to access file. Line index "+line+" is out of bounds";
        }
    }
    private static String getFileStringError(int line, int column){
        if (line >= 0 && line < ogFileLines.length) {
            String fileLine = ogFileLines[line];
            StringBuilder pointerHeads = new StringBuilder();
            for (int x = 0; x < column; x++) pointerHeads.append(' ');
            pointerHeads.append("^^^");
            return fileLine + "\n" + pointerHeads;
        } else {
            return "Unable to access file. Line index "+line+" is out of bounds";
        }
    }
}
