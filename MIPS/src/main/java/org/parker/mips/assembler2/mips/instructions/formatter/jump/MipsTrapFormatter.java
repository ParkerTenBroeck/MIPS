package org.parker.mips.assembler2.mips.instructions.formatter.jump;

import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.instruction.StandardInstruction;
import org.parker.mips.assembler2.mips.MipsAssembler;
import org.parker.mips.assembler2.operand.OpImmediate;

public enum MipsTrapFormatter implements org.parker.mips.assembler2.mips.instructions.formatter.MipsJumpFormatter {

    trap(0b011010);

    private final int opCode;

    MipsTrapFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, BaseAssembler assembler){
        int im = 0;

        if(instruction.argsLength() == 1){
                if(!(instruction.getArg(0) instanceof OpImmediate)){
                    instruction.throwParameterTypeError(0, OpImmediate.class);
                }
            im = ((Number) instruction.getArg(0).getValue()).intValue();
        }else{
            instruction.throwParameterCountError(1);
        }
        //Operand Order o,i
        org.parker.mips.assembler2.mips.instructions.formatter.MipsJumpFormatter.super.encode(data, new int[]{opCode, im}, assembler);
    }
}

