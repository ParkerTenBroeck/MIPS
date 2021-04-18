package org.parker.mips.assembler.instruction;

import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.util.linking.LinkType;

public interface InstructionFormatter {

    int getInstructionSize();
    void encode(byte[] data, StandardInstruction instruction, BaseAssembler assembler);

    /**
     * only used in LinkableInstructions
     * @return returns an array that represents the linktype of each operand in the corresponding index
     *
     */
    default LinkType[] getLinkTypes(){
        return new LinkType[0];
    }

}
