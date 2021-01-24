package org.parker.mips.processor;

import java.io.PrintStream;
import java.io.PrintWriter;

public class RunTimeMemoryException extends RuntimeException{

    public RunTimeMemoryException(){
        super();
    }

    public RunTimeMemoryException(String message){
        super(message);
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
