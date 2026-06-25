package org.SquidSquad.Tokenizer;

// dis a lot a things to take care of
public enum TokenTypes {
    // single char tokens
    Lparenth, Rparenth, Comma,
    Lbraket, Rbraket,
    LCbraket,RCbraket,
    Colon,
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
    // list/json Ops
    Get,Insert,Append,
    Remove,Set,
    // logical Ops
    Equals,NotEqual,isMore,
    isMoreEqual,isLessEqual,
    isLess,And,Or,Not,
    // movement ops
    TurnTo,GoTo,
    doBez,followSpline,
    followSplineLinear,
    followsplineSpline,
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