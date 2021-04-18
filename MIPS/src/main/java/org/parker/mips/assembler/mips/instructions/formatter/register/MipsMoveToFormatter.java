package org.parker.mips.assembler.mips.instructions.formatter.register;

import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.instruction.StandardInstruction;
import org.parker.mips.assembler.operand.OpRegister;
import org.parker.mips.assembler.mips.instructions.formatter.MipsRegisterFormatter;

public enum MipsMoveToFormatter implements MipsRegisterFormatter {

    mthi(0b010001),
    mtlo(0b010011);

    private final int opCode;

    MipsMoveToFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, BaseAssembler assembler){
        int regs = 0;
        if(instruction.argsLength() == 3){
                if(!(instruction.getArg(0) instanceof OpRegister)){
                    instruction.throwParameterTypeError(0, OpRegister.class);
                }
            regs = ((Number) instruction.getArg(0).getValue()).intValue();
        }else{
            instruction.throwParameterCountError(3);
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,regs, 0, 0, 0, opCode}, assembler);
    }
}
