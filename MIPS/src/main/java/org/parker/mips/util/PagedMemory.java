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
package org.parker.mips.util;

import java.util.HashMap;
import java.util.Map;

public class PagedMemory implements Memory {

    private final HashMap<Integer, byte[]> memory = new HashMap();
    public final int pageSize = 4096;

    public PagedMemory(){

    }

    public byte[] getPage(int i) {
        return memory.get(i);
    }

    @Override
    public long getSize() {
        long largestAddress = 0;
        for(Map.Entry<Integer, byte[]> entry: memory.entrySet()){
            if(entry.getKey() > largestAddress){
                largestAddress = entry.getKey();
            }
        }
        return (largestAddress + 1) * pageSize;
    }

    @Override
    public byte getByte(long address) {
        if(!memory.containsKey(((int)address) >> 12)){
            memory.put(((int)address) >> 12, new byte[0b1000000000000]);
        }
        return memory.get(((int)address) >> 12)[ ((int)address) & 0b111111111111];
    }

    @Override
    public void clear() {
        memory.clear();
    }

    @Override
    public void setByte(long address, byte value) {
        if(!memory.containsKey(((int)address) >> 12)){
            memory.put(((int)address) >> 12, new byte[0b1000000000000]);
        }
        memory.get(((int)address) >> 12)[ ((int)address) & 0b111111111111] = (byte)value;
    }

    public int getPageCount() {
        return memory.entrySet().size();
    }
}
