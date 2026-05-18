package org.SquidSquad.CommandSequencer.Variables.Complex;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

import java.lang.reflect.Array;
import java.time.temporal.ValueRange;
import java.util.ArrayList;

public class dynList extends Variable {
    public dynList(ArrayList<Variable> leList){
        super(VariableTypes.List, leList);
    }
    public dynList(ArrayList<Variable> leList, String name){
        super(VariableTypes.List, leList, name);
    }
    // logic Ops
    @Override
    public boolean equals(Variable in){
        if (in.getType() == VariableTypes.List){
            ArrayList<Variable> inList = (ArrayList<Variable>)in.getValue();
            ArrayList<Variable> thisList = (ArrayList<Variable>)value;
            if (inList.size() == thisList.size()){
                for (int i = 0; i < inList.size(); i++){
                    if (!inList.get(i).equals(thisList.get(i))){
                        return false; // this is when I reject "never nester" mentality.
                    }
                }
                return true;
            }
        }
        return false;
    }
    // json/list shenanigans
    @Override
    public void set2get(Variable in, int index){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in).getClone());
        } else {
            throwErr("set2get", new Object[]{in}, "Given Json is not a Json, it is: "+in.getType());
        }
    }
    @Override
    public void set2get(Variable in, Variable id){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in).getClone());
        } else {
            throwErr("set2get", new Object[]{in,id}, "Given Json is not a Json, it is: "+in.getType());
        }
    }
    @Override
    public void set(int index, Variable in){
        ((ArrayList<Variable>)value).set(index, in);
    }
    @Override
    public void setToRemove(Variable list, int index){
        setVariable(list.getFromIndex(index).getClone());
        list.remove(index);
    }
    @Override
    public void setToRemove(Variable json, Variable id){
        setVariable(json.getFromID(id).getClone());
        json.remove(id);
    }
    @Override
    public Variable getFromIndex(int index){
        ArrayList<Variable> thisList = (ArrayList<Variable>)value;
        if (index < thisList.size() && index >= 0){
            return thisList.get(index);
        }
        throwErr("getFormIndex", new Object[]{index}, "Out of given list boundries index: "+index);
        return null;
    }
    @Override
    public void insertVar(Variable in, int index){
        ((ArrayList<Variable>)value).set(index,in);
    }
    @Override
    public void append(Variable in){
        ((ArrayList<Variable>)value).add(in);
    }
    @Override
    public void remove(int index){
        ((ArrayList<Variable>)value).remove(index);
    }
}
