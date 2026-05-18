package org.SquidSquad.CommandSequencer.Variables.Complex;

import org.SquidSquad.CommandSequencer.Variables.Variable;
import org.SquidSquad.CommandSequencer.Variables.VariableTypes;

import java.util.Map;

public class dynJson extends Variable {
    public dynJson(Map<Variable,Variable> leJson){
        super(VariableTypes.Json, leJson);
    }
    public dynJson(Map<Variable,Variable> leJson, String name){
        super(VariableTypes.Json, leJson, name);
    }
    // logic Ops
    @Override
    public boolean equals(Variable in){
        if (in.getType() != VariableTypes.Json) return false;
        // key check
        Map<Variable,Variable> inMap = (Map<Variable,Variable>)in;
        Map<Variable,Variable> map = (Map<Variable, Variable>)value;
        for (Variable key : map.keySet()){
            boolean cont = false;
            for (Variable key2 : inMap.keySet()){
                if (key.equals(key2)){
                    cont = true;
                }
            }
            if (!cont){
                return false;
            }
        }
        // value check
        for (Variable key : map.keySet()){
            if (!inMap.get(key).equals(map.get(key))){
                return false;
            }
        }
        return true;
    }
    // json/list shenanigans
    @Override
    public void append(Variable in, Variable id){
        ((Map<Variable, Variable>)value).put(id,in);
    }
    @Override
    public void remove(Variable id){
        ((Map<Variable, Variable>)value).remove(id);
    }
    @Override
    public void set(Variable id, Variable in){
        ((Map<Variable, Variable>)value).put(id,in);
    }
    @Override
    public Variable getFromID(Variable id){
        Map<Variable,Variable> map = (Map<Variable, Variable>)value;
        if (map.containsKey(id)){
            return map.get(id);
        } else {
            throwErr("getFromID", new Object[]{id}, "This Json does not contain given key!");
        }
        return null;
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
}
