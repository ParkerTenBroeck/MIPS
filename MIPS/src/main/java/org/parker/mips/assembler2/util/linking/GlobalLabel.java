package org.parker.mips.assembler2.util.linking;

import org.parker.mips.assembler2.exception.LabelNotDeclaredError;
import org.parker.mips.assembler2.util.Line;

public class GlobalLabel extends Label{

    private final AssemblyUnit parentAssemblyUnit;

    public GlobalLabel(AssemblyUnit au, String mnemonic, Line line) {
        super(mnemonic, line);
        this.parentAssemblyUnit = au;
    }

    @Override
    public long getAddress() {
        if(!(parentAssemblyUnit.getAsuLabelMap().containsKey(this.mnemonic))){
            throw new LabelNotDeclaredError(this);
        }
        return parentAssemblyUnit.getAsuLabelMap().get(this.mnemonic).getAddress();
    }
}
