package org.parker.mips.util;

public class ByteMemory implements Memory{

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
