package org.parker.mips.assembler;

import org.parker.mips.assembler.data.UserLine;

import java.io.PrintStream;
import java.io.PrintWriter;

public class AssemblerException extends Throwable{
    UserLine line;

    AssemblerException(String message, UserLine line){
        super(message);
        this.line = line;
    }
    AssemblerException(String message, UserLine line, Throwable cause){
        super(message, cause);
        this.line = line;
    }
    AssemblerException(UserLine line){
        super();
        this.line = line;
    }
    AssemblerException(){
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
