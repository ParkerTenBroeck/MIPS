package org.parker.mips.architectures.mips.emulator.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class InvalidOpCodeException extends RuntimeException{

    public InvalidOpCodeException(){
        super();
    }

    public InvalidOpCodeException(String message){
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
