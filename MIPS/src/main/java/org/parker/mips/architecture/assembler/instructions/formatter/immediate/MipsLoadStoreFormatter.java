package org.parker.mips.architecture.assembler.instructions.formatter.immediate;

import org.parker.retargetableassembler.base.assembler.BaseAssembler;
import org.parker.retargetableassembler.instruction.StandardInstruction;
import org.parker.mips.architecture.assembler.instructions.formatter.MipsImmediateFormatter;
import org.parker.retargetableassembler.operand.OpLong;
import org.parker.retargetableassembler.operand.OpRegister;
import org.parker.retargetableassembler.util.linking.LinkType;

public enum MipsLoadStoreFormatter implements MipsImmediateFormatter {

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
    public void encode(byte[] data, StandardInstruction instruction, BaseAssembler assembler){
        int regt = 0;
        int im = 0;
        int regs = 0;

        if(instruction.argsLength() == 3){
            if(!(instruction.getArg(0) instanceof OpRegister)){
                instruction.throwParameterTypeError(0, OpRegister.class);
            }
            if(!(instruction.getArg(1) instanceof OpLong)){
                instruction.throwParameterTypeError(1, OpLong.class);
            }
            if(!(instruction.getArg(2) instanceof OpRegister)){
                instruction.throwParameterTypeError(2, OpRegister.class);
            }
            regt = ((Number) instruction.getArg(0).getValue()).intValue();
            im = ((Number) instruction.getArg(1).getValue()).intValue();
            regs = ((Number) instruction.getArg(2).getValue()).intValue();
        }else{
            instruction.throwParameterCountError(3);
        }
        //Operand Order o,s,t,i
        MipsImmediateFormatter.super.encode(data, new int[]{opCode, regs, regt, im}, assembler);
    }

    @Override
    public LinkType[] getLinkTypes() {
        return new LinkType[]{null, LinkType.ABSOLUTE_BYTE, null};
    }
}
