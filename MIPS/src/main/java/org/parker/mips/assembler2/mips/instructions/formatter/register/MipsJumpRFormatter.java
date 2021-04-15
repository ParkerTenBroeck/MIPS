package org.parker.mips.assembler2.mips.instructions.formatter.register;

import org.parker.mips.assembler2.instruction.StandardInstruction;
import org.parker.mips.assembler2.mips.MipsAssembler;
import org.parker.mips.assembler2.mips.instructions.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.OpRegister;

public enum MipsJumpRFormatter implements MipsRegisterFormatter {

    jalr(0b001001),
    jr(0b001000);

    private final int opCode;

    MipsJumpRFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, MipsAssembler assembler){
        int regs = 0;
        if(instruction.argsLength() == 1){
                if(!(instruction.getArg(0) instanceof OpRegister)){
                    instruction.throwParameterTypeError(0, OpRegister.class);
                }
            regs = ((Number) instruction.getArg(0).getValue()).intValue();
        }else{
            instruction.throwParameterCountError(1);
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,regs, 0, 0, 0, opCode}, assembler);
    }
}
