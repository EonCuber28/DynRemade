package org.SquidSquad.CommandSequencer.Variables;

public class VariableException extends RuntimeException {
    public VariableException(String method, String involvedVars, String message) {
        super(method+" failed to use vars "+involvedVars+" because "+message);
    }
}
