package org.SquidSquad.CommandSequencer.Commands.movement;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandException;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class SplineTo extends Command {
    private boolean Literal;
    private double[] pose;
    public SplineTo(int line, String A, String B){
        super(line, CommandType.SplineTo,new String[]{A,B});
        Literal = false;
    }
    public SplineTo(int line, String A, String B, String C){
        super(line, CommandType.SplineTo, new String[]{A,B,C});
        Literal = false;
    }
    public SplineTo(int line, String point){
        super(line, CommandType.SplineTo, new String[]{point});
        Literal = false;
    }
    public SplineTo(int line, double A, double B){
        super(line, CommandType.SplineTo, new String[]{String.valueOf(A), String.valueOf(B)});
        Literal = true;
        pose = new double[]{A,B};
    }
    public SplineTo(int line, double A, double B, double C){
        super(line, CommandType.SplineTo, new String[]{String.valueOf(A), String.valueOf(B), String.valueOf(C)});
        Literal = true;
        pose = new double[]{A,B,C};
    }
    public SplineTo(int line, double[] pos){
        super(line, CommandType.SplineTo,"");
        switch (pos.length){
            case 2 -> super.InVarIDs = new String[]{
                    String.valueOf(pos[0]),
                    String.valueOf(pos[1])};
            case 3 -> super.InVarIDs = new String[]{
                    String.valueOf(pos[0]),
                    String.valueOf(pos[1]),
                    String.valueOf(pos[2])};
        }
        pose = pos.clone();
        Literal = true;
    }

    private double[][] Spline2Bez(double[][] points, double[] angles){
        // Claude told me to
        double[] start = points[0];
        double[] end = points[1];

        double dist = Math.sqrt(
                Math.pow(end[0]-start[0],2)
                +Math.pow(end[1]-start[1],2));

        double tx0 = Math.cos(angles[0])*dist;
        double ty0 = Math.sin(angles[0])*dist;
        double tx1 = Math.cos(angles[1])*dist;
        double ty1 = Math.cos(angles[1])*dist;

        double[] bezStart = start.clone();
        double[] mid1 = {(start[0]+tx0)/3.0, (start[1]+ty0)/3.0};
        double[] mid2 = {(end[0]-tx1)/3.0, (end[1]+ty1)/3.0};
        double[] bezEnd = end.clone();

        return new double[][]{bezStart,mid1,mid2,bezEnd};
    }

    @Override
    public void run(){
        double[] start = getRobotPose.get(); // TODO: fix this piece of garbage, and instead use a "last pos" var form the last move command. (also removes another command register step)
        double[] end;

        if (Literal){
            switch (InVarIDs.length){
                case 2 -> {
                    end = new double[]{pose[0],pose[1],start[1]};
                    moveSE(start, end);
                }
                case 3 -> {
                    end = pose.clone();
                    moveSE(start, end);
                }
            }
        } else {
            switch (InVarIDs.length){
                case 1 -> {
                    if (varManager.getVar(InVarIDs[0]).getType() == VariableTypes.FieldCord){
                        end = new double[]{
                                (double) (((Variable[]) varManager.getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double) (((Variable[]) varManager.getVar(InVarIDs[0]).getValue())[1].getValue()),
                                start[0]
                        };
                        moveSE(start, end);
                    } else if(varManager.getVar(InVarIDs[0]).getType() == VariableTypes.FieldPos) {
                        end = new double[]{
                                (double) (((Variable[]) varManager.getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double) (((Variable[]) varManager.getVar(InVarIDs[0]).getValue())[1].getValue()),
                                (double) (((Variable[]) varManager.getVar(InVarIDs[0]).getValue())[3].getValue())
                        };
                        moveSE(start, end);
                    } else {
                        throw new CommandException(line, "Spline To", "variable type "+varManager.getVar(InVarIDs[0]).getType().toString()+" is not FieldPos/Cord");
                    }
                }
                case 2 -> {
                    if (varManager.getVar(InVarIDs[0]).getType() == VariableTypes.Number && varManager.getVar(InVarIDs[1]).getType() == VariableTypes.Number){
                        end = new double[]{
                                (double) varManager.getVar(InVarIDs[0]).getValue(),
                                (double) varManager.getVar(InVarIDs[1]).getValue(),
                                start[2]
                        };
                        moveSE(start, end);
                    } else {
                        String type1 = varManager.getVar(InVarIDs[0]).getType().toString();
                        String type2 = varManager.getVar(InVarIDs[1]).getType().toString();
                        throw new CommandException(line, "Spline To", "Cannot use variable types "+type1+", "+type2+" as values for coordinates");
                    }
                }
                case 3 -> {
                    if (varManager.getVar(InVarIDs[0]).getType() == VariableTypes.Number && varManager.getVar(InVarIDs[1]).getType() == VariableTypes.Number && varManager.getVar(InVarIDs[2]).getType() == VariableTypes.Number) {
                        end = new double[]{
                                (double) varManager.getVar(InVarIDs[0]).getValue(),
                                (double) varManager.getVar(InVarIDs[1]).getValue(),
                                (double) varManager.getVar(InVarIDs[2]).getValue(),
                        };
                        moveSE(start, end);
                    } else {
                        String type1 = varManager.getVar(InVarIDs[0]).getType().toString();
                        String type2 = varManager.getVar(InVarIDs[1]).getType().toString();
                        String type3 = varManager.getVar(InVarIDs[2]).getType().toString();
                        throw new CommandException(line, "Spline To", "Cannot use variable types "+type1+", "+type2+", "+type3+" as values for coordinates");
                    }
                }
            }
        }
    }
    private void moveSE(double[] start, double[] end) {
        if (canSpline) {
            doSpline.accept(new double[][]{start, end});
            if (isLinearFollower) waitForIdle();
        } else if (canBez){
            double[][] bezPoints = Spline2Bez(new double[][]{start,end}, new double[]{start[2],end[2]});
            doBez.accept(bezPoints);
            if (isLinearFollower) waitForIdle();
        } else {
            throw new CommandException(line, "Spline To", "Follower cannot follow spline-like paths");
        }
    }
    private void waitForIdle(){
        while (!isBotIdle.get()){ // after much contemplating, I decided that we just gonna run this with a max refresh of 100hz
            long start = System.currentTimeMillis();

            updateFollower.run();
            runUpdate.run();

            long diff = System.currentTimeMillis() - start;
            long sleep = 10-diff;
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
