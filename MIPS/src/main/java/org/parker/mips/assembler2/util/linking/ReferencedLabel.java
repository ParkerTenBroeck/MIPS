package org.parker.mips.assembler2.util.linking;

import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.exception.IllegalLabelReference;
import org.parker.mips.assembler2.exception.LabelNotDeclaredError;
import org.parker.mips.assembler2.util.Line;

import java.util.Map;

public class ReferencedLabel extends Label{

    private final Map<String, Label> globalLabelMap;

    public ReferencedLabel(Map<String, Label> globalLabelMap, String mnemonic, Line line) {
        super(mnemonic, line);
        this.globalLabelMap = globalLabelMap;
    }

    public ReferencedLabel(BaseAssembler assembler, String mnemonic, Line line) {
        this(assembler.getGlobalLabelMap(), mnemonic, line);
    }

    @Override
    public long getAddress() {
        if(this.globalLabelMap.containsKey(this.mnemonic)) {
            if(globalLabelMap.get(this.mnemonic) instanceof ReferencedLabel){
                throw new IllegalLabelReference(this, globalLabelMap.get(this.mnemonic));
            }
            return globalLabelMap.get(this.mnemonic).getAddress();
        }else{
            throw new LabelNotDeclaredError(this);
        }
    }
}
