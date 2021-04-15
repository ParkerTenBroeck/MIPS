package org.parker.mips.assembler2.operand;

import org.parker.mips.assembler2.base.assembler.Assembler;
import org.parker.mips.assembler2.exception.LableNotDeclaredError;
import org.parker.mips.assembler2.util.Label;
import org.parker.mips.assembler2.util.linking.LinkType;

public class OpLabel extends OpLong implements LinkableOperand{

    protected final Label label;
    private Boolean linked = false;

    public OpLabel(Label label) {
        this.label = label;
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
    public void link(Assembler assembler, long sourceAddr, LinkType linkType) throws LableNotDeclaredError {
        //if(labelMap.containsKey(labelMnemonic)){
            //Label label = labelMap.get(labelMnemonic);
            if(linkType == null){
                this.setValue(LinkType.ABSOLUTE_BYTE.link(sourceAddr, label.address));
            }else{
                this.setValue(linkType.link(sourceAddr, label.address));
            }
        //}else{
            //throw new LableNotDeclaredError("Label: " + labelMnemonic + " is not defined or not defined in the current scope");
        //}
        this.linked = true;
    }
}
