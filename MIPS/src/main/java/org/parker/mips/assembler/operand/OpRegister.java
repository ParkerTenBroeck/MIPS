package org.parker.mips.assembler.operand;

public class OpRegister implements Operand {

    private final int regNumber;

    public OpRegister(int regNum){
        regNumber = regNum;
    }

    @Override
    public Integer getValue() {
        return regNumber;
    }
}