package org.SquidSquad.CommandSequencer.Commands;

import org.SquidSquad.CommandSequencer.VariableManager;
import org.SquidSquad.CommandSequencer.Variables.Variable;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Command {
    protected static boolean canBez;
    protected static boolean canSpline;
    protected static boolean isLinearFollower;
    protected static Consumer<double[][]> doSpline;
    protected static Consumer<double[][]> doBez;
    protected static Consumer<double[][]> moveTo;       // required
    protected static Consumer<Double> turnTo;         // required
    protected static Supplier<Boolean> isBotIdle;
    protected static Supplier<double[]> getRobotPose; // required
    protected static Runnable runUpdate;
    protected static Runnable updateFollower;
    protected static Runnable isOpModeRunning; // needed for ensuring we stop non-linear move commands when op mode is stopped
    public void registerUpdaters(Runnable updateLoop, Runnable isOpModeRunning){
        runUpdate = updateLoop;
        Command.isOpModeRunning = isOpModeRunning;
    }
    public void registerUpdaters(Runnable updateLoop, Runnable isOpModeRunning, Runnable updateFollower){
        runUpdate = updateLoop;
        Command.isOpModeRunning = isOpModeRunning;
        Command.updateFollower = updateFollower;
    }
    public void registerFollower(Supplier<Boolean> isBotIdle, Supplier<double[]> getRobotPose, Consumer<double[][]> moveTo, Consumer<Double> turnTo){
        canBez = false; canSpline = false;
        isLinearFollower = false;

        Command.moveTo = moveTo;
        Command.turnTo = turnTo;
        Command.isBotIdle = isBotIdle;
        Command.getRobotPose = getRobotPose;
    }
    public void registerFollower(Supplier<Boolean> isBotIdle, Supplier<double[]> getRobotPose, Consumer<double[][]> moveTo, Consumer<Double> turnTo, Consumer<double[][]> doCurve, boolean usesSpline){
        canBez = !usesSpline;
        canSpline = !canBez;
        isLinearFollower = false;

        Command.moveTo = moveTo;
        Command.turnTo = turnTo;
        Command.isBotIdle = isBotIdle;
        Command.getRobotPose = getRobotPose;

        if (canBez) Command.doBez = doCurve;
        else Command.doSpline = doCurve;
    }
    public void registerFollower(Consumer<double[][]> moveTo, Supplier<double[]> getRobotPose, Consumer<Double> turnTo){
        canBez = false; canSpline = false;
        isLinearFollower = true;

        Command.moveTo = moveTo;
        Command.turnTo = turnTo;
        Command.getRobotPose = getRobotPose;
    }
    public void registerFollower(Consumer<double[][]> moveTo, Supplier<double[]> getRobotPose, Consumer<Double> turnTo, Consumer<double[][]> doCurve, boolean usesSpline){
        canBez = !usesSpline;
        canSpline = !canBez;
        isLinearFollower = true;

        Command.moveTo = moveTo;
        Command.turnTo = turnTo;
        Command.getRobotPose = getRobotPose;

        if (canBez) Command.doBez = doCurve;
        else Command.doSpline = doCurve;
    }

    protected static ArrayList<String> telemBuffer = new ArrayList<>();
    protected static Consumer<String> runDynPath;
    public void registerDynPathRunner(Consumer<String> runner){
        runDynPath = runner;
    }
    protected VariableManager varManager;
    private Variable getVar(String ID){
        return varManager.getVar(ID);
    }

    protected String[] InVarIDs;
    protected String OutVarID;

    private CommandType type;

    protected int line;

    public Command(int line, CommandType type, String[] InVarIDs, String OutVarID){
        this.line = line;
        this.type = type;
        this.InVarIDs = InVarIDs;
        this.OutVarID = OutVarID;
    }
    public Command(int line, CommandType type, String[] InVarIDs){
        this.line = line;
        this.type = type;
        this.InVarIDs = InVarIDs;
        this.OutVarID = "";
    }
    public Command(int line, CommandType type, String OutVarID){
        this.line = line;
        this.type = type;
        this.InVarIDs = new String[]{};
        this.OutVarID = OutVarID;
    }

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

            case SplineTo -> commandType = "Bezier";
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
