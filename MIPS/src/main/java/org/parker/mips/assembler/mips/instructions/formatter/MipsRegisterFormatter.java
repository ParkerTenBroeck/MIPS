package org.parker.mips.assembler.mips.instructions.formatter;

import org.parker.mips.assembler.base.assembler.BaseAssembler;

public interface MipsRegisterFormatter extends MipsFormatter {

    @Override
    default int getInstructionSize() {
        return 4;
    }

    default void encode(byte[] data, int[] fields, BaseAssembler assembler){
        MipsFormatter.super.encode(data, fields, new int[]{6,5,5,5,5,6}, assembler);
    }
}
