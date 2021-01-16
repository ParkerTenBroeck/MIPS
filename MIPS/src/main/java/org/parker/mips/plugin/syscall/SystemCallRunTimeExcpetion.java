package org.parker.mips.plugin.syscall;

public class SystemCallRunTimeExcpetion extends  RuntimeException{

    public SystemCallRunTimeExcpetion(final String message, final Throwable cause){
        super(message, cause);
    }

    public SystemCallRunTimeExcpetion(final String message){
        super(message);
    }

    public SystemCallRunTimeExcpetion(final Throwable cause) {
        super(cause);
    }
}
