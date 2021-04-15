package org.parker.mips.assembler2.mips.instructions.formatter.immediate;

import org.parker.mips.assembler2.instruction.StandardInstruction;
import org.parker.mips.assembler2.mips.MipsAssembler;
import org.parker.mips.assembler2.mips.instructions.formatter.MipsImmediateFormatter;
import org.parker.mips.assembler2.operand.OpLong;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.util.linking.LinkType;

public enum MipsBranchZFormatter implements MipsImmediateFormatter {

    bgtz(0b000111),
    blez(0b000110);

    private final int opCode;

    MipsBranchZFormatter(int opCode) {
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, MipsAssembler assembler) {
        int regs = 0;
        int im = 0;

        if (instruction.argsLength() == 2) {
            if (!(instruction.getArg(0) instanceof OpRegister)) {
                instruction.throwParameterTypeError(0, OpRegister.class);
            }
            if (!(instruction.getArg(1) instanceof OpLong)) {
                instruction.throwParameterTypeError(1, OpLong.class);
            }
            regs = ((Number) instruction.getArg(0).getValue()).intValue();
            im = ((Number) instruction.getArg(1).getValue()).intValue();
        } else {
            instruction.throwParameterCountError(2);
        }
        //Operand Order o,s,t,i
        MipsImmediateFormatter.super.encode(data, new int[]{opCode, regs, 0, im}, assembler);
    }

    @Override
    public LinkType[] getLinkTypes() {
        return new LinkType[]{null, LinkType.RELATIVE_WORD};
    }
}
