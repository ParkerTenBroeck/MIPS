package org.parker.mips.assembler2.instruction;

import org.parker.mips.assembler2.base.LinkableData;
import org.parker.mips.assembler2.base.assembler.Assembler;
import org.parker.mips.assembler2.operand.LinkableOperand;
import org.parker.mips.assembler2.util.linking.LinkType;

public class StandardLinkableInstruction extends StandardInstruction implements LinkableData {

    public StandardLinkableInstruction(InstructionFormatter isf) {
        super(isf);
    }

    @Override
    public void link(Assembler assembler, long sourceAddress) {

        LinkType[] linkTypes = isf.getLinkTypes();

        for(int i = 0; i < linkTypes.length; i ++){
            if(linkTypes[i] == null) continue;

            if(getArg(i) instanceof LinkableOperand){
                try {
                    ((LinkableOperand) getArg(i)).link(assembler, sourceAddress, linkTypes[i]);
                }catch(Exception e){
                    this.throwLinkingException(i, e);
                }
            }
        }
    }
}
