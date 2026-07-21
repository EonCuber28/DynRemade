package org.SquidSquad.CommandSequencer.Commands.variables;

import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.variables.Variable;
import org.SquidSquad.CommandSequencer.variables.VariableTypes;
import org.SquidSquad.CommandSequencer.variables.primitives.DynBoolean;
import org.SquidSquad.CommandSequencer.variables.primitives.DynNumber;
import org.SquidSquad.CommandSequencer.variables.primitives.DynString;
import org.SquidSquad.Tokenizer.Token;

public class Get extends Command{
    private final String in;
    private final String out;
    private final Token InDex;
    public Get(int line, String in, Token InDex, String out){
        super(line, CommandType.Get, new String[]{in,String.valueOf(InDex.getValue())},out);
        this.in = in;
        this.out = out;
        this.InDex = InDex;
    }

    public void run(){
        super.run();
        Variable target = varManager.getVar(in);
        if (target == null){
            throw new CommandException(line,"Set","Variable "+in+" is not defined!");
        }
        else {
            VariableTypes targetType = target.getType();
            if (targetType != VariableTypes.FieldCord &&
                targetType != VariableTypes.FieldPos &&
                targetType != VariableTypes.List &&
                targetType != VariableTypes.Json){
                throw new CommandException(line,"Set","Cannot use non FieldCord/FieldPos/List/Json for this operation!");
            }
        }
        Variable out = varManager.getVar(this.out);
        if (out == null){
            throw new CommandException(line,"Set","Variable "+this.out+" is not defined!");
        }
        else {
            VariableTypes targetType = out.getType();
            if (targetType != VariableTypes.FieldCord &&
                targetType != VariableTypes.FieldPos &&
                targetType != VariableTypes.List &&
                targetType != VariableTypes.Json){
                throw new CommandException(line,"Set","Cannot use non FieldCord/FieldPos/List/Json for this operation!");
            }
        }
        switch(InDex.type()){
            case Boolean -> out.set2get(target,new DynBoolean((boolean)InDex.getValue()));
            case Number -> out.set2get(target,new DynNumber((double)InDex.getValue()));
            case String -> out.set2get(target,new DynString((String)InDex.getValue()));
            case Name -> {
                Variable id = varManager.getVar((String)InDex.getValue());
                if (id == null) throw new CommandException(line,"Get","Variable "+InDex.getValue()+" not defined!");
                out.set2get(target,id);
            }
            default -> throw new CommandException(line,"Get","Expected a boolean/number/string/name | Got: "+InDex.type());
        }
    }
}
