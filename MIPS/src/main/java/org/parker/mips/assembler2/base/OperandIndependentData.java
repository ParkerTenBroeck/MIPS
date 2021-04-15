package org.parker.mips.assembler2.base;

public abstract class OperandIndependentData<ArgType> extends DataStatement<ArgType>{

    private byte[] data;
    private final long size;

    public OperandIndependentData(int size){
        this.size = size;
    }

    @Override
    public final long getSize() {
        return this.size;
    }

    public abstract byte[] toBytes();

    @Override
    public final byte[] toBinary() {
        if(data == null){
            data = toBytes();
            if(data.length != this.size){
                throw new RuntimeException("Size of data does not equal size");
            }
        }
        return this.data;
    }
}
