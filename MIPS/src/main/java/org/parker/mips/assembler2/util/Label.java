package org.parker.mips.assembler2.util;

import org.parker.mips.assembler2.util.Line;

public class Label{

    public final long address;
    public final Line line;
    public final String mnemonic;

    public Label(long address, String mnemonic, Line line){
        this.address = address;
        this.mnemonic = mnemonic;
        this.line = line;
    }

    public Label(String mnemonic){
        this.mnemonic = mnemonic;
        line = null;
        address = -1;
    }
}
