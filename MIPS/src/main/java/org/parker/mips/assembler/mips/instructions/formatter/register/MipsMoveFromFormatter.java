package org.parker.mips.assembler.mips.instructions.formatter.register;

import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.instruction.StandardInstruction;
import org.parker.mips.assembler.operand.OpRegister;
import org.parker.mips.assembler.mips.instructions.formatter.MipsRegisterFormatter;

public enum MipsMoveFromFormatter implements MipsRegisterFormatter {

    mfhi(0b010000),
    mflo(0b010010);

    private final int opCode;

    MipsMoveFromFormatter(int opCode){
        this.opCode = opCode;
    }

    @Override
    public void encode(byte[] data, StandardInstruction instruction, BaseAssembler assembler){
        int regd = 0;
        if(instruction.argsLength() == 1){
                if(!(instruction.getArg(0) instanceof OpRegister)){
                    instruction.throwParameterTypeError(0, OpRegister.class);
                }
            regd = ((Number) instruction.getArg(0).getValue()).intValue();
        }else{
            instruction.throwParameterCountError(1);
        }
        //Operand Order o,s,t,d,a,f
        MipsRegisterFormatter.super.encode(data, new int[]{0,0, 0, regd, 0, opCode}, assembler);
    }
}
