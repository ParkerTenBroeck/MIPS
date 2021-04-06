package org.parker.mips.assembler2.base;

import org.parker.mips.assembler2.util.ExpressionParser;

public abstract class OperandIndependentData extends Statement implements Data{

    private byte[] data;
    private long size;

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
        if(evaluated()) {
            if(data == null){
                data = toBytes();
                if(data.length != this.size){
                    throw new RuntimeException("Size of data does not equal size");
                }
            }
            return this.data;
        }else{
            throw new RuntimeException("Statement has not been evaluated yet illegal data access");
        }
    }

    @Override
    public final void evaluateOperands(ExpressionParser ep) {
        super.evaluateOperands(ep);
    }
}
