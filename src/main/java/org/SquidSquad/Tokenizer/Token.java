package org.SquidSquad.Tokenizer;

public class Token {
    private TokenTypes type;
    public TokenTypes type(){return type;}

    private int line;
    public int getLine(){return line;}

    private Object value;
    private Object getValue(){return value;}

    public Token(TokenTypes type,int line){
        this.type = type;
        this.line = line;
    }
    public Token(TokenTypes type,int line,Object value){
        this.type = type;
        this.line = line;
        this.value = value;
    }
}
