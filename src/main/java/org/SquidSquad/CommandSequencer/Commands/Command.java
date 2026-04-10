package org.SquidSquad.CommandSequencer.Commands;

import org.SquidSquad.CommandSequencer.VariableManager;
import org.SquidSquad.CommandSequencer.Variables.Variable;

public class Command {
    protected VariableManager varManager;
    private Variable getVar(String ID){
        return varManager.getVar(ID);
    }

    protected String[] InVarIDs;
    protected String OutVarID;

    private CommandType type;

    protected int line;

    public Command(int line, CommandType type, String[] InVarIDs, String OutVarID){}
    public Command(int line, CommandType type, String[] InVarIDs){}
    public Command(int line, CommandType type, String OutVarID){}

    public void run(){}

    // getters
    public CommandType getType(){
        return type;
    }
    public String[] getInVarIDs(){
        return InVarIDs;
    }
    public String getOutVarID(){
        return OutVarID;
    }
    public int getLine(){
        return line;
    }
    // debug
    public String toString(){
        // "@lineN: Type; [InID1,InID2]; outID"
        String out = "@line"+line+": ";
        String commandType;
        switch (type){
            case For -> commandType = "For";
            case If -> commandType = "If";
            case While -> commandType = "While";

            case jFunc -> commandType = "jFunc";
            case RunPath -> commandType = "RunPath";

            case Cos -> commandType = "Cos";
            case iCos -> commandType = "iCos";
            case iSin -> commandType = "iSin";
            case iTan -> commandType = "iTan";
            case Sin -> commandType = "Sin";
            case Tan -> commandType = "Tan";
            case toDeg -> commandType = "toDeg";
            case toRad -> commandType = "toRad";

            case Add -> commandType = "Add";
            case Decrement -> commandType = "Decrement";
            case Div -> commandType = "Div";
            case Increment -> commandType = "Increment";
            case Mux -> commandType = "Mux";
            case Pow -> commandType = "Pow";
            case Sqrt -> commandType = "Sqrt";
            case Sub -> commandType = "Sub";

            case Bezier -> commandType = "Bezier";
            case GoTo -> commandType = "GoTo";
            case TurnTo -> commandType = "TurnTo";

            case RngBoolean -> commandType = "RngBoolean";
            case RngDouble -> commandType = "RngDouble";
            case RngFloat -> commandType = "RngFloat";
            case RngInteger -> commandType = "RngInteger";

            case AddData -> commandType = "AddData";
            case Clear -> commandType = "Clear";
            case Update -> commandType = "Update";

            case SetVar -> commandType = "SetVar";
            default -> commandType = "Null/Undef (VERY BAD)";
        }
        out = out+commandType+"; [";
        for (int i = 0; i < InVarIDs.length; i++){
            out = out+InVarIDs[i];
            if (i != InVarIDs.length-1){
                out = out+", ";
            }
        }
        out = out+"]; "+OutVarID;
        return out;
    }
}
