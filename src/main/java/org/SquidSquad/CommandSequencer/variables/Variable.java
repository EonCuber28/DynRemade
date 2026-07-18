package org.SquidSquad.CommandSequencer.variables;

import org.SquidSquad.CommandSequencer.variables.complex.DynFieldCord;
import org.SquidSquad.CommandSequencer.variables.complex.DynJson;
import org.SquidSquad.CommandSequencer.variables.complex.DynList;
import org.SquidSquad.CommandSequencer.variables.primitives.DynBoolean;
import org.SquidSquad.CommandSequencer.variables.primitives.DynNumber;
import org.SquidSquad.CommandSequencer.variables.primitives.DynString;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Variable {
    // gotta do this dumb thing to make it work
    private static BiConsumer<Variable,Variable> setVar;
    private static Consumer<Variable> registerVar;
    public static void registerVarSettersGetters(BiConsumer<Variable,Variable> method, Consumer<Variable> method2){
        setVar = method;
        registerVar = method2;
    }
    public void setVariable(Variable in){
        setVar.accept(this, in);
    }
    public void registerVar(Variable in){
        registerVar.accept(in);
    }

    private VariableTypes type;
    protected Object value;
    private final boolean literal;
    private final String name;

    public Variable(VariableTypes type, Object value, String name){
        this.type = type;
        this.value = value;
        this.name = name;
        literal = false;
    }
    public Variable(VariableTypes type, Object value){
        this.type = type;
        this.value = value;
        this.name = "Literal";
        literal = true;
    }

    public String getName(){
        return name;
    }
    public boolean isLiteral(){
        return literal;
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
        if (this.literal){
            out = "Literal: ";
        }
        switch (this.type){
            case Number -> out = out+"Number";
            case String -> out = out+"String";
            case Boolean -> out = out+"Boolean";
            case List -> out = out+"List";
            case Json -> out = out+"Json";
            case FieldCord -> out = out+"FieldCord";
            case FieldPos -> out = out+"FieldPos";
            default -> out = out+"NULL (XTRA BAD)";
        }
        out = out+value;
        return out;
    }
    public String valueToString(){
        return value.toString();
    }
    
    // overridable operations
    public void Add(Variable in1, Variable in2){throwErr("Add",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Add(Variable in){throwErr("Add",new Object[]{in},"invalid method for var "+type);}
    
    public void Sub(Variable in1, Variable in2){throwErr("Sub",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Sub(Variable in){throwErr("Sub",new Object[]{in},"invalid method for var "+type);}
    
    public void Mux(Variable in1, Variable in2){throwErr("Mux",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Mux(Variable in){throwErr("Mux",new Object[]{in},"invalid method for var "+type);}
    
    public void Div(Variable in1, Variable in2){throwErr("Div",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Div(Variable in){throwErr("Div",new Object[]{in},"invalid method for var "+type);}
    
    public void Pow(Variable in1, Variable in2){throwErr("Pow",new Object[]{in1,in2},"invalid method for var "+type);}
    public void Pow(Variable in){throwErr("Pow",new Object[]{in},"invalid method for var "+type);}
    
    public void Sqrt(Variable in){throwErr("Sqrt",new Object[]{in},"invalid method for var "+type);}
    public void Sqrt(){throwErr("Sqrt", new Object[]{}, "invalid method for var "+type);}
    
    public void Sin(Variable in){throwErr("Sin",new Object[]{in},"invalid method for var "+type);}
    public void Sin(){throwErr("Sin", new Object[]{}, "invalid method for var "+type);}
    
    public void iSin(Variable in){throwErr("iSin",new Object[]{in},"invalid method for var "+type);}
    public void iSin(){throwErr("iSin", new Object[]{}, "invalid method for var "+type);}
    
    public void Cos(Variable in){throwErr("Cos",new Object[]{in},"invalid method for var "+type);}
    public void Cos(){throwErr("Cos", new Object[]{}, "invalid method for var "+type);}
    
    public void iCos(Variable in){throwErr("iCos",new Object[]{in},"invalid method for var "+type);}
    public void iCos(){throwErr("iCos", new Object[]{}, "invalid method for var "+type);}

    public void Tan(Variable in){throwErr("Tan",new Object[]{in},"invalid method for var "+type);}
    public void Tan(){throwErr("Tan", new Object[]{}, "invalid method for var "+type);}

    public void iTan(Variable in){throwErr("iTan",new Object[]{in},"invalid method for var "+type);}
    public void iTan(){throwErr("iTan", new Object[]{}, "invalid method for var "+type);}

    public void toDeg(Variable in){throwErr("toDeg",new Object[]{in},"invalid method for var "+type);}
    public void toDeg(){throwErr("toDeg", new Object[]{}, "invalid method for var "+type);}

    public void toRad(Variable in){throwErr("toRad",new Object[]{in},"invalid method for var "+type);}
    public void toRad(){throwErr("toRad",new Object[]{},"invalid method for var "+type);}
    
    public void Inc(){throwErr("Increment",new Object[]{},"invalid method for var "+type);}
    public void Dec(){throwErr("Decrement",new Object[]{},"invalid method for var "+type);}

    public boolean equals(Variable in){throwErr("equals",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean and(Variable in){throwErr("And",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean or(Variable in){throwErr("Or",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean not(){throwErr("Not",new Object[]{this},"invalid method for var "+type); return false;}

    public boolean lessThan(Variable in){throwErr("Less Than (<)",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean moreThan(Variable in){throwErr("More Than (>)",new Object[]{in},"invalid method for var "+type); return false;}

    public boolean lessThanEq(Variable in){throwErr("More Than Eqauls (>=)",new Object[]{in},"invalid method for var "+type); return false;}
    public boolean moreThanEq(Variable in){throwErr("More Than Equals (>=)",new Object[]{in},"invalid method for var "+type); return false;}
    // json/list ops (ts so cooked) executed as if THIS VAR is the out (if not that then we are the given list/json)
    //get
    // take json/listVar id/index outVar(this)
    public void set2get(Variable in, int index){throwErr("Get",new Object[]{in,index},"invalid method for var "+type);}
    public void set2get(Variable in, Variable id){throwErr("Get",new Object[]{in,id},"invalid method for var "+type);}
    // helper functions (can be overrided) for lists and arrays
    public Variable getFromID(Variable id){throwErr("Get", new Object[]{id}, "method not overridden"); return null;}
    public Variable getFromIndex(int index){throwErr("Get",new Object[]{index},"invalid method for var "+type); return null;}
    //insert
    // take inVar id/index targetList/json
    public void insertVar(Variable in, int index){throwErr("Insert",new Object[]{in,index},"invalid method for var "+type);}
    public void insertVar(Variable in, Variable id){throwErr("Insert",new Object[]{in,id},"invalid method for var "+type);}
    //append
    // take inVar listVar
    // take inVar id listVar
    public void append(Variable in){throwErr("Append",new Object[]{in},"invalid method for var "+type);}
    public void append(Variable in, Variable id){throwErr("Append",new Object[]{in,id},"invalid method for var "+type);}
    //remove
    // take index/id list/jsonVar
    public void remove(int index){throwErr("Remove",new Object[]{index},"invalid method for var "+type);}
    public void remove(Variable id){throwErr("Remove",new Object[]{id},"invalid method for var "+type);}
    // take index/id list/jsonVar outVar
    public void setToRemove(Variable list, int index){throwErr("Set",new Object[]{list,index},"invalid method for var "+type);}
    public void setToRemove(Variable json, Variable id){throwErr("Set",new Object[]{json,id},"invalid method for var "+type);}
    //set
    // take id/index inVar
    public void set(int index, Variable in){throwErr("Set",new Object[]{index,in},"invalid method for var "+type);}
    public void set(Variable id, Variable in){throwErr("Set",new Object[]{id,in},"invalid method for var "+type);}

    // setters
    public Variable getClone(Variable toClone){
        Variable clone = null;
        switch (toClone.getType()){
            case FieldCord -> {
                if (toClone.isLiteral()){
                    double[] litVal = {
                            (double)((Variable[])toClone.getValue())[0].getValue(),
                            (double)((Variable[])toClone.getValue())[1].getValue()};
                    clone = new DynFieldCord(litVal,toClone.getName());
                } else {
                    // TODO: for any associated values, if the value is literal, clone it.
                    clone = new DynFieldCord((Variable[])toClone.getValue());
                }
            }
            case FieldPos -> {
                if (toClone.isLiteral()){
                    double[] litVal = {
                            (double)((Variable[])toClone.getValue())[0].getValue(),
                            (double)((Variable[])toClone.getValue())[1].getValue(),
                            (double)((Variable[])toClone.getValue())[2].getValue()};
                    clone = new DynFieldCord(litVal,toClone.getName());
                } else {
                    // TODO: same here
                    clone = new DynFieldCord((Variable[])toClone.getValue());
                }
            }
            case Boolean -> {
                if (toClone.isLiteral()){
                    clone = new DynBoolean((boolean)toClone.getValue());
                } else {
                    clone = new DynBoolean((boolean)toClone.getValue(),toClone.getName());
                }
            }
            case Number -> {
                if (toClone.isLiteral()){
                    clone = new DynNumber((double)toClone.getValue());
                } else {
                    clone = new DynNumber((double)toClone.getValue(),toClone.getName());
                }
            }
            case String -> {
                if (toClone.isLiteral()){
                    clone = new DynString((String)toClone.getValue());
                } else {
                    clone = new DynString(toClone.getName(),(String)toClone.getValue());
                }
            }
            case List -> {
                if (toClone.isLiteral()){
                    clone = new DynList((ArrayList<Variable>)toClone.getValue());
                } else {
                    // TODO: and here
                    clone = new DynList((ArrayList<Variable>)toClone.getValue(),toClone.getName());
                }
            }
            case Json -> {
                if (toClone.isLiteral()){
                    clone = new DynJson((Map<Variable, Variable>)toClone.getValue());
                } else {
                    // TODO: this too
                    clone = new DynJson((Map<Variable, Variable>)toClone.getValue(),toClone.getName());
                }
            }
        }
        return clone;
    }
    public Variable getClone(){
        return getClone(this);
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

    // kinda a thing i made to express to DYN programmers if they are doing a bad to thing to a var. (should primarily trigger for null type variables, which should never happen)
    protected void throwErr(String method, Object[] vars, String reason){
        StringBuilder involvedVars = new StringBuilder();
        for (int i = 0; i < vars.length; i++){
            involvedVars.append(vars[i]);
            if (!(i == vars.length-1)){
                involvedVars.append(", ");
            }
        }
        throw new VariableException(method, involvedVars.toString(), reason);
    }
}
