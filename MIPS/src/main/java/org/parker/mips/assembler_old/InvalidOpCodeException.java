package org.parker.mips.assembler_old;

import org.parker.mips.assembler_old.data.UserLine;

public class InvalidOpCodeException extends AssemblerException {

    InvalidOpCodeException(String message, UserLine line){
        super(message, line);
    }
    InvalidOpCodeException(UserLine line){
        super(line);
    }
}

