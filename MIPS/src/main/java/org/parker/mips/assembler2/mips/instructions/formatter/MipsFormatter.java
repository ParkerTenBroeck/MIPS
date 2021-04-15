package org.parker.mips.assembler2.mips.instructions.formatter;

import org.parker.mips.assembler2.mips.MipsAssembler;
import org.parker.mips.assembler2.instruction.InstructionFormatter;

public interface MipsFormatter extends InstructionFormatter {


    default void encode(byte[] data, int[] fields, int[] fieldSize, MipsAssembler assembler){

        int dataI = 0;

        for(int i = 0; i < fields.length; i ++){
            int max = (1 << (fieldSize[i])) - 1;
            int min = -((1 << (fieldSize[i])));
            if(max < fields[i] || min > fields[i]) {
                //error field overflow
            }else{
                dataI = dataI << fieldSize[i];
                dataI |= fields[i] & max;
            }
        }
        //no idea if this is big or small endian
        data[0] = (byte) ((dataI & 0xFF000000) >> 24);
        data[1] = (byte) ((dataI & 0x00FF0000) >> 16);
        data[2] = (byte) ((dataI & 0x0000FF00) >> 8);
        data[3] = (byte) ((dataI & 0x000000FF));
    }
}
