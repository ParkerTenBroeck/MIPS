package org.parker.mips.assembler2.operand;

public class OpLong implements Operand{

    private long value;

    public OpLong(long value){
        this.value = value;
    }
    public OpLong(){
    }
    protected void setValue(long value){
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }
}
