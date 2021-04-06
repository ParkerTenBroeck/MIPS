package org.parker.mips.assembler2.instruction;

import org.parker.mips.assembler2.base.LinkableData;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.LinkableOperand;
import org.parker.mips.assembler2.operand.Operand;
import org.parker.mips.assembler2.util.Label;
import org.parker.mips.assembler2.util.LinkType;

import java.util.HashMap;

public class StandardLinkableInstruction extends StandardInstruction implements LinkableData {

    public StandardLinkableInstruction(InstructionFormatter isf) {
        super(isf);
    }

    @Override
    public void link(HashMap<String, Label> labelMap, long sourceAddress) {
        Operand[] ops = getOps();
        LinkType[] linkTypes = isf.getLinkTypes();

        for(int i = 0; i < linkTypes.length; i ++){
            if(ops[i] instanceof LinkableOperand && linkTypes[i] != null){
                ((LinkableOperand) ops[i]).link(labelMap, sourceAddress, linkTypes[i]);
            }
        }
        //this.isf.getLinkType;
    }
}
