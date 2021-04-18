package org.parker.mips.assembler.instruction;

import org.parker.mips.assembler.base.OperandIndependentData;
import org.parker.mips.assembler.operand.OpImmediate;
import org.parker.mips.assembler.operand.OpLabel;
import org.parker.mips.assembler.operand.OpRegister;
import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.exception.AssemblerError;
import org.parker.mips.assembler.operand.Operand;
import org.parker.mips.assembler.util.Register;
import org.parker.mips.assembler.util.linking.Label;

public class StandardInstruction extends OperandIndependentData<Operand> {

    final InstructionFormatter isf;
    final BaseAssembler assembler;

    public StandardInstruction(InstructionFormatter isf, BaseAssembler assembler) {
        super(isf.getInstructionSize());
        this.isf = isf;
        this.assembler = assembler;
    }


    @Override
    protected Operand evaluateArgument(int index, Object result) {
        if(result instanceof Register){
            return new OpRegister(((Register) result).regNumber);
        }else if(result instanceof Integer || result instanceof Long || result instanceof Byte || result instanceof Short){
            return new OpImmediate(((Number) result).longValue());
        }else if(result instanceof Label){
            return new OpLabel(((Label) result));
        }else{
            return super.evaluateArgument(index, result);
        }
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[(int) getSize()];
        try {
            isf.encode(bytes, this, assembler);
        }catch (Exception e){
            throw new AssemblerError("Failed to convert instruction to bytes", getLine(), e);
        }
        return bytes;
    }
}
