package org.parker.mips.assembler2.base;

import org.parker.mips.assembler2.util.ExpressionParser;
import org.parker.mips.assembler2.util.Label;

import java.util.HashMap;

/**
 * Any statement that will become part of the assembled binary
 */
public interface Data {
    byte[] toBinary();
    default long getSize(){
        return toBinary().length;
    }
}
