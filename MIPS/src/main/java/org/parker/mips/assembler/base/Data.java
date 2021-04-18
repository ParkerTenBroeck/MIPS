package org.parker.mips.assembler.base;

/**
 * Any statement that will become part of the assembled binary
 */
public interface Data {
    byte[] toBinary();
    default long getSize(){
        return toBinary().length;
    }
}
