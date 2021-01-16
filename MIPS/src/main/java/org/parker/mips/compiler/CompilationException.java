package org.parker.mips.compiler;

import org.parker.mips.compiler.data.UserLine;

public class CompilationException extends Throwable{
    UserLine line;

    CompilationException(String message, UserLine line){
        super(message);
        this.line = line;
    }
    CompilationException(String message, UserLine line, Throwable cause){
        super(message, cause);
        this.line = line;
    }
    CompilationException(UserLine line){
        super();
        this.line = line;
    }
    CompilationException(){
        super();
    }

    @Override
    public String getMessage(){
        return "On Line: " + line + " " + super.getMessage();
    }
}
