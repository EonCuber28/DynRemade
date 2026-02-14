package org.SquidSquad.Tokenizer;

import com.sun.source.tree.ArrayAccessTree;

import java.util.ArrayList;

public class Tokenizer {
    private final ArrayList<Token> tokens = new ArrayList<>();

    private String[] removeComments(String[] in){
        ArrayList<String> out = new ArrayList<>();
        char[] lastChars = new char[2];
        boolean inMultiComment = false;
        for (String str : in){
            for (char chr : str.toCharArray()){
                String lastChunk = String.valueOf(lastChars[1]);
                lastChunk += lastChars[0];
                lastChunk += chr;
                if (lastChunk == "'''"){
                    inMultiComment = !inMultiComment;
                }
                String lastSmallChunk = String.valueOf(lastChars[0]);
                lastSmallChunk += chr;
                if (lastSmallChunk == "//"){
                    break;
                }
            }
        }
        return out.toArray(new String[out.size()]);
    }

    public Token[] processScript(String script){
        for (char character : script.toCharArray()) {

        }
        return null;
    }
    public Token[] processScript(String[] lines){return null;}
}
