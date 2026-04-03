package org.SquidSquad.Tokenizer;

import java.util.ArrayList;

public class Tokenizer {
    /*
    this funciton handles the scrubbing of comments form the code defined by the description of comments in the docs at: ftcdyn.org
    it was also fixed (and enhanced) by claude Sonnet 4.6
     */
    private String[] removeComments(String[] in) {
        in = in.clone();
        ArrayList<String> out = new ArrayList<>();
        boolean inMultiComment = false;

        for (String str : in) {
            StringBuilder lineBuffer = new StringBuilder();
            int i = 0;

            while (i < str.length()) {
                // Check for ''' toggle
                if (i + 2 < str.length()
                        && str.charAt(i) == '\''
                        && str.charAt(i + 1) == '\''
                        && str.charAt(i + 2) == '\'') {
                    inMultiComment = !inMultiComment;
                    i += 3;
                    continue;
                }

                // Check for // single-line comment
                if (!inMultiComment
                        && i + 1 < str.length()
                        && str.charAt(i) == '/'
                        && str.charAt(i + 1) == '/') {
                    break;
                }

                if (!inMultiComment) {
                    lineBuffer.append(str.charAt(i));
                }

                i++;
            }

            out.add(lineBuffer.toString());
        }

        return out.stream().filter(s -> !s.isBlank()).toArray(String[]::new); // clears the final result of empty lines or lines with just spaces at the end.
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

    public Token[] tokenize(String[] cleanLines){
        ArrayList<Token> tokens = new ArrayList<>();

        for (String line : cleanLines){
            String cleanLine = line.strip();
            String[] lineChunks = cleanLine.split(" ");
            for (String chunk : lineChunks){
                System.out.println(chunk);
            }
        }

        return null;
    }
}
