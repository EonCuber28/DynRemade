package org.SquidSquad.CommandSequencer.Commands;

import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.movement.splineStuff.SplineType;
import org.SquidSquad.CommandSequencer.VariableManager;
import org.SquidSquad.CommandSequencer.variables.Variable;
import org.SquidSquad.CommandSequencer.variables.VariableException;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Command {
    // telemetry thing
    protected static final ArrayList<String> telemBuffer = new ArrayList<>();
    // misc linker stuff
    protected static Consumer<String> runDynPath;
    public static void registerPathRunner(Consumer<String> runner){
        Command.runDynPath = runner;
    }
    // pathPlanner linker stuff
    protected static Consumer<double[]> moveTo;
    protected static Consumer<Double> turnTo;
    public static void registerCommonActions(Consumer<double[]> movey, Consumer<Double> turny){
        Command.moveTo = movey;
        Command.turnTo = turny;
    }

    protected static boolean doFollowerSpline = false;
    protected static boolean doFollowerBez = false;
    protected static BiConsumer<double[], SplineType> doSpline;
    protected static Consumer<double[][]> doBez;
    public static void registerDoSpline(BiConsumer<double[], SplineType> spliney){
        Command.doFollowerSpline = true;
        Command.doSpline = spliney;
    }
    public static void registerDoBez(Consumer<double[][]> bezzy){
        Command.doFollowerBez = true;
        Command.doBez = bezzy;
    }

    protected static boolean isLinearFollower = true;
    protected static Supplier<double[]> getBotPos;
    public static void registerPosGetter(Supplier<double[]> getter){
        Command.isLinearFollower = false;
        Command.getBotPos = getter;
    }

    protected static Runnable updateLoop;
    protected static Runnable updateFollower;
    protected static Supplier<Boolean> isIdle;
    public static void registerMainLoop(Runnable updater){
        Command.updateLoop = updater;
    }
    public static void registerFollowerUpdater(Runnable updater, Supplier<Boolean> isIdle){
        Command.updateFollower = updater;
        Command.isIdle = isIdle;
    }
    protected static Supplier<double[]> getRobotPose;
    public static void registerRobotPose(Supplier<double[]> roboPose){
        getRobotPose = roboPose;
    }

    // the rest of the class
    protected static VariableManager varManager;
    public static void linkVarMan(VariableManager varMan){
        Command.varManager = varMan;
    }
    private Variable getVar(String ID){
        return varManager.getVar(ID);
    }

    protected String[] InVarIDs;
    protected String OutVarID;

    private final CommandType type;

    protected final int line;

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

    public void addCommand(Command cmd){
        throw new CommandException(line,type.toString(),"Cannot add command to "+type+" command type!");
    }

    public void run(){
        VariableException.setLine(line);
    }

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
        StringBuilder out = new StringBuilder("@line" + line + ": ");
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

            case SplineTo -> commandType = "Spline";
            case BezTo ->  commandType = "Bezier";
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
        out.append(commandType).append("; [");
        for (int i = 0; i < InVarIDs.length; i++){
            out.append(InVarIDs[i]);
            if (i != InVarIDs.length-1){
                out.append(", ");
            }
        }
        out.append("]; ").append(OutVarID);
        return out.toString();
    }
}
