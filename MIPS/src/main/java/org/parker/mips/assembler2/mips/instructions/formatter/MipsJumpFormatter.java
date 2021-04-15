package org.parker.mips.assembler2.mips.instructions.formatter;

import org.parker.mips.assembler2.mips.MipsAssembler;

public interface MipsJumpFormatter extends MipsFormatter{

    @Override
    default int getInstructionSize() {
        return 4;
    }

    default void encode(byte[] data, int[] fields, MipsAssembler assembler){
        MipsFormatter.super.encode(data, fields, new int[]{6,26}, assembler);
    }
}
