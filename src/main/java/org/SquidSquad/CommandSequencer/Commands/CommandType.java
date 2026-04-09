package org.SquidSquad.CommandSequencer.Commands;

public enum CommandType {
    // control flow
    For,If,While,
    // Etc
    jFunc, RunPath,
    // math
    //arithmetic
    Add,Decrement,Div,
    Increment,Mux,Pow,
    Sqrt,Sub,
    //trig
    Cos,iCos,iSin,iTan,
    Sin,Tan,toDeg,toRad,
    // movement
    Bezier,GoTo,TurnTo,
    // random
    RngBoolean,RngDouble,
    RngFloat,RngInteger,
    // telemetry
    AddData,Clear,Update,
    // variables
    SetVar
}
