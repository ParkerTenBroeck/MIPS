package org.parker.mips.assembler2.util;

public class Label {

    public final long address;
    public final Line line;
    public final String mnemonic;

    public Label(long address, String mnemonic, Line line){
        this.address = address;
        this.mnemonic = mnemonic;
        this.line = line;
    }
}
