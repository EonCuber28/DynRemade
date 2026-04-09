package org.SquidSquad.Tokenizer;

// dis a lot a things to take care of
public enum TokenTypes {
    // single char tokens
    Lparenth, Rparenth, Comma,
    Lbraket, Rbraket,
    LCbraket,RCbraket,
    // math commands
    Add,Sub,Mux,Div,
    Pow,Sqrt,Sin,iSin,
    Cos,iCos,Tan,iTan,
    toRad,toDeg,
    Increment, Decrement,
    // variables
    Number,Bool,String,
    List,Json,FieldCord,
    FieldPos,
    // logical Ops
    Equals,NotEqual,isMore,
    isLess,And,Or,Not,
    // movement ops
    TurnTo,GoTo,DoPez, // DoPez is the bezier command
    // func/loop/if
    DefPath,Run,While,For,If,
    // telemetry
    AddData,Update,Clear,
    // literals
    Literal, Name, // literal = value      Name=name (dummy)
    // random commands
    RngFloat,RngDouble,
    RngInteger,RngBoolean,
    // extra
    Start,End,PathStartPos,
    Cmd,To,MainPathFunc
}