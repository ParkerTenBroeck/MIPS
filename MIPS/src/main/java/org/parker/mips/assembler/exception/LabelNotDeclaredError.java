package org.parker.mips.assembler.exception;

import org.parker.mips.assembler.util.linking.Label;

public class LabelNotDeclaredError extends LinkingException {

    private Label label;


    public LabelNotDeclaredError(Label label) {
        super("Label was not declared");
        this.label = label;
    }

    @Override
    public String toString() {
        return "Label: " + label.mnemonic + " was not declared";
    }
}
