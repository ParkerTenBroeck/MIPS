package org.parker.mips.plugin.syscall;

public class InvalidSystemCallException extends  RuntimeException{

    InvalidSystemCallException(String message){
        super(message);
    }
}
