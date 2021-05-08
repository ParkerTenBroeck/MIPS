/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.retargetableassembler.instruction;

import org.parker.retargetableassembler.base.OperandIndependentData;
import org.parker.retargetableassembler.operand.OpImmediate;
import org.parker.retargetableassembler.operand.OpLabel;
import org.parker.retargetableassembler.operand.OpRegister;
import org.parker.retargetableassembler.base.assembler.BaseAssembler;
import org.parker.retargetableassembler.exception.AssemblerError;
import org.parker.retargetableassembler.operand.Operand;
import org.parker.retargetableassembler.util.Register;
import org.parker.retargetableassembler.util.linking.Label;

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
