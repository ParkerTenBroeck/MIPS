package org.parker.mips.assembler2.instruction;

import org.parker.mips.assembler2.base.OperandIndependentData;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsRegisterFormatter;

public class StandardInstruction extends OperandIndependentData {

    final InstructionFormatter isf;

    public StandardInstruction(InstructionFormatter isf) {
        super(isf.getInstructionSize());
        this.isf = isf;
    }

    @Override
    public void setOperandExpression(String operandExpression) {

        super.setOperandExpression(isf.modifyOperandExpression(operandExpression));
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[(int) getSize()];
        isf.encode(bytes, getOps(), null);
        return bytes;
    }
}
