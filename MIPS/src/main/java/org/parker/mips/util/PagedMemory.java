package org.parker.mips.util;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class PagedMemory implements Memory {

    private final HashMap<Integer, byte[]> memory = new HashMap();


    public PagedMemory(){

    }

    @Override
    public void add(long address, byte[] data){
        for(int i = 0; i < data.length; i ++){
            setByte(address + i, data[i]);
        }
    }


    public byte[] getPage(int i) {
        return memory.get(i);
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public long getLong(long address, boolean bigEndian) {


        //ByteBuffer.
        return 0;
    }

    @Override
    public int getWord(long address, boolean bigEndian) {
        return 0;
    }

    @Override
    public short getShort(long address, boolean bigEndian) {
        return 0;
    }

    @Override
    public byte getByte(long address) {
        return 0;
    }

    @Override
    public void setWOrd(long address, long value, boolean bigEndian) {

    }

    @Override
    public void setWord(long address, int value, boolean bigEndian) {

    }

    @Override
    public void setShort(long address, int value, boolean bigEndian) {

    }

    @Override
    public void setShort(long address, short value, boolean bigEndian) {

    }

    @Override
    public void setByte(long address, int value) {
        if(!memory.containsKey(((int)address) >> 12)){
            memory.put(((int)address) >> 12, new byte[0b111111111111]);
        }
        memory.get(((int)address) >> 12)[ ((int)address) & 0b1000000000000] = (byte)value;
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
