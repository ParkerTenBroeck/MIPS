package org.parker.mips.compiler;

import org.parker.mips.compiler.data.UserLine;

import java.io.PrintStream;
import java.io.PrintWriter;

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
        String temp = "On Line " + line + ": ";
        temp  += (super.getMessage() == null ? "" : super.getMessage()) ;
        temp += (super.getCause() == null ? "" : super.getCause().getMessage());
        return temp;
    }

    @Override
    public void printStackTrace() {
        //super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        //super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        //super.printStackTrace(s);
    }
}
