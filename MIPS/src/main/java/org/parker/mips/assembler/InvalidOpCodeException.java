package org.parker.mips.assembler;

import org.parker.mips.assembler.data.UserLine;

public class InvalidOpCodeException extends AssemblerException {

    InvalidOpCodeException(String message, UserLine line){
        super(message, line);
    }
    InvalidOpCodeException(UserLine line){
        super(line);
    }
}

