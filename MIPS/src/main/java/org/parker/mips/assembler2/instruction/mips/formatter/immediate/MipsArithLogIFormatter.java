package org.parker.mips.assembler2.instruction.mips.formatter.immediate;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.exception.ParameterTypeError;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsImmediateFormatter;
import org.parker.mips.assembler2.operand.OpImmediate;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsArithLogIFormatter implements MipsImmediateFormatter {

    addi(0b001000),
    addiu(0b001001),
    andi(0b001100),
    ori(0b001101),
    xori(0b001110),
    slti(0b001010),
    sltiu(0b001001);

    private final int opCode;

    MipsArithLogIFormatter(int opCode) {
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
            if (!(operands[2] instanceof OpImmediate)) {
                throw new ParameterTypeError();
            }
            regt = ((Number) operands[0].getValue()).intValue();
            regs = ((Number) operands[1].getValue()).intValue();
            im = ((Number) operands[2].getValue()).intValue();
        } else {
            //error
        }
        //Operand Order o,s,t,i
        MipsImmediateFormatter.super.encode(data, new int[]{opCode, regs, regt, im}, assembler);
    }
}
