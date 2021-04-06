package org.parker.mips.assembler2.instruction.mips.formatter.register;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsMoveToFormatter implements MipsRegisterFormatter {

    mthi(0b010001),
    mtlo(0b010011);

    private final int opCode;

    MipsMoveToFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int regs = 0;
        if(operands.length == 3){
                if(!(operands[0] instanceof OpRegister)){

                }
            regs = ((Number) operands[0].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,regs, 0, 0, 0, opCode}, assembler);
    }
}
