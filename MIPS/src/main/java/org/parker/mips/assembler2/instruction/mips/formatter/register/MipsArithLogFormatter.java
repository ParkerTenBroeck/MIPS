package org.parker.mips.assembler2.instruction.mips.formatter.register;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsArithLogFormatter implements MipsRegisterFormatter {

    add(0b100000),
    addu(0b100001),
    and(0b100100),
    nor(0b100111),
    or(0b100101),
    sub(0b100010),
    subu(0b100011),
    xor(0b100110),
    slt(0b101010),
    sltu(0b101001);

    private final int opCode;

    private MipsArithLogFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int regd = 0;
        int regs = 0;
        int regt = 0;
        if(operands.length == 3){
            for(int i = 0; i < operands.length; i ++){
                if(!(operands[i] instanceof OpRegister)){

                }
            }
            regd = (Integer) operands[0].getValue();
            regs = (Integer) operands[1].getValue();
            regt = (Integer) operands[2].getValue();
        }else{
            //error
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,regs, regt, regd, 0, opCode}, assembler);
    }
}
