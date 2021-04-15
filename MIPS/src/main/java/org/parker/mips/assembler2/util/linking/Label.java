package org.parker.mips.assembler2.util.linking;

import org.parker.mips.assembler2.util.Line;

public abstract class Label {

    public final String mnemonic;
    public final Line line;

    public Label(String mnemonic, Line line){
        this.mnemonic = mnemonic;
        this.line = line;
    }

    public abstract long getAddress();

}
