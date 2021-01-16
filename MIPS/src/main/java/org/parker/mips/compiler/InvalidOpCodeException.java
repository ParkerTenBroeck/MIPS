package org.parker.mips.compiler;

import org.parker.mips.compiler.data.UserLine;

public class InvalidOpCodeException extends CompilationException{

    InvalidOpCodeException(String message, UserLine line){
        super(message, line);
    }
    InvalidOpCodeException(UserLine line){
        super(line);
    }
}

