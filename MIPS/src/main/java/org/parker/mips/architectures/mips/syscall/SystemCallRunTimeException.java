package org.parker.mips.architectures.mips.syscall;

@SuppressWarnings("unused")
public class SystemCallRunTimeException extends  RuntimeException{

    public SystemCallRunTimeException(final String message, final Throwable cause){
        super(message, cause);
    }

    public SystemCallRunTimeException(final String message){
        super(message);
    }

    public SystemCallRunTimeException(final Throwable cause) {
        super(cause);
    }
}
