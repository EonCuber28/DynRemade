package org.SquidSquad.Tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tokenizer {

    private Map<Integer,Integer> lineMap = new HashMap<>();
    private Map<Integer, Map<Integer, Integer>> charMap = new HashMap<>(); // clean line -> (clean char -> original char)
    /*
    this funciton handles the scrubbing of comments form the code defined by the description of comments in the docs at: ftcdyn.org (very much WIP rn)
    it was also fixed (and enhanced) by claude Sonnet 4.6
     */
    private String[] removeComments(String[] in) {
        in = in.clone();
        ArrayList<String> out = new ArrayList<>();
        lineMap.clear();
        charMap.clear();
        boolean inMultiComment = false;
        int originalIndex = 0;

        for (String str : in) {
            StringBuilder lineBuffer = new StringBuilder();
            Map<Integer, Integer> charIndexMap = new HashMap<>();
            int i = 0;

            while (i < str.length()) {
                if (i + 2 < str.length()
                        && str.charAt(i) == '\''
                        && str.charAt(i + 1) == '\''
                        && str.charAt(i + 2) == '\'') {
                    inMultiComment = !inMultiComment;
                    i += 3;
                    continue;
                }
                if (!inMultiComment
                        && i + 1 < str.length()
                        && str.charAt(i) == '/'
                        && str.charAt(i + 1) == '/') {
                    break;
                }
                if (!inMultiComment) {
                    charIndexMap.put(lineBuffer.length(), i);
                    lineBuffer.append(str.charAt(i));
                }
                i++;
            }

            // Remap char indexes after strip/collapse
            String raw = lineBuffer.toString();
            String cleaned = raw.strip().replaceAll("\\s+", " ");
            if (!cleaned.isBlank()) {
                int cleanLineIndex = out.size();
                lineMap.put(cleanLineIndex, originalIndex);

                // Build remapped charIndexMap accounting for strip/collapse
                Map<Integer, Integer> remappedCharMap = new HashMap<>();
                int rawOffset = raw.indexOf(cleaned.charAt(0)); // strip offset
                int cleanIdx = 0;
                int rawIdx = rawOffset;
                while (cleanIdx < cleaned.length() && rawIdx < raw.length()) {
                    char cleanChr = cleaned.charAt(cleanIdx);
                    char rawChr = raw.charAt(rawIdx);
                    if (cleanChr == ' ' && rawChr != ' ') {
                        rawIdx++;
                        continue;
                    }
                    if (rawChr != cleanChr) {
                        rawIdx++;
                        continue;
                    }
                    remappedCharMap.put(cleanIdx, charIndexMap.getOrDefault(rawIdx, rawIdx));
                    cleanIdx++;
                    rawIdx++;
                }

                charMap.put(cleanLineIndex, remappedCharMap);
                out.add(cleaned);
            }

            originalIndex++;
        }

        return out.toArray(new String[0]);
    }

    public Token[] processScript(String script){
        String[] lines = script.split("\r\n|\r|\n"); // chop it up
        lines = removeComments(lines);
        return tokenize(lines);
    }
    public Token[] processScript(String[] lines){
        lines = removeComments(lines);
        return tokenize(lines);
    }

    public Token[] tokenize(String[] cleanLines) {
        ArrayList<Token> tokens = new ArrayList<>();
        int cleanLineIndex = 0;

        for (String line : cleanLines) {
            int lineIndex = lineMap.get(cleanLineIndex);
            String[] lineChunks = line.split(" ");
            int cleanCharIndex = 0;

            for (String chunk : lineChunks) {
                int charIndex = charMap.get(cleanLineIndex).getOrDefault(cleanCharIndex, -1);
                //chunk line: lineIndex
                //chunk char: charIndex
                // tokenization process start
                Token[] processedChunk = processChunk(chunk, lineIndex,charIndex);
                for (Token token : processedChunk){
                    tokens.add(token);
                }
                // tokenization process end

                cleanCharIndex += chunk.length() + 1;
            }

            cleanLineIndex++;
        }

        return tokens.toArray(new Token[0]);
    }
    // fix to stop the creation of null TokenTypes for tokens was created my Claude Sonnet 4.6
    private Token[] processChunk(String chunk, int lineIndex,int charIndex){
        int chunkSize = chunk.length();
        ArrayList<Token> out = new ArrayList<>();
        // per character checks
        String internalChunk = "";
        int counter = 0;
        for (char piece : chunk.toCharArray()) {
            counter++;
            // string handleing
            if (piece == '"'){
                // its a "string" literal so add the internal Chunk to a literal token
                out.add(new Token(TokenTypes.Literal, lineIndex, charIndex, "\""));
                internalChunk = "";
            }
            //entering new traversal area -> mark with token
            else if (piece == '('){
                flushInternalChunk(internalChunk, out, lineIndex,charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Lparenth, lineIndex,charIndex));
            }
            else if (piece == '['){
                flushInternalChunk(internalChunk, out, lineIndex,charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Lbraket, lineIndex,charIndex));
            }
            else if (piece == '{'){
                flushInternalChunk(internalChunk, out, lineIndex,charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.LCbraket, lineIndex,charIndex));
            }
            //exiting traversal area -> mark with token
            else if (piece == ')'){
                flushInternalChunk(internalChunk, out, lineIndex,charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Rparenth, lineIndex,charIndex));
            }
            else if (piece == ']'){
                flushInternalChunk(internalChunk, out, lineIndex,charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Rbraket, lineIndex,charIndex));
            }
            else if (piece == '}'){
                flushInternalChunk(internalChunk, out, lineIndex,charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.RCbraket, lineIndex,charIndex));
            }
            // is comma? -> mark wth token
            else if (piece == ','){
                flushInternalChunk(internalChunk, out, lineIndex,charIndex);
                internalChunk = "";
                out.add(new Token(TokenTypes.Comma, lineIndex,charIndex));
            }
            else {
                internalChunk = internalChunk+piece;
                // now we ask questions
                //is there a variable declaration?
                if (internalChunk.equals("FieldCord")){
                    out.add(new Token(TokenTypes.FieldCord, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("FieldPos")){
                    out.add(new Token(TokenTypes.FieldPos, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Num")){
                    out.add(new Token(TokenTypes.Number, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Bool")){
                    out.add(new Token(TokenTypes.Bool, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("String")){
                    out.add(new Token(TokenTypes.String, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("List")){
                    out.add(new Token(TokenTypes.List, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Json")){
                    out.add(new Token(TokenTypes.Json, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is there a math command?
                else if (internalChunk.equals("ADD")){
                    out.add(new Token(TokenTypes.Add, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("SUB")){
                    out.add(new Token(TokenTypes.Sub, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("MUX")){
                    out.add(new Token(TokenTypes.Mux, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("DIV")){
                    out.add(new Token(TokenTypes.Div, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("POW")){
                    out.add(new Token(TokenTypes.Pow, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("SQR")){
                    out.add(new Token(TokenTypes.Sqrt, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("SIN")){
                    out.add(new Token(TokenTypes.Sin, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("invSIN")){
                    out.add(new Token(TokenTypes.iSin, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("COS")){
                    out.add(new Token(TokenTypes.Cos, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("invCOS")){
                    out.add(new Token(TokenTypes.iCos, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("TAN")){
                    out.add(new Token(TokenTypes.Tan, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("invTAN")){
                    out.add(new Token(TokenTypes.iTan, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("toDeg")){
                    out.add(new Token(TokenTypes.toDeg, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("toRad")){
                    out.add(new Token(TokenTypes.toRad, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Inc")){
                    out.add(new Token(TokenTypes.Increment, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is there a pointer?
                else if (internalChunk.equals("to")){
                    out.add(new Token(TokenTypes.To, lineIndex,charIndex));
                    internalChunk = "";
                }
                // is it a function/loop/condition declaration?
                else if (internalChunk.equals("def_path")){
                    out.add(new Token(TokenTypes.DefPath, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("for")){
                    out.add(new Token(TokenTypes.For, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("while")){
                    out.add(new Token(TokenTypes.While, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("if")){
                    out.add(new Token(TokenTypes.If, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is it a ending declaration?
                else if (internalChunk.equals("end")){
                    out.add(new Token(TokenTypes.End, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("autoPath")){
                    out.add(new Token(TokenTypes.MainPathFunc, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("start")){
                    out.add(new Token(TokenTypes.Start, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is it a logical operator?
                else if (internalChunk.equals("Or")){
                    out.add(new Token(TokenTypes.Or, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("And")){
                    out.add(new Token(TokenTypes.And, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Not")){
                    out.add(new Token(TokenTypes.Not, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is it movement command?
                else if (internalChunk.equals("turnTo")){
                    out.add(new Token(TokenTypes.TurnTo, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("goTo")){
                    out.add(new Token(TokenTypes.GoTo, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("followBezier")){
                    out.add(new Token(TokenTypes.DoPez, lineIndex,charIndex));
                    internalChunk = "";
                }
                //it is a starting declaration?
                else if (internalChunk.equals("PathStartPosition")){
                    out.add(new Token(TokenTypes.PathStartPos, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is it a jFunc/path_func call?
                else if (internalChunk.equals("jFunc")){
                    out.add(new Token(TokenTypes.Cmd, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("RUN")){
                    out.add(new Token(TokenTypes.Run, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is it a telemetry command?
                else if (internalChunk.equals("AddData")){
                    out.add(new Token(TokenTypes.AddData, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Update")){
                    out.add(new Token(TokenTypes.Update, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Clear")){
                    out.add(new Token(TokenTypes.Clear, lineIndex,charIndex));
                    internalChunk = "";
                }
                //is it a random command?
                else if (internalChunk.equals("RndFlt")){
                    out.add(new Token(TokenTypes.RngFloat, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("RngDbl")){
                    out.add(new Token(TokenTypes.RngDouble, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("Rngint")){
                    out.add(new Token(TokenTypes.RngInteger, lineIndex,charIndex));
                    internalChunk = "";
                } else if (internalChunk.equals("RngBool")){
                    out.add(new Token(TokenTypes.RngBoolean, lineIndex,charIndex));
                    internalChunk = "";
                } else {
                    if (counter == chunkSize) {
                        flushInternalChunk(internalChunk, out, lineIndex, charIndex);
                    }
                }
            }
        }

        return out.toArray(new Token[0]);
    }
    private void flushInternalChunk(String internalChunk, ArrayList<Token> out, int lineIndex, int charIndex) {
        if (internalChunk.isEmpty()) return;
        if (internalChunk.equals("true")) {
            out.add(new Token(TokenTypes.Literal, lineIndex, charIndex, true));
        } else if (internalChunk.equals("false")) {
            out.add(new Token(TokenTypes.Literal, lineIndex, charIndex, false));
        } else if (Character.isDigit(internalChunk.charAt(0))) {
            if (!internalChunk.contains(".")) internalChunk += ".0";
            try {
                out.add(new Token(TokenTypes.Literal, lineIndex, charIndex, Double.parseDouble(internalChunk)));
            } catch (NumberFormatException e) {
                throw new TokenizerException(e.getMessage(), "invalid number format at line" + lineIndex + " column:" + charIndex);
            }
        } else {
            out.add(new Token(TokenTypes.Name, lineIndex, charIndex, internalChunk));
        }
    }
}
