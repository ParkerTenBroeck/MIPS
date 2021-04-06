package org.parker.mips.assembler2.instruction.mips.formatter.immediate;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.exception.ParameterTypeError;
import org.parker.mips.assembler2.instruction.mips.formatter.MipsImmediateFormatter;
import org.parker.mips.assembler2.operand.OpImmediate;
import org.parker.mips.assembler2.operand.OpLong;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsLoadIFormatter implements MipsImmediateFormatter {

    lhi(0b011001),
    llo(0b011000);

    private final int opCode;

    MipsLoadIFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int regt = 0;
        int im = 0;

        if(operands.length == 2){
            if(!(operands[0] instanceof OpRegister)){
                throw new ParameterTypeError();
            }
            if(!(operands[1] instanceof OpImmediate)){
                throw new ParameterTypeError();
            }
            regt = ((Number) operands[0].getValue()).intValue();
            im = ((Number) operands[1].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,s,t,i
        MipsImmediateFormatter.super.encode(data, new int[]{opCode, 0, regt, im}, assembler);
    }
}
