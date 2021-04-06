package org.parker.mips.assembler2.instruction.mips.formatter.immediate;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.exception.ParameterTypeError;
import org.parker.mips.assembler2.operand.OpLong;
import org.parker.mips.assembler2.operand.OpRegister;
import org.parker.mips.assembler2.operand.Operand;
import org.parker.mips.assembler2.util.LinkType;

public enum MipsLoadStoreFormatter implements MipsMemoryAccessFormatter {

    lb(0b100000),
    lbu(0b100100),
    lh(0b100001),
    lhu(0b100101),
    lw(0b100011),
    sb(0b101000),
    sh(0b101001),
    sw(0b101011);

    private final int opCode;

    MipsLoadStoreFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int regt = 0;
        int im = 0;
        int regs = 0;

        if(operands.length == 3){
            if(!(operands[0] instanceof OpRegister)){
                throw new ParameterTypeError();
            }
            if(!(operands[1] instanceof OpLong)){
                throw new ParameterTypeError();
            }
            if(!(operands[2] instanceof OpRegister)){
                throw new ParameterTypeError();
            }
            regt = ((Number) operands[0].getValue()).intValue();
            im = ((Number) operands[1].getValue()).intValue();
            regs = ((Number) operands[2].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,s,t,i
        MipsMemoryAccessFormatter.super.encode(data, new int[]{opCode, regs, regt, im}, assembler);
    }

    @Override
    public LinkType[] getLinkTypes() {
        return new LinkType[]{null, LinkType.ABSOLUTE_BYTE, null};
    }
}
