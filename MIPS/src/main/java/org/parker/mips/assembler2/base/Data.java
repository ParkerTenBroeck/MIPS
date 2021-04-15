package org.parker.mips.assembler2.base;

/**
 * Any statement that will become part of the assembled binary
 */
public interface Data {
    byte[] toBinary();
    default long getSize(){
        return toBinary().length;
    }
}
