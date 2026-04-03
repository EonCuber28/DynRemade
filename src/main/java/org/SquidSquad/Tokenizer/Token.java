package org.SquidSquad.Tokenizer;

public class Token {
    private TokenTypes type;
    private Object value;
    private int line;

    public TokenTypes type(){
        return type;
    }
    public int getLine(){
        return line;
    }

    private Object getValue(){
        return value;
    }

    public Token(TokenTypes type,int line){
        this.type = type;
        this.line = line;
    }
    public Token(TokenTypes type,int line,Object value){
        this.type = type;
        this.line = line;
        this.value = value;
    }
    public String toString(){
        // "[Line X; toeknType; value]"
        String tokenType = "";
        switch (type){
            // single chars
            case Lparenth -> tokenType = "Left Parenthesis";
            case Rparenth -> tokenType = "Right Parenthesis";
            case Comma -> tokenType = "Comma";

            case Lbraket -> tokenType = "Left Curly Bracket";
            case Rbraket -> tokenType = "Right Curly Bracket";

            // math ops
            case Add -> tokenType = "Add";
            case Sub -> tokenType = "Subtract";
            case Mux -> tokenType = "Multiply";
            case Div -> tokenType = "Divide";

            case Pow -> tokenType = "Exponential";
            case Sqrt -> tokenType = "Square Root";
            case Sin -> tokenType = "Sine";
            case iSin -> tokenType = "Inverse Sine";

            case Cos -> tokenType = "Cosine";
            case iCos -> tokenType = "Inverse Cosine";
            case Tan -> tokenType = "Tangent";
            case iTan -> tokenType = "Inverse Tangent";

            case toRad -> tokenType = "To Radian";
            case toDeg -> tokenType = "To Degree";

            case Increment -> tokenType = "Increment Number";
            case Decrement ->  tokenType = "Decrement Number";

            // variable types
            case Number -> tokenType = "Number";
            case Bool -> tokenType = "Boolean";
            case String -> tokenType = "String";

            case List -> tokenType = "List";
            case Json -> tokenType = "Json";
            case FieldCord -> tokenType = "Field Coordinate";

            case FieldPos -> tokenType = "Field Position";

            // logical ops
            case Equals -> tokenType = "Equals";
            case NotEqual -> tokenType = "Not Equal";
            case isMore -> tokenType = "Is More";

            case isLess -> tokenType = "Is Less";
            case And -> tokenType = "And";
            case Or -> tokenType = "Or";
            case Not -> tokenType = "Not";

            // funcs/loop/if
            case DefPath -> tokenType = "Define Path";
            case Run -> tokenType = "Run"; // run target path function
            case While -> tokenType = "While Loop";
            case For -> tokenType = "For Loop";

            // telem
            case AddData -> tokenType = "Add Telemetry Data";
            case Update -> tokenType = "Update Telemetry";
            case Clear -> tokenType = "Clear Telemetry";

            // literals
            case Literal -> tokenType = "Literal"; // variable value, etc.
            case Name -> tokenType = "Name"; // name of variable, or path func

            // extra
            case Start -> tokenType = "Start"; // start path func, or loop
            case End -> tokenType = "End"; // end of path func, or loop
            case PathStartPos -> tokenType = "Path Start Position";
            case Cmd -> tokenType = "Execute Command"; // execute java defined command, can take N values, and can return 1 value.
            case To -> tokenType = "To"; // this is like a output variable pointer thingy, it tells where to specifically send the operation output to. ex: Add Var1 Var2 *TO* Var3
            default -> tokenType = "null (BAD)";
        }
        return "[Line "+line+"; "+tokenType+"; "+value+"]";
    }
}
