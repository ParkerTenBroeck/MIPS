package org.parker.mips.assembler.operand;

import org.parker.mips.assembler.base.assembler.Assembler;
import org.parker.mips.assembler.exception.LabelNotDeclaredError;
import org.parker.mips.assembler.util.linking.Label;
import org.parker.mips.assembler.util.linking.LinkType;

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
    public void link(Assembler assembler, long sourceAddr, LinkType linkType) throws LabelNotDeclaredError {
        //if(labelMap.containsKey(labelMnemonic)){
            //Label label = labelMap.get(labelMnemonic);
            if(linkType == null){
                this.setValue(LinkType.ABSOLUTE_BYTE.link(sourceAddr, label.getAddress()));
            }else{
                this.setValue(linkType.link(sourceAddr, label.getAddress()));
            }
        //}else{
            //throw new LableNotDeclaredError("Label: " + labelMnemonic + " is not defined or not defined in the current scope");
        //}
        this.linked = true;
    }
}
