package org.parker.mips.architectures.mips.syscall;

public class InvalidSystemCallException extends  RuntimeException{

    InvalidSystemCallException(String message){
        super(message);
    }
}
