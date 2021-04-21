package org.parker.mips.architectures.mips.syscall;

public class MissingYMALField extends RuntimeException{

    final private String field;

    public MissingYMALField(String field){
        super("Missing field " + field);
        this.field = field;
    }
}
