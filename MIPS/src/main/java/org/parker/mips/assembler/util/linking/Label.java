package org.parker.mips.assembler.util.linking;

import org.parker.mips.assembler.util.Line;

public abstract class Label {

    public final String mnemonic;
    public final Line line;

    public Label(String mnemonic, Line line){
        this.mnemonic = mnemonic;
        this.line = line;
    }

    public abstract long getAddress();

    @Override
    public String toString() {
        return "Label: " + mnemonic + " on line: " + line.getHumanLineNumber() + " from: " + line.getFile().getAbsolutePath();
    }
}
