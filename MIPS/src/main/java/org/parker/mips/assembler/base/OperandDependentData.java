package org.parker.mips.assembler.base;

/**
 * Data which size depends on the operands present at assembly time
 * Memory Labels that have not been defined yet cannot be used as arguments
 */
public abstract class OperandDependentData<ArgType> extends DataStatement<ArgType>{
    private byte[] data;

    public OperandDependentData(){
    }

    protected abstract byte[] toBytes();

    @Override
    public final byte[] toBinary() {
        if (data == null) {
            data = toBytes();
        }
        return data;
    }

    @Override
    public final long getSize() {
        return toBinary().length;
    }
}
