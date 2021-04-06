package org.parker.mips.assembler2.instruction.mips.formatter;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.instruction.InstructionFormatter;

public interface MipsRegisterFormatter extends MipsFormatter {

    @Override
    default int getInstructionSize() {
        return 4;
    }

    default void encode(byte[] data, int[] fields, Assembler assembler){
        MipsFormatter.super.encode(data, fields, new int[]{6,5,5,5,5,6}, assembler);
    }
}
