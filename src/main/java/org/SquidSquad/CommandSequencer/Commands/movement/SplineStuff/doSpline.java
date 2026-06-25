package org.SquidSquad.CommandSequencer.Commands.movement.SplineStuff;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class doSpline extends Command {
    private splineType type;
    private boolean Literal = false;
    private double[] endPos;
    private String endTanID;
    private Double endTanVal;
    public doSpline(int line, String Coord, String endTan, splineType type){
        super (line, CommandType.SplineTo, new String[]{Coord, endTan});
        this.type = type;
        endTanID = endTan;
    }
    public doSpline(int line, String Coord, double endTan, splineType type){
        super (line, CommandType.SplineTo, new String[]{Coord, String.valueOf(endTan)});
        this.type = type;
        Literal = true;
        endTanVal = endTan;
    }
    public doSpline(int line, String Coord, splineType type){
        super (line, CommandType.SplineTo, new String[]{Coord});
        this.type = type;
    }

    @Override
    public void run(){
        double[] start = getRobotPose.get();
        double[] end = endPos;

        double endTan;
        if (endTanID != null && endTanVal != null) {
            if (Literal) {
                endTan = endTanVal;
            } else {
                if (varManager.getVar(endTanID).getType() == VariableTypes.Number) {
                    endTan = (double) varManager.getVar(endTanID).getValue();
                } else {
                    throw new CommandException(line,"doSpline","Given variable is not a number!");
                }
            }
        } else {
            endTan = Math.atan2(end[1]-start[1], end[0]-start[0]);
        }

        double[] out = null;
        if (end.length == 2){
            out = new double[]{end[0],end[1],endTan};
        } else if (end.length == 3){
            out = new double[]{end[0],end[1],end[2],endTan};
        }
        doSpline.accept(out,type);
    }
}
