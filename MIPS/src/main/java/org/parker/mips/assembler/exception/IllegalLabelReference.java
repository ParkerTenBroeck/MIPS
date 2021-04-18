package org.parker.mips.assembler.exception;

import org.parker.mips.assembler.util.linking.Label;

public class IllegalLabelReference extends LinkingException{

    private final Label label;
    private final Label reference;

    public IllegalLabelReference(Label label, Label reference){
        this.label = label;
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Cannot reference \n" + reference.toString() + "\nwith " + label.toString();
    }
}
