package org.parker.mips.assembler2.operand;

import org.parker.mips.assembler2.exception.LableNotDeclaredError;
import org.parker.mips.assembler2.util.Label;
import org.parker.mips.assembler2.util.LinkType;

import java.util.HashMap;

public class OpLabel extends OpLong implements LinkableOperand{

    protected final String labelMnemonic;
    private Boolean linked = false;

    public OpLabel(String label) {
        this.labelMnemonic = label;
    }

    @Override
    public Long getValue() {
        if(linked) {
            return super.getValue();
        }else{
            throw new RuntimeException("Im not Linked yet!!");
        }
    }

    @Override
    public void link(HashMap<String, Label> labelMap, long sourceAddr, LinkType linkType) {
        if(labelMap.containsKey(labelMnemonic)){
            Label label = labelMap.get(labelMnemonic);
            if(linkType == null){
                this.setValue(LinkType.ABSOLUTE_BYTE.link(sourceAddr, label.address));
            }else{
                this.setValue(linkType.link(sourceAddr, label.address));
            }
        }else{
            throw new LableNotDeclaredError();
        }
        this.linked = true;
    }
}
