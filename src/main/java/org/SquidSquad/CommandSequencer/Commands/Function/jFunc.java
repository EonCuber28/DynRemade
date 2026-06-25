package org.SquidSquad.CommandSequencer.Commands.Function;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.CommandException;
import org.SquidSquad.CommandSequencer.Commands.CommandType;
import org.SquidSquad.CommandSequencer.Variables.Variable;

import java.util.function.Function;

public class jFunc extends Command {
    public enum funcType{
        I,IO,O,S // for input only, Input and output, output only, and static (no args)
    }
    private funcType type;
    private Function inFunc;
    public jFunc(int line, Function inFunc){
        super(line, CommandType.jFunc,"");
        type = funcType.S;
        this.inFunc = inFunc;
    }
    public jFunc(int line, Function inFunc, String Var, boolean IeO){
        super(line,CommandType.jFunc,"");
        this.inFunc = inFunc;
        if (IeO){
            super.InVarIDs = new String[]{Var};
            type = funcType.I;
        } else {
            super.OutVarID = Var;
            type = funcType.O;
        }
    }
    public jFunc(int line, Function<Variable,Variable> inFunc, String inVar, String outVar){
        super(line,CommandType.jFunc,new String[]{inVar},outVar);
        this.inFunc = inFunc;
        type = funcType.IO;
    }

    @Override
    public void run(){
        try {
            switch (type) {
                case S -> inFunc.apply(null);
                case I -> inFunc.apply(super.varManager.getVar(super.getInVarIDs()[0]));
                case O -> super.varManager.getVar(super.getOutVarID()).setVariable((Variable) inFunc.apply(null));
                case IO -> super.varManager.getVar(super.getOutVarID()).setVariable((Variable) inFunc.apply(super.varManager.getVar(super.getInVarIDs()[0])));
            }
        } catch (RuntimeException e) {
            throw new CommandException(line,"jFunc","we cannot call the jFunc because: "+e);
        }
    }
}
