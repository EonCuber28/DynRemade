package org.SquidSquad.CommandSequencer;

import org.SquidSquad.CommandSequencer.Commands.Command;
import org.SquidSquad.CommandSequencer.Commands.Function.RunPath;
import org.SquidSquad.Tokenizer.Token;
import org.SquidSquad.Tokenizer.TokenTypes;

import java.util.ArrayList;

import static org.SquidSquad.Tokenizer.TokenTypes.Name;

// something to note about this process is that most of the errors
// that we throw here are going to be the most common errors
// for a prog to see while developing with dyn.
public class CommandConstructor {
    private String mainFuncName = "Main";
    private String[] ogFileLines;
    private Token[] givenTokens;
    private ArrayList<Command> depthTracker;
    private ArrayList<Command> finalCommands;
    private int i;

    public CommandConstructor(String ogFile){
        CommandException.linkFile(ogFile);
        i = 0;
        depthTracker = new ArrayList<>();
        finalCommands = new ArrayList<>();
    }

    public void processTokens(Token[] tokenstream){
        givenTokens = tokenstream;
        while (i < tokenstream.length){
            delegateToken();
        }
    }
    private void delegateToken() {
        Token current = givenTokens[i];
        // we only care about the "starters" of the token stream
        switch (current.type()) {
            case Add, Sub, Mux, Div,
                 Pow, Sqrt, Sin, iSin,
                 Cos, iCos, Tan, iTan,
                 toRad, toDeg,
                 Increment, Decrement -> processMathOp();
            case Number, Bool, String, List,
                 Json, FieldCord, FieldPos -> processVariable();
            case Get, Insert, Append,
                 Remove, Set -> processListJsonOp();
            case TurnTo, GoTo,
                 doBez,followSpline,
                 followSplineLinear,
                 followsplineSpline-> processMoveOp();
            case DefPath, While, For, If -> processFuncLoopIf();
            case AddData, Update, Clear -> processTelemetry();
            case RngBoolean, RngDouble,
                 RngInteger, RngFloat -> processRandomOp();
            case PathStartPos -> processPathStart();
            case Cmd -> processCommand();
            case MainPathFunc -> processMainPathFunc();
            case Run -> processRun();
            case End -> processEnd();
        }
    }

    private void processRun(){
        int line = givenTokens[i].getLine();
        i++;
        if (givenTokens[i].type() == Name){
            String pathID = String.valueOf(givenTokens[i].getValue());
            addCommand(new RunPath(line,pathID));
        } else {
            throwError("Cannot use non-name input for Run Command!");
        }
        i++;
    }
    private void processEnd(){
        i++;
        if (depthTracker.size() == 1) {
            finalCommands.add(depthTracker.removeLast());
        } else if (!depthTracker.isEmpty()){
            Command currentDepth = depthTracker.getLast();
            depthTracker.removeLast();
            depthTracker.getLast().addCommand(currentDepth);
        }
    }
    private void processMathOp(){
        Token current = givenTokens[i];
        switch (current.type()){
            // 1-2 IO ops
            case Sqrt,Sin,iSin,
                 Cos,iCos,Tan,
                 iTan -> {
            }
            // 2-3 IO ops
            case Pow,Div,Mux,
                 Sub,Add -> {
            }
        }
    }
    private void processMoveOp(){
        Token current = givenTokens[i];
        switch (current.type()){
        }
    }
    private void processCommand(){}
    private void processVariable(){}
    private void processRandomOp(){}
    private void processPathStart(){}
    private void processTelemetry(){}
    private void processCondition(){}
    private void processListJsonOp(){}
    private void processFuncLoopIf(){}
    private void processMainPathFunc(){}

    private void addCommand(Command c){
        depthTracker.getLast().addCommand(c);
    }
    private boolean nextTksMeetType(TokenTypes type1){
        return true;
    }
    private boolean nextTksMeetType(TokenTypes type1, TokenTypes type2){
        return true;
    }
    private boolean nextTksMeetType(TokenTypes type1, TokenTypes type2, TokenTypes type3){
        return true;
    }
    private boolean nextTksMeetType(TokenTypes type1, TokenTypes type2, TokenTypes type3, TokenTypes type4){
        return true;
    }

    private void throwError(String reason) {
        Token current = givenTokens[i];
        int line = current.getLine();
        int column = current.getColumn();
        throw new CommandException(line,column,reason);
    }
}