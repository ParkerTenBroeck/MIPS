package org.parker.mips.assembler2.mips.instructions.formatter.jump;

import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.instruction.StandardInstruction;
import org.parker.mips.assembler2.mips.MipsAssembler;
import org.parker.mips.assembler2.operand.OpLong;
import org.parker.mips.assembler2.util.linking.LinkType;

public enum MipsJumpFormatter implements org.parker.mips.assembler2.mips.instructions.formatter.MipsJumpFormatter {

    j(0b000010),
    jal(0b000011);

    private final int opCode;

    MipsJumpFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, BaseAssembler assembler){
        int im = 0;

        if(instruction.argsLength() == 1){
                if(!(instruction.getArg(0) instanceof OpLong)){
                    instruction.throwParameterTypeError(0,OpLong.class);
                }
            im = ((Number) instruction.getArg(0).getValue()).intValue();
        }else{
            instruction.throwParameterCountError(1);
        }
        //Operand Order o,i
        org.parker.mips.assembler2.mips.instructions.formatter.MipsJumpFormatter.super.encode(data, new int[]{opCode, im}, assembler);
    }

    @Override
    public LinkType[] getLinkTypes() {
        return new LinkType[]{LinkType.RELATIVE_WORD};
    }


}
