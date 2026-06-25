package org.SquidSquad.CommandSequencer.Commands.movement.BezierStuff;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;

import java.util.ArrayList;
import java.util.List;

public class doBezier extends Command {
    public doBezier(int line, String midPose, String endPose) {
        super(line, CommandType.BezTo, new String[]{midPose,endPose});
    }
    public doBezier(int line, String[] midPoses, String endPose) {
        super(line, CommandType.BezTo, constructPoses(midPoses, endPose));
    }
    private static String[] constructPoses(String[] midPoses, String endPos){
        ArrayList<String> poses = new ArrayList<>(List.of(midPoses));
        poses.add(endPos);
        return poses.toArray(new String[0]);
    }

    @Override
    public void run(){

    }
}
