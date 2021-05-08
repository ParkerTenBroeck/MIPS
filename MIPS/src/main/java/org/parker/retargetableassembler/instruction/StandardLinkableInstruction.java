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

import org.parker.retargetableassembler.operand.LinkableOperand;
import org.parker.retargetableassembler.base.LinkableData;
import org.parker.retargetableassembler.base.assembler.Assembler;
import org.parker.retargetableassembler.base.assembler.BaseAssembler;
import org.parker.retargetableassembler.util.linking.LinkType;

public class StandardLinkableInstruction extends StandardInstruction implements LinkableData {

    public StandardLinkableInstruction(InstructionFormatter isf, BaseAssembler assembler) {
        super(isf, assembler);
    }

    @Override
    public void link(Assembler assembler, long sourceAddress) {

        LinkType[] linkTypes = isf.getLinkTypes();

        for(int i = 0; i < linkTypes.length; i ++){
            if(linkTypes[i] == null) continue;

            if(getArg(i) instanceof LinkableOperand){
                try {
                    ((LinkableOperand) getArg(i)).link(assembler, sourceAddress, linkTypes[i]);
                }catch(Exception e){
                    this.throwLinkingException(i, e);
                }
            }
        }
    }
}
