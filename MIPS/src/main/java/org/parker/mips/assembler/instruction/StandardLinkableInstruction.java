package org.parker.mips.assembler.instruction;

import org.parker.mips.assembler.operand.LinkableOperand;
import org.parker.mips.assembler.base.LinkableData;
import org.parker.mips.assembler.base.assembler.Assembler;
import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.util.linking.LinkType;

public class StandardLinkableInstruction extends StandardInstruction implements LinkableData {

    public StandardLinkableInstruction(InstructionFormatter isf, BaseAssembler assembler) {
        super(isf, assembler);
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
