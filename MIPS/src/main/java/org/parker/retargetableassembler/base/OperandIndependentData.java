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
package org.parker.retargetableassembler.base;

public abstract class OperandIndependentData<ArgType> extends DataStatement<ArgType>{

    private byte[] data;
    private final long size;

    public OperandIndependentData(int size){
        this.size = size;
    }

    @Override
    public final long getSize() {
        return this.size;
    }

    public abstract byte[] toBytes();

    @Override
    public final byte[] toBinary() {
        if(data == null){
            data = toBytes();
            if(data.length != this.size){
                throw new RuntimeException("Size of data does not equal size");
            }
        }
        return this.data;
    }
}
