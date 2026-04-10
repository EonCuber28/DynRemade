package org.SquidSquad.CommandSequencer.Commands.ControlFlow;

import org.SquidSquad.CommandSequencer.VariableManager;

public class Condition {
    private VariableManager varMan;

    public enum ConditionType{
        And,Or,           // both parts are Conditions
        Equals,NotEquals, // both parts are Conditions/booleans
        MoreThan,LessThan,// both parts are Variable Number
        Constant}         // in1 is used as this value
    private ConditionType type;

    private enum InTypes{bool,var,condition}
    private Object Part1;
    private InTypes Part1Type;

    private Object Part2;
    private InTypes Part2Type;

    private void setCondition(String condition){
        switch (condition){
            case "And" -> type = ConditionType.And;
            case "Or" -> type = ConditionType.Or;
            case "Equals" -> type = ConditionType.Equals;
            case "NotEquals" -> type = ConditionType.NotEquals;
            case "MoreThan" -> type = ConditionType.MoreThan;
            case "LessThan" -> type = ConditionType.LessThan;
            case "Constant" -> type = ConditionType.Constant;
            default -> type = null;
        }
    }

    // for variable/boolean inputs
    public Condition(VariableManager varMan, String opType, boolean in1, boolean in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.bool;
        Part2Type = InTypes.bool;
    }
    public Condition(VariableManager varMan, String opType, String in1, boolean in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.var;
        Part2Type = InTypes.bool;
    }
    public Condition(VariableManager varMan, String opType, boolean in1, String in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.bool;
        Part2Type = InTypes.var;
    }
    public Condition(VariableManager varMan, String opType, String in1, String in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.var;
        Part2Type = InTypes.var;
    }

    // for condition based inputs
    public Condition(VariableManager varMan, String opType, Condition in1, Condition in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.condition;
        Part2Type = InTypes.condition;
    }
    public Condition(VariableManager varMan, String opType, boolean in1, Condition in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.bool;
        Part2Type = InTypes.condition;
    }
    public Condition(VariableManager varMan, String opType, Condition in1, boolean in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.condition;
        Part2Type = InTypes.bool;
    }
    public Condition(VariableManager varMan, String opType, String in1, Condition in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.var;
        Part2Type = InTypes.condition;
    }
    public Condition(VariableManager varMan, String opType, Condition in1, String in2){
        this.varMan = varMan;
        Part1 = in1;
        Part2 = in2;
        setCondition(opType);
        Part1Type = InTypes.condition;
        Part2Type = InTypes.var;
    }

    public Condition(VariableManager varMan, boolean in){
        this.varMan = varMan;
        Part1 = in;
        Part1Type = InTypes.bool;
        setCondition("Constant");
    }
    public Condition(VariableManager varMan, String in){
        this.varMan = varMan;
        Part1 = in;
        Part1Type = InTypes.var;
        setCondition("Constant");
    }
    // process parts
    public boolean getResult(){ // i know for a FACT that there is going to be issues in this
        Boolean result = null;
        switch (type){
            case Or -> {
                switch (Part1Type){
                    case var -> {
                        switch (Part2Type){ // ts so ass
                            case var -> result = (varMan.getVar((String) Part1).equals(true) || varMan.getVar((String) Part2).equals(true));
                            case bool -> result = (varMan.getVar((String) Part1).equals(true) || (boolean)Part2);
                            case condition -> result = (varMan.getVar((String) Part1).equals(true) || ((Condition)Part2).getResult());
                        }
                    }
                    case bool -> {
                        switch (Part2Type){
                            case var -> result = ((boolean)Part1 || varMan.getVar((String)Part2).equals(true));
                            case bool -> result = ((boolean)Part1 || (boolean)Part2);
                            case condition -> result = ((boolean)Part1 || ((Condition)Part2).getResult());
                        }
                    }
                    case condition -> {
                        switch (Part2Type){
                            case var -> result = (((Condition)Part1).getResult() || varMan.getVar((String)Part2).equals(true));
                            case bool -> result = (((Condition)Part1).getResult() || (boolean)Part2);
                            case condition -> result = (((Condition)Part1).getResult() || ((Condition)Part2).getResult());
                        }
                    }
                }
            }
            case And -> {
                switch (Part1Type){
                    case var -> {
                        switch (Part2Type){ // ts so ass
                            case var -> result = (varMan.getVar((String) Part1).equals(true) && varMan.getVar((String) Part2).equals(true));
                            case bool -> result = (varMan.getVar((String) Part1).equals(true) && (boolean)Part2);
                            case condition -> result = (varMan.getVar((String) Part1).equals(true) && ((Condition)Part2).getResult());
                        }
                    }
                    case bool -> {
                        switch (Part2Type){
                            case var -> result = ((boolean)Part1 && varMan.getVar((String)Part2).equals(true));
                            case bool -> result = ((boolean)Part1 && (boolean)Part2);
                            case condition -> result = ((boolean)Part1 && ((Condition)Part2).getResult());
                        }
                    }
                    case condition -> {
                        switch (Part2Type){
                            case var -> result = (((Condition)Part1).getResult() && varMan.getVar((String)Part2).equals(true));
                            case bool -> result = (((Condition)Part1).getResult() && (boolean)Part2);
                            case condition -> result = (((Condition)Part1).getResult() && ((Condition)Part2).getResult());
                        }
                    }
                }
            }
            case Equals -> {
                switch (Part1Type){
                    case var -> {
                        switch (Part2Type){ // ts so ass
                            case var -> result = (varMan.getVar((String) Part1).equals(varMan.getVar((String) Part2)));
                            case bool -> result = (varMan.getVar((String) Part1).equals(Part2));
                            case condition -> result = (varMan.getVar((String) Part1).equals(((Condition)Part2).getResult()));
                        }
                    }
                    case bool -> {
                        switch (Part2Type){
                            case var -> result = ((boolean)Part1 == varMan.getVar((String)Part2).equals(true));
                            case bool -> result = ((boolean)Part1 == (boolean)Part2);
                            case condition -> result = ((boolean)Part1 == ((Condition)Part2).getResult());
                        }
                    }
                    case condition -> {
                        switch (Part2Type){
                            case var -> result = (((Condition)Part1).getResult() == varMan.getVar((String)Part2).equals(true));
                            case bool -> result = (((Condition)Part1).getResult() == (boolean)Part2);
                            case condition -> result = (((Condition)Part1).getResult() == ((Condition)Part2).getResult());
                        }
                    }
                }
            }
            case NotEquals -> {
                switch (Part1Type){
                    case var -> {
                        switch (Part2Type){ // ts so ass
                            case var -> result = !(varMan.getVar((String) Part1).equals(varMan.getVar((String) Part2)));
                            case bool -> result = !(varMan.getVar((String) Part1).equals(Part2));
                            case condition -> result = !(varMan.getVar((String) Part1).equals(((Condition)Part2).getResult()));
                        }
                    }
                    case bool -> {
                        switch (Part2Type){
                            case var -> result = ((boolean)Part1 != varMan.getVar((String)Part2).equals(true));
                            case bool -> result = ((boolean)Part1 != (boolean)Part2);
                            case condition -> result = ((boolean)Part1 != ((Condition)Part2).getResult());
                        }
                    }
                    case condition -> {
                        switch (Part2Type){
                            case var -> result = (((Condition)Part1).getResult() != varMan.getVar((String)Part2).equals(true));
                            case bool -> result = (((Condition)Part1).getResult() != (boolean)Part2);
                            case condition -> result = (((Condition)Part1).getResult() != ((Condition)Part2).getResult());
                        }
                    }
                }
            }
            case LessThan -> {
                switch (Part1Type){
                    case var -> {
                        switch (Part2Type){
                            case var -> result = varMan.getVar((String)Part1).lessThan(varMan.getVar((String)Part2));
                        }
                    }
                }
            }
            case MoreThan -> {
                switch (Part1Type){
                    case var -> {
                        switch (Part2Type){
                            case var -> result = varMan.getVar((String)Part1).moreThan(varMan.getVar((String)Part2));
                        }
                    }
                }
            }
            case Constant -> {
                switch (Part1Type){
                    case var -> result = varMan.getVar((String)Part1).equals(true);
                    case bool -> result = (boolean)Part1;
                    case condition -> result = ((Condition)Part1).getResult();
                }
            }
        }
        if (result == null){
            String opType;
            String part1type;
            String part2type;
            switch (type){
                case MoreThan -> opType = "More Than";
                case Constant -> opType = "Constant";
                case Equals -> opType = "Equals";
                case And -> opType = "And";
                case Or -> opType = "Or";
                case LessThan -> opType = "Less Than";
                case NotEquals -> opType = "Not Equals";
                default -> opType = "null (BAD)";
            }
            switch (Part1Type){
                case condition -> part1type = "Condition";
                case var -> part1type = "Variable";
                case bool -> part1type = "Boolean";
                default -> part1type = "Null (BAD)";
            }
            switch (Part2Type){
                case bool -> part2type = "Boolean";
                case var -> part2type = "Variable";
                case condition -> part2type = "Condition";
                default -> part2type = "Null (OK)";
            }
            throw new ConditionException("Unable to determine operation "+opType+" on type "+part1type+" and "+part2type);
        }
        return result;
    }
}

class ConditionException extends RuntimeException{
    public ConditionException(String message){
        super(message);
    }
}
