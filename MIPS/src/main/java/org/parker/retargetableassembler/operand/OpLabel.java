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
package org.parker.retargetableassembler.operand;

import org.parker.retargetableassembler.base.assembler.Assembler;
import org.parker.retargetableassembler.exception.LabelNotDeclaredError;
import org.parker.retargetableassembler.util.linking.Label;
import org.parker.retargetableassembler.util.linking.LinkType;

public class OpLabel extends OpLong implements LinkableOperand{

    protected final Label label;
    private Boolean linked = false;

    public OpLabel(Label label) {
        this.label = label;
    }

    @Override
    public Long getValue() {
        if(linked) {
            return super.getValue();
        }else{
            throw new RuntimeException("Im not Linked yet!!");
        }
    }

    @Override
    public void link(Assembler assembler, long sourceAddr, LinkType linkType) throws LabelNotDeclaredError {
        //if(labelMap.containsKey(labelMnemonic)){
            //Label label = labelMap.get(labelMnemonic);
            if(linkType == null){
                this.setValue(LinkType.ABSOLUTE_BYTE.link(sourceAddr, label.getAddress()));
            }else{
                this.setValue(linkType.link(sourceAddr, label.getAddress()));
            }
        //}else{
            //throw new LableNotDeclaredError("Label: " + labelMnemonic + " is not defined or not defined in the current scope");
        //}
        this.linked = true;
    }
}
