package org.SquidSquad.CommandSequencer.Commands.movement;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandException;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

public class GoTo extends Command {
    private boolean Literal;
    private double[] pos;
    public GoTo(int line, double[] pose){
        super(line, CommandType.GoTo,new String[]{},"");
        Literal = true;
        switch (pose.length){
            case 2 -> super.InVarIDs = new String[]{
                    String.valueOf(pose[0]),
                    String.valueOf(pose[1])};
            case 3 -> super.InVarIDs = new String[]{
                    String.valueOf(pose[0]),
                    String.valueOf(pose[1]),
                    String.valueOf(pose[2])};
        }
        pos = pose.clone(); // we don't wanna touch the original (or worse, have the original touch US)
    }
    public GoTo(int line, double x, double y, double h){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(x),
                String.valueOf(y),
                String.valueOf(h)});
        Literal = true;
        pos = new double[]{x,y,h};
    }
    public GoTo(int line, double x, double y){
        super(line, CommandType.GoTo, new String[]{
                String.valueOf(x),
                String.valueOf(y)});
        Literal = true;
        pos = new double[]{x,y};
    }
    public GoTo(int line, String var){
        super(line, CommandType.GoTo,new String[]{var});
        Literal = false;
    }
    public GoTo(int line, String varX, String varY){
        super(line, CommandType.GoTo,new String[]{varX,varY});
        Literal = false;
    }
    public GoTo(int line, String varX, String varY, String varH){
        super(line, CommandType.GoTo,new String[]{varX,varY,varH});
        Literal = false;
    }

    @Override
    public void run(){
        double[] start = getRobotPose.get();
        double[] end;
        if (Literal){
            switch (pos.length){
                case 2 -> {
                    end = new double[]{
                            pos[0],
                            pos[1],
                            start[2]
                    }; // allocate even MORE RAM!!!!
                    moveSE(start, end);
                }
                case 3 -> {
                    end = pos;  // we already control it, so no need to copy it.
                    moveSE(start, end);
                }
            }
        } else {
            switch (InVarIDs.length){
                case 1 -> {
                    if (varManager.getVar(InVarIDs[0]).getType() == VariableTypes.FieldCord){
                        end = new double[]{
                                (double)(((Variable[])varManager.getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double)(((Variable[])varManager.getVar(InVarIDs[0]).getValue())[1].getValue()),
                                start[2]
                        };
                    } else if(varManager.getVar(InVarIDs[0]).getType() == VariableTypes.FieldPos){
                        end = new double[]{
                                (double)(((Variable[])varManager.getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double)(((Variable[])varManager.getVar(InVarIDs[0]).getValue())[1].getValue()),
                                (double)(((Variable[])varManager.getVar(InVarIDs[0]).getValue())[2].getValue())
                        };
                    } else {
                        throw new CommandException(line, "GoTo", "Cannot use variable type: "+varManager.getVar(InVarIDs[0]).getValue().toString()+" as valid field position");
                    }
                    moveSE(start, end);
                }
                case 2 -> {
                    if (varManager.getVar(InVarIDs[0]).getType() == VariableTypes.Number && varManager.getVar(InVarIDs[1]).getType() == VariableTypes.Number){
                        end = new double[]{
                                (double)(((Variable[])varManager.getVar(InVarIDs[0]).getValue())[0].getValue()),
                                (double)(((Variable[])varManager.getVar(InVarIDs[0]).getValue())[1].getValue()),
                                start[2]
                        };
                        moveSE(start, end);
                    } else {
                        String type1 = varManager.getVar(InVarIDs[0]).getType().toString();
                        String type2 = varManager.getVar(InVarIDs[1]).getType().toString();
                        throw new CommandException(line, "GoTo", "Cannot use variable types "+type1+", "+type2+" as values for coordinates");
                    }
                }
                case 3 -> {
                    if (varManager.getVar(InVarIDs[0]).getType() == VariableTypes.Number && varManager.getVar(InVarIDs[1]).getType() == VariableTypes.Number && varManager.getVar(InVarIDs[2]).getType() == VariableTypes.Number){
                        end = pos;
                        moveSE(start, end);
                    } else {
                        String type1 = varManager.getVar(InVarIDs[0]).getType().toString();
                        String type2 = varManager.getVar(InVarIDs[1]).getType().toString();
                        String type3 = varManager.getVar(InVarIDs[2]).getType().toString();
                        throw new CommandException(line, "GoTo", "Cannot use variable types "+type1+", "+type2+", "+type3+" as values for coordinates");
                    }
                }
            }
        }
    }
    private void moveSE(double[] start, double[] end){ // this feels way too simple compared to SplineTo
        moveTo.accept(new double[][]{start, end});
        if (isLinearFollower) waitForIdle();
    }
    private void waitForIdle(){
        while (!isBotIdle.get()){
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
