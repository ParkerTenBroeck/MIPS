package org.parker.mips.assembler.debugger;

import org.parker.mips.assembler.util.Line;

import java.io.Serializable;

public class FinalizedLabel implements Serializable {

    final Line line;
    final String mnemonic;
    final long address;

    public FinalizedLabel(String mnemonic, Line line, long address) {
        this.line = line;
        this.mnemonic = mnemonic;
        this.address = address;
    }

    public long getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FinalizedLabel){
            if(mnemonic.equals(((FinalizedLabel) obj).mnemonic)
            && line.getFile().getAbsolutePath().equals(((FinalizedLabel) obj).line.getFile().getAbsolutePath())
            && getAddress() == ((FinalizedLabel) obj).getAddress()){
                return true;
            }
        }else if(obj instanceof String){
            if(obj.equals(mnemonic + ": " + line.getFile().getAbsolutePath())){
                return true;
            }
        }
        return super.equals(obj);
    }
}
