package org.parker.mips.assembler.util.linking;

import org.parker.mips.assembler.util.Line;

public class LocalLabel extends Label{

    private final long address;
    private final AssemblyUnit au;

    public LocalLabel(long address, AssemblyUnit au, String mnemonic, Line line) {
        super(mnemonic, line);
        this.address = address;
        this.au = au;
    }

    @Override
    public long getAddress() {
        return address + au.getStartingAddress();
    }
}
