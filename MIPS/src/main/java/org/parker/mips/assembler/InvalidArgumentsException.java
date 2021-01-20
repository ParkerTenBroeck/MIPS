package org.parker.mips.assembler;

import org.parker.mips.assembler.data.UserLine;

public class InvalidArgumentsException extends AssemblerException {

    InvalidArgumentsException(String message, UserLine line){
        super(message, line);
    }
    InvalidArgumentsException(String message, UserLine line, Throwable cause){
        super(message, line, cause);
    }
}
