package org.parker.mips.assembler2.util;

public class Register {

    public final int regNumber;

    public Register(int value){
        if(value > 31 || value < 0){
            throw new IllegalArgumentException("Incorrect Register Number: " + value);
        }else{
            regNumber = value;
        }
    }

    @Override
    public String toString() {
        return "$" + regNumber;
    }
}
