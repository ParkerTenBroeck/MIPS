package org.parker.mips.assembler2.instruction.mips.formatter.jump;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.exception.ParameterTypeError;
import org.parker.mips.assembler2.operand.OpLong;
import org.parker.mips.assembler2.operand.Operand;
import org.parker.mips.assembler2.util.LinkType;

public enum MipsJumpFormatter implements org.parker.mips.assembler2.instruction.mips.formatter.MipsJumpFormatter {

    j(0b000010),
    jal(0b000011);

    private final int opCode;

    MipsJumpFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, Operand[] operands, Assembler assembler){
        int im = 0;

        if(operands.length == 1){
                if(!(operands[0] instanceof OpLong)){
                    throw new ParameterTypeError();
                }
            im = ((Number) operands[0].getValue()).intValue();
        }else{
            //error
        }
        //Operand Order o,i
        org.parker.mips.assembler2.instruction.mips.formatter.MipsJumpFormatter.super.encode(data, new int[]{opCode, im}, assembler);
    }

    @Override
    public LinkType[] getLinkTypes() {
        return new LinkType[]{LinkType.RELATIVE_WORD};
    }


}
