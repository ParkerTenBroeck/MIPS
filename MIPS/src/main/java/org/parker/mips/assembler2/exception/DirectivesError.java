package org.parker.mips.assembler2.exception;

import org.parker.mips.assembler2.exception.AssemblerError;
import org.parker.mips.assembler2.util.Line;

public class DirectivesError extends AssemblerError {

    public DirectivesError(String message, Line line, int s, int e, Exception ex){
        super(message,line, s, e, ex);
    }
    public DirectivesError(String message, Line line, int s, int e){
        super(message,line, s, e);
    }

    public DirectivesError(String message, Line line, int s){
        super(message,line, s);
    }

    public DirectivesError(String message, Line line, int s, Exception ex){
        super(message,line, s, ex);
    }

    public DirectivesError(String message, Line line) {
        super(message, line);
    }
}
