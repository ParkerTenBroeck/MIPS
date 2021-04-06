package org.parker.mips.assembler2.instruction.mips.formatter;

import org.parker.mips.assembler2.Assembler;

public interface MipsImmediateFormatter extends MipsFormatter{

    @Override
    default int getInstructionSize() {
        return 4;
    }

    default void encode(byte[] data, int[] fields, Assembler assembler){
        MipsFormatter.super.encode(data, fields, new int[]{6,5,5,16}, assembler);
    }
}
