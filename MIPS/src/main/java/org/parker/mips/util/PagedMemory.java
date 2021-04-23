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
