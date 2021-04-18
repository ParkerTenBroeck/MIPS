package org.parker.mips.assembler.exception;

import org.parker.mips.assembler.util.linking.Label;

public class LabelRedeclaredError extends AssemblerError{

    private final Label label;

    public LabelRedeclaredError(Label label) {
        super("Label: " + label.mnemonic + " has already been declared in this assembly unit", label.line);
        this.label = label;
    }
}
