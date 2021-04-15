package org.parker.mips.assembler2.exception;

public class LinkingException extends RuntimeException{

    public LinkingException(String message){
        super(message);
    }
    public LinkingException(String message, Exception cause){
        super(message, cause);
    }
}
