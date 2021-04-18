package org.parker.mips.assembler.exception;

import org.parker.mips.assembler.util.Line;

public class ExpressionError extends AssemblerError{

    public ExpressionError(String message, Line line, int s, int e, Exception ex){
        super(message,line, s, e, ex);
    }
    public ExpressionError(String message, Line line, int s, int e){
        super(message,line, s, e);
    }

    public ExpressionError(String message, Line line, int s){
        super(message,line, s);
    }

    public ExpressionError(String message, Line line, int s, Exception ex){
        super(message,line, s, ex);
    }
}
