package org.parker.mips.assembler2.instruction.mips.formatter.jump;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.operand.OpImmediate;
import org.parker.mips.assembler2.operand.OpLong;
import org.parker.mips.assembler2.operand.Operand;

public enum MipsTrapFormatter implements org.parker.mips.assembler2.instruction.mips.formatter.MipsJumpFormatter {

    trap(0b011010);

    private final int opCode;

    MipsTrapFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int im = 0;

        if(operands.length == 1){
            for(int i = 0; i < operands.length; i ++){
                if(!(operands[i] instanceof OpImmediate)){

                }
            }
            im = ((Number) operands[0].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,i
        org.parker.mips.assembler2.instruction.mips.formatter.MipsJumpFormatter.super.encode(data, new int[]{opCode, im}, assembler);
    }
}

