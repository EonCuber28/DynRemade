package org.SquidSquad.Tokenizer;

// dis a lot a things to take care of
public enum TokenTypes {
    // single char tokens
    Lparenth, Rparenth, Comma,
    // math commands
    Add,Sub,Mux,Div,
    Pow,Sqrt,Sin,iSin,
    Cos,iCos,Tan,iTan,
    toRad,toDeg,
    // variables
    Number,Bool,String,
    List,Json,FieldCord,
    FieldPos,
    // logical Ops
    Equals,NotEqual,isMore,
    isLess,And,Or,Not,
    // func/loop/if
    DefPath,Run,While,For,
    // telemetry
    AddData,Update,
    // literals
    Literal, Name,
    // extra
    Start,End,PathStartPos,Cmd,To
}