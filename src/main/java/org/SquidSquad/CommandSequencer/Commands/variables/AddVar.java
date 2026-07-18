package org.SquidSquad.CommandSequencer.Commands.variables;

import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.variables.complex.DynFieldCord;
import org.SquidSquad.CommandSequencer.variables.complex.DynFieldPos;
import org.SquidSquad.CommandSequencer.variables.complex.DynJson;
import org.SquidSquad.CommandSequencer.variables.complex.DynList;
import org.SquidSquad.CommandSequencer.variables.primitives.DynBoolean;
import org.SquidSquad.CommandSequencer.variables.primitives.DynNumber;
import org.SquidSquad.CommandSequencer.variables.primitives.DynString;
import org.SquidSquad.CommandSequencer.variables.Variable;
import org.SquidSquad.CommandSequencer.variables.VariableTypes;

import java.util.ArrayList;
import java.util.Map;

public class AddVar extends Command {
    private String varName;
    private final VariableTypes variableType;
    private final Object value;

    public AddVar(int line, String name, VariableTypes type, Object value){
        super(line, CommandType.AddVar, new String[0]);
        this.varName = name;
        this.variableType = type;
        this.value = value;
    }
    public AddVar(int line, VariableTypes type, Object value){
        super(line, CommandType.AddVar, new String[0]);
        this.variableType = type;
        this.value = value;
    }

    @Override
    public void run(){
        Variable newVar = null;
        switch(variableType){
            case Number -> {
                if (value instanceof Double || value instanceof Integer || value instanceof Float || value instanceof Long){
                    if (varName != null) newVar = new DynNumber((double)value,varName);
                    else newVar = new DynNumber((double)value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            case String -> {
                if (value instanceof String){
                    if (varName != null) newVar = new DynString((String)value,varName);
                    else newVar = new DynString((String)value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            case Boolean -> {
                if (value instanceof Boolean){
                    if (varName != null) newVar = new DynBoolean((boolean)value,varName);
                    else newVar = new DynBoolean((boolean)value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            case List -> {
                if (value instanceof ArrayList<?>){
                    if (varName != null) newVar = new DynList((ArrayList<Variable>)value,varName);
                    else newVar = new DynList((ArrayList<Variable>)value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            case Json -> {
                if (value instanceof Map<?,?>){
                    if (varName != null) newVar = new DynJson((Map<Variable, Variable>)value,varName);
                    else newVar = new DynJson((Map<Variable, Variable>)value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            case FieldCord -> {
                if (value instanceof Object[]) {
                    if (((Object[])value)[0] instanceof Double){
                        if (((Object[])value)[1] instanceof Double){
                            if (varName != null) newVar = new DynFieldCord(new double[]{(double)((Object[])value)[0], (double)((Object[])value)[1]},varName);
                            else newVar = new DynFieldCord(new double[]{(double)((Object[])value)[0], (double)((Object[])value)[1]});
                        } else {
                            if (varName != null) newVar = new DynFieldCord(new Variable[]{new DynNumber((double)((Object[])value)[0]), varManager.getVar((String)((Object[])value)[1])},varName);
                            else newVar = new DynFieldCord(new Variable[]{new DynNumber((double)((Object[])value)[0]), varManager.getVar((String)((Object[])value)[1])});
                        }
                    } else{
                        if (((Object[])value)[1] instanceof Double){
                            if (varName != null) newVar = new DynFieldCord(new Variable[]{varManager.getVar((String)((Object[])value)[0]), new DynNumber((double)((Object[])value)[1])},varName);
                            else newVar = new DynFieldCord(new Variable[]{varManager.getVar((String)((Object[])value)[0]), new DynNumber((double)((Object[])value)[1])},varName);
                        } else {
                            if (varName != null) newVar = new DynFieldCord(new Variable[]{varManager.getVar((String)((Object[])value)[0]),varManager.getVar((String)((Object[])value)[1])},varName);
                            else newVar = new DynFieldCord(new Variable[]{varManager.getVar((String)((Object[])value)[0]),varManager.getVar((String)((Object[])value)[1])},varName);
                        }
                    }
                } else if (value instanceof double[]){
                    if (varName != null) newVar = new DynFieldCord((double[]) value, varName);
                    else newVar = new DynFieldCord((double[]) value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            case FieldPos -> {
                if (value instanceof Variable[]) {
                    if (varName != null) newVar = new DynFieldPos((Variable[]) value, varName);
                    else newVar = new DynFieldPos((Variable[]) value);
                } else if (value instanceof double[]){
                    if (varName != null) newVar = new DynFieldPos((double[]) value, varName);
                    else newVar = new DynFieldCord((double[]) value);
                } else {
                    throw new CommandException(line, "AddVar", "Given value is not a json!"); // I think that its literally impossible for this to fire
                }
            }
            default -> throw new CommandException(line, "AddVar", "Unknown variable type: "+variableType);
        }
        varManager.registerVar(newVar); // we just gotta hope and pray to god that the sun doesn't blast an unlucky robot controller and uppin deletes the type enum.
    }
}
