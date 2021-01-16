package org.parker.mips.compiler;

import org.parker.mips.compiler.data.UserLine;

public class InvalidArgumentsException extends CompilationException{

    InvalidArgumentsException(String message, UserLine line){
        super(message, line);
    }
    InvalidArgumentsException(String message, UserLine line, Throwable cause){
        super(message, line, cause);
    }
}
