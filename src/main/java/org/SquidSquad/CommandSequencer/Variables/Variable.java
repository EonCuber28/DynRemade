package org.SquidSquad.CommandSequencer.Variables;

import java.util.ArrayList;
import java.util.Map;

public class Variable {
    private VariableTypes type;
    private Object value;

    public Variable(VariableTypes type, Object value){
        this.type = type;
        this.value = value;
    }

    public Object getValue(){
        return value;
    }
    public VariableTypes getType(){
        return type;
    }

    public String toString(){
        // type: value
        String out = "";
        switch (this.type){
            case Number -> out = "Number";
            case String -> out = "String";
            case Boolean -> out = "Boolean";
            case List -> out = "List";
            case Json -> out = "Json";
            case FieldCord -> out = "FieldCord";
            case FieldPos -> out = "FieldPos";
            default -> out = "NULL (XTRA BAD)";
        }
        out = out+value;
        return out;
    }
    
    // overridable operations
    public boolean Add(Variable in1, Variable in2){return false;} // dear future me: the reason that all of these return a boolean is to determine if the operation was successful, if it was not then we throw an error.
    public boolean Add(Variable in){return false;}
    
    public boolean sub(Variable in1, Variable in2){return false;}
    public boolean sub(Variable in){return false;}
    
    public boolean mux(Variable in1, Variable in2){return false;}
    public boolean mux(Variable in){return false;}
    
    public boolean div(Variable in1, Variable in2){return false;}
    public boolean div(Variable in){return false;}
    
    public boolean pow(Variable in1, Variable in2){return false;}
    public boolean pow(Variable in){return false;}
    
    public boolean sqrt(Variable in){return false;}
    public boolean sqrt(){return false;}
    
    public boolean sin(Variable in){return false;}
    public boolean sin(){return false;}
    
    public boolean iSin(Variable in){return false;}
    public boolean iSin(){return false;}
    
    public boolean cos(Variable in){return false;}
    public boolean cos(){return false;}
    
    public boolean iCos(Variable in){return false;}
    public boolean iCos(){return false;}

    public boolean tan(Variable in){return false;}
    public boolean tan(){return false;}

    public boolean iTan(Variable in){return false;}
    public boolean iTan(){return false;}

    public boolean toDeg(Variable in){return false;}
    public boolean toDeg(){return false;}

    public boolean toRad(Variable in){return false;}
    public boolean toRad(){return false;}
    
    public boolean inc(){return false;}
    public boolean dec(){return false;}

    public boolean equals(Variable in){return false;}
    public boolean and(Variable in){return false;}
    public boolean or(Variable in){return false;}
    public boolean not(){return false;}

    public boolean lessThan(Variable in){return false;}
    public boolean moreThan(Variable in){return false;}

    public void setVariable(Variable value){
        this.type = value.getType();
        this.value = value.getValue();
    }
    public void setValue(ArrayList<Variable> in){
        this.type = VariableTypes.List;
        this.value = in;
    }
    public void setValue(Map<Variable,Variable> in){
        this.type = VariableTypes.Json;
        this.value = in;
    }
    public void setValue(String in){
        this.type = VariableTypes.String;
        this.value = in;
    }
    public void setValue(boolean in){
        this.type = VariableTypes.Boolean;
        this.value = in;
    }
    public void setValue(double in){
        this.type = VariableTypes.Number;
        this.value = in;
    }
    public void setValue(double[] in){
        if (in.length == 2) {
            this.type = VariableTypes.FieldCord;
            this.value = in;
        } else if (in.length == 3){
            this.type = VariableTypes.FieldPos;
            this.value = in;
        }
    }
}
