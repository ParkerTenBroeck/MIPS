package org.parker.mips.assembler2.util;

import java.util.HashMap;

public class PagedMemory {

    private final HashMap<Integer, byte[]> memory = new HashMap();


    public PagedMemory(){

    }

    public void add(int address, byte[] data){
        for(int i = 0; i < data.length; i ++){
            put(address + i, data[i]);
        }
    }

    public void put(int address, byte data){
        if(!memory.containsKey(address >> 12)){
            memory.put(address >> 12, new byte[0b111111111111]);
        }
        memory.get(address >> 12)[address & 0b111111111111] = data;
    }

    public byte[] getPage(int i) {
        return memory.get(i);
    }
}
