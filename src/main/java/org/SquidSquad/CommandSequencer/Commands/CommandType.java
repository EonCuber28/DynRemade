package org.SquidSquad.CommandSequencer.Commands;

public enum CommandType {
    // control flow
    For,If,While,
    // Function
    jFunc, RunPath, DynPath,
    // math
    //arithmetic
    Add,Decrement,Div,
    Increment,Mux,Pow,
    Sqrt,Sub,
    //trig
    Cos,iCos,iSin,iTan,
    Sin,Tan,toDeg,toRad,
    // movement
    SplineTo,GoTo,TurnTo,
    // random
    RngBoolean,RngDouble,
    RngFloat,RngInteger,
    // telemetry
    AddData,Clear,Update,
    // variables
    SetVar
}
