package org.parker.mips.assembler2.instruction.mips.formatter.register;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.OpImmediate;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsShiftFormatter implements MipsRegisterFormatter {

    sll(0b000000),
    sra(0b000011),
    srl(0b000010);

    private final int opCode;

    private MipsShiftFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int regd = 0;
        int regt = 0;
        int a = 0;
        if(operands.length == 3){

                if(!(operands[0] instanceof OpRegister)){

                }else if(!(operands[1] instanceof OpRegister)){

                }else if(!(operands[2] instanceof OpImmediate)){

                }
            regd = ((Number) operands[0].getValue()).intValue();
            regt = ((Number) operands[1].getValue()).intValue();
            a = ((Number) operands[2].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,0, regt, regd, a, opCode}, assembler);
    }
}
