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
package org.parker.retargetableassembler.util.linking;

import org.parker.retargetableassembler.base.Data;
import org.parker.retargetableassembler.base.assembler.BaseAssembler;
import org.parker.retargetableassembler.exception.LabelRedeclaredError;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AssemblyUnit implements Serializable {
    private long startingAddress = -1;
    private long size;
    private long alignment = 1;
    public final Map<String, Label> asuLabelMap;
    public final List<Data> data;

    public AssemblyUnit(List<Data> data, Map<String, Label> asuLabelMap){
        this.data = data;
        this.asuLabelMap = asuLabelMap;
    }
    public long getStartingAddress(){
        if(this.startingAddress == -1){
            return this.startingAddress;
        }
        return BaseAssembler.align(this.startingAddress, alignment);
    }

    public Map<String, Label> getAsuLabelMap() {
        return asuLabelMap;
    }

    public void addLabel(Label label){
        if (asuLabelMap.containsKey(label.mnemonic)) {
            throw new LabelRedeclaredError(label);
        }
        asuLabelMap.put(label.mnemonic, label);
    }

    public long getEndingAddress(){
        return getStartingAddress() + getSize();
    }
    public void setStartingAddress(long startingAddress){
        this.startingAddress = startingAddress;
    }
    public void setSize(long size){
        this.size = size;
    }

    public long getSize() {
        return this.size;
    }

    public void setAlignment(long alignment) {
        this.alignment = alignment;
    }
}
