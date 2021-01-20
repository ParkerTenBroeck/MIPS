package org.parker.mips.assembler;

import org.parker.mips.assembler.data.UserLine;

public class DirectiveException extends RuntimeException{
    private UserLine line = null;

    public DirectiveException(final String message){
        super(message);
    }
    public DirectiveException(final Throwable cause){
        super(cause);
    }
    public DirectiveException(final String message, final Throwable cause){
        super(message, cause);
    }
    public DirectiveException(final String message, final UserLine line, final Throwable cause){
        super(message, cause);
        this.line = line;
    }
    public DirectiveException(final String message, final UserLine line){
        super(message);
        this.line = line;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ((line != null) ? "" : (" on line " + line.toString()));
    }
}
