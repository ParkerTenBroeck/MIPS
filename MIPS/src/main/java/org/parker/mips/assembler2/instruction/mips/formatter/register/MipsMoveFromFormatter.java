package org.parker.mips.assembler2.instruction.mips.formatter.register;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsMoveFromFormatter implements MipsRegisterFormatter {

    mfhi(0b010000),
    mflo(0b010010);

    private final int opCode;

    MipsMoveFromFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int regd = 0;
        if(operands.length == 1){
                if(!(operands[0] instanceof OpRegister)){

                }
            regd = ((Number) operands[0].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,0, 0, regd, 0, opCode}, assembler);
    }
}
