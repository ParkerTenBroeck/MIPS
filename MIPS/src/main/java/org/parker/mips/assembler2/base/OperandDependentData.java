package org.parker.mips.assembler2.base;

import org.parker.mips.assembler2.util.ExpressionParser;

/**
 * Data which size depends on the operands present at assembly time
 * Memory Labels that have not been defined yet cannot be used as arguments
 */
public abstract class OperandDependentData extends Statement implements Data{
    private byte[] data;

    public OperandDependentData(){
    }

    protected abstract byte[] toBytes();

    @Override
    public final byte[] toBinary() {
        if(evaluated()) {
            if (data == null) {
                data = toBytes();
            }
            return data;
        }else{
            throw new RuntimeException("Statement has not been evaluated yet illegal data access");
        }
    }

    @Override
    public final long getSize() {
        return toBinary().length;
    }

    @Override
    public final void evaluateOperands(ExpressionParser ep) {
        super.evaluateOperands(ep);
    }
}
