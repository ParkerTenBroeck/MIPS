package org.parker.mips.assembler2.mips.instructions.formatter;

import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.mips.MipsAssembler;

public interface MipsJumpFormatter extends MipsFormatter{

    @Override
    default int getInstructionSize() {
        return 4;
    }

    default void encode(byte[] data, int[] fields, BaseAssembler assembler){
        MipsFormatter.super.encode(data, fields, new int[]{6,26}, assembler);
    }
}
