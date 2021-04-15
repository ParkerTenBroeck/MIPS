package org.parker.mips.assembler2.exception;

import org.parker.mips.assembler2.util.Line;

public class InstructionNotFoundError extends AssemblerError{
    public InstructionNotFoundError(String message, Line line, int s, int e, Exception ex){
        super(message,line, s, e, ex);
    }
    public InstructionNotFoundError(String message, Line line, int s, int e){
        super(message,line, s, e);
    }

    public InstructionNotFoundError(String message, Line line, int s){
        super(message,line, s);
    }

    public InstructionNotFoundError(String message, Line line, int s, Exception ex){
        super(message,line, s, ex);
    }
}
