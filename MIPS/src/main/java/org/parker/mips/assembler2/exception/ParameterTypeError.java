package org.parker.mips.assembler2.exception;

import org.parker.mips.assembler2.util.Line;

public class ParameterTypeError extends AssemblerError{

    public ParameterTypeError(String message, Line line, int s, int e, Exception ex){
        super(message,line, s, e, ex);
    }
    public ParameterTypeError(String message, Line line, int s, int e){
        super(message,line, s, e);
    }

    public ParameterTypeError(String message, Line line, int s){
        super(message,line, s);
    }

    public ParameterTypeError(String message, Line line, int s, Exception ex){
        super(message,line, s, ex);
    }
}
