package org.parker.mips.assembler2.instruction;

import org.parker.mips.assembler2.mips.MipsAssembler;
import org.parker.mips.assembler2.util.linking.LinkType;

public interface InstructionFormatter {

    int getInstructionSize();
    void encode(byte[] data, StandardInstruction instruction, MipsAssembler assembler);

    /**
     * only used in LinkableInstructions
     * @return returns an array that represents the linktype of each operand in the corresponding index
     *
     */
    default LinkType[] getLinkTypes(){
        return new LinkType[0];
    }

}
