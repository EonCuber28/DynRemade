package org.SquidSquad.Tokenizer;

public class TokenizerException extends RuntimeException {
    public TokenizerException(String message, String deets) {
        super(message+"\n"+deets);
    }
}
