package org.SquidSquad.CommandSequencer.Variables.Primitives;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableException;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

import java.util.ArrayList;
import java.util.Map;

public class dynBoolean extends Variable {
    public dynBoolean(boolean state){
        super(VariableTypes.Boolean, state);

    }
    public dynBoolean(boolean state, String name){
        super(VariableTypes.Boolean,state,name);
    }

    private void catchIncompatible(Variable in, String operation){
        if (in.getType() != VariableTypes.Boolean){
            throwErr(operation,new Object[]{in}, "Cannot use variable type: "+in.getType());
        }
    }

    // logic Ops
    @Override
    public boolean equals(Variable in){
        switch (in.getType()){
            case Boolean -> {
                return ((boolean)in.getValue() == (boolean)value);
            }
            case String -> {
                return String.valueOf(in.getValue()).equals(String.valueOf(value));
            }
        }
        return false;
    }
    @Override
    public boolean and(Variable in){
        catchIncompatible(in, "And");
        return ((boolean)in.getValue() && (boolean)value);
    }
    @Override
    public boolean or(Variable in){
        catchIncompatible(in, "Or");
        return ((boolean)in.getValue() || (boolean)value);
    }
    @Override
    public boolean not(){
        return !(boolean)value;
    }
    // json/list shenanigans
    @Override
    public void set2get(Variable in, int index){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in));
        } else {
            throwErr("set2get", new Object[]{in}, "Given Json is not a Json, it is: "+in.getType());
        }
    }
    @Override
    public void set2get(Variable in, Variable id){
        if (in.getType() == VariableTypes.Json){
            setVariable(in.getFromID(in));
        } else {
            throwErr("set2get", new Object[]{in,id}, "Given Json is not a Json, it is: "+in.getType());
        }
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
}
