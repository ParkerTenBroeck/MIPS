package org.parker.mips.assembler.mips.instructions.formatter;

import org.parker.mips.assembler.mips.exceptions.FieldOverflow;
import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.instruction.InstructionFormatter;

public interface MipsFormatter extends InstructionFormatter {


    default void encode(byte[] data, int[] fields, int[] fieldSize, BaseAssembler assembler){

        int dataI = 0;

        for(int i = 0; i < fields.length; i ++){
            int mask = (1 << (fieldSize[i])) - 1;

            int sMax = (1 << (fieldSize[i] - 1)) - 1;
            int sMin = -((1 << (fieldSize[i] - 1)));
            int uMax = (1 << (fieldSize[i])) - 1;
            int  uMin = 0;
            if((sMax >= fields[i] && sMin <= fields[i]) || (uMax >= fields[i] && uMin <= fields[i])) {
                dataI = dataI << fieldSize[i];
                dataI |= fields[i] & mask;
            }else{
                throw new FieldOverflow(i,fields[i], uMax, sMin);
            }
        }

        //ByteBuffer bb = ByteBuffer.wrap(data);
        //bb.order(assembler.isBigEndian()? ByteOrder.BIG_ENDIAN:ByteOrder.LITTLE_ENDIAN);
        //bb.putInt(dataI);
        //no idea if this is big or small endian
        data[0] = (byte) ((dataI & 0xFF000000) >> 24);
        data[1] = (byte) ((dataI & 0x00FF0000) >> 16);
        data[2] = (byte) ((dataI & 0x0000FF00) >> 8);
        data[3] = (byte) ((dataI & 0x000000FF));
    }
}
