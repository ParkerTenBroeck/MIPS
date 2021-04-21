package org.parker.mips.architectures.mips.assembler.instructions.formatter;

import org.parker.mips.assembler.base.assembler.BaseAssembler;

public interface MipsJumpFormatter extends MipsFormatter{

    @Override
    default int getInstructionSize() {
        return 4;
    }

    default void encode(byte[] data, int[] fields, BaseAssembler assembler){
        MipsFormatter.super.encode(data, fields, new int[]{6,26}, assembler);
    }
}
