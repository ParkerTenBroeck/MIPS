package org.parker.mips.assembler2.exception;

public class LableNotDeclaredError extends LinkingException {

    public LableNotDeclaredError(String message, Exception ex){
        super(message,ex);
    }
    public LableNotDeclaredError(String message){
        super(message);
    }


}
