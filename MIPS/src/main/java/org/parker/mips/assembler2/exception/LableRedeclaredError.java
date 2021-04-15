package org.parker.mips.assembler2.exception;

import org.parker.mips.assembler2.util.Line;

public class LableRedeclaredError extends AssemblerError{
    public LableRedeclaredError(String message, Line line, int s, int e, Exception ex){
        super(message,line, s, e, ex);
    }
    public LableRedeclaredError(String message, Line line, int s, int e){
        super(message,line, s, e);
    }

    public LableRedeclaredError(String message, Line line, int s){
        super(message,line, s);
    }

    public LableRedeclaredError(String message, Line line, int s, Exception ex){
        super(message,line, s, ex);
    }
}
