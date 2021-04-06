package org.parker.mips.assembler2.instruction.mips.formatter.register;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsDivMultFormatter implements MipsRegisterFormatter {

    div(0b011010),
    divu(0b011011),
    mult(0b011000),
    multu(0b011001);

    private final int opCode;

    MipsDivMultFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int regs = 0;
        int regt = 0;
        if(operands.length == 2){
            for(int i = 0; i < operands.length; i ++){
                if(!(operands[i] instanceof OpRegister)){
                    //error
                }
            }
            regs = ((Number) operands[0].getValue()).intValue();
            regt = ((Number) operands[1].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,regs, regt, 0, 0, opCode}, assembler);
    }
}