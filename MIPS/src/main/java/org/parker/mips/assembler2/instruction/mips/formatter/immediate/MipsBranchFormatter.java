package org.parker.mips.assembler2.instruction.mips.formatter.immediate;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.exception.ParameterTypeError;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsImmediateFormatter;
import org.parker.mips.assembler2.operand.OpLong;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;
import org.parker.mips.assembler2.util.LinkType;

public enum MipsBranchFormatter implements MipsImmediateFormatter {

    beq(0b000100),
    bne(0b000101);

    private final int opCode;

    MipsBranchFormatter(int opCode) {
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler) {
        int regt = 0;
        int regs = 0;
        int im = 0;

        if (operands.length == 3) {
            if (!(operands[0] instanceof OpRegister)) {
                throw new ParameterTypeError();
            }
            if (!(operands[1] instanceof OpRegister)) {
                throw new ParameterTypeError();
            }
            if (!(operands[2] instanceof OpLong)) {
                throw new ParameterTypeError();
            }
            regs = ((Number) operands[0].getValue()).intValue();
            regt = ((Number) operands[1].getValue()).intValue();
            im = ((Number) operands[2].getValue()).intValue();
        } else {
            //error
        }
        //Operand Order o,s,t,i
        MipsImmediateFormatter.super.encode(data, new int[]{opCode, regs, regt, im}, assembler);
    }

    @Override
    public LinkType[] getLinkTypes() {
        return new LinkType[]{null, null, LinkType.RELATIVE_WORD};
    }
}
