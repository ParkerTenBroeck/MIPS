package org.parker.mips.assembler_old;

import org.parker.mips.assembler_old.data.UserLine;

public class InvalidArgumentsException extends AssemblerException {

    InvalidArgumentsException(String message, UserLine line){
        super(message, line);
    }
    InvalidArgumentsException(String message, UserLine line, Throwable cause){
        super(message, line, cause);
    }
}
