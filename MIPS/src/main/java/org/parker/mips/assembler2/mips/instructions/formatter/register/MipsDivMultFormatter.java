package org.parker.mips.assembler2.mips.instructions.formatter.register;

import org.parker.mips.assembler2.instruction.StandardInstruction;
import org.parker.mips.assembler2.mips.MipsAssembler;
import org.parker.mips.assembler2.mips.instructions.formatter.MipsRegisterFormatter;
import org.parker.mips.assembler2.operand.OpRegister;

public enum MipsDivMultFormatter implements MipsRegisterFormatter {

    div(0b011010),
    divu(0b011011),
    mult(0b011000),
    multu(0b011001);

    private final int opCode;

    MipsDivMultFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, MipsAssembler assembler){
        int regs = 0;
        int regt = 0;
        if(instruction.argsLength() == 2){
            for(int i = 0; i < instruction.argsLength(); i ++){
                if(!(instruction.getArg(i) instanceof OpRegister)){
                    instruction.throwParameterTypeError(i, OpRegister.class);
                }
            }
            regs = ((Number) instruction.getArg(0).getValue()).intValue();
            regt = ((Number) instruction.getArg(1).getValue()).intValue();
        }else{
            instruction.throwParameterCountError(2);
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,regs, regt, 0, 0, opCode}, assembler);
    }
}