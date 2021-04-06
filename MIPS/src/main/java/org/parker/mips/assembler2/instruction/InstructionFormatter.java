package org.parker.mips.assembler2.instruction;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.operand.Operand;
import org.parker.mips.assembler2.util.LinkType;

public interface InstructionFormatter {

    int getInstructionSize();
    void encode(byte[] data, Operand[] operands, Assembler assembler);
    default String modifyOperandExpression(String expression){
        return expression;
    }

    /**
     * only used in LinkableInstructions
     * @return returns an array that represents the linktype of each operand in the corresponding index
     *
     */
    default LinkType[] getLinkTypes(){
        return new LinkType[0];
    }

}
