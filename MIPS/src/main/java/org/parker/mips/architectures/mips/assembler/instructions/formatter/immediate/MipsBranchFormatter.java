package org.parker.mips.architectures.mips.assembler.instructions.formatter.immediate;

import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.instruction.StandardInstruction;
import org.parker.mips.architectures.mips.assembler.instructions.formatter.MipsImmediateFormatter;
import org.parker.mips.assembler.operand.OpLong;
import org.parker.mips.assembler.operand.OpRegister;
import org.parker.mips.assembler.util.linking.LinkType;

public enum MipsBranchFormatter implements MipsImmediateFormatter {

    beq(0b000100),
    bne(0b000101);

    private final int opCode;

    MipsBranchFormatter(int opCode) {
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, BaseAssembler assembler) {
        int regt = 0;
        int regs = 0;
        int im = 0;

        if (instruction.argsLength() == 3) {
            if (!(instruction.getArg(0) instanceof OpRegister)) {
                instruction.throwParameterTypeError(0, OpRegister.class);
            }
            if (!(instruction.getArg(1) instanceof OpRegister)) {
                instruction.throwParameterTypeError(1, OpRegister.class);
            }
            if (!(instruction.getArg(2) instanceof OpLong)) {
                instruction.throwParameterTypeError(2, OpLong.class);
            }
            regs = ((Number) instruction.getArg(0).getValue()).intValue();
            regt = ((Number) instruction.getArg(1).getValue()).intValue();
            im = ((Number) instruction.getArg(2).getValue()).intValue();
        } else {
            instruction.throwParameterCountError(3);
        }
        //Operand Order o,s,t,i
        MipsImmediateFormatter.super.encode(data, new int[]{opCode, regs, regt, im}, assembler);
    }

    @Override
    public LinkType[] getLinkTypes() {
        return new LinkType[]{null, null, LinkType.RELATIVE_WORD};
    }
}
