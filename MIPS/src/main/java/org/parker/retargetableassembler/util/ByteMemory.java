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
package org.parker.retargetableassembler.util;

import org.parker.retargetableassembler.util.Memory;

public class ByteMemory implements Memory {

    private final byte[] memory;

    public ByteMemory(byte[] mem){
        this.memory = mem;
    }

    @Override
    public final long getSize() {
        return memory.length;
    }

    @Override
    public final byte getByte(long address) {
        return memory[(int) address];
    }

    @Override
    public void clear() {
        for(int i = 0; i < memory.length; i ++){
            memory[i] = 0;
        }
    }

    @Override
    public void setByte(long address, byte value) {
        memory[(int) address] = value;
    }
}
