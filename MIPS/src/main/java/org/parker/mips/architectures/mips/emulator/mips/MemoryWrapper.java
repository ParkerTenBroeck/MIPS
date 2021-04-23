package org.parker.mips.architectures.mips.emulator.mips;

public class MemoryWrapper implements org.parker.mips.util.Memory {

    @Override
    public long getSize() {
        return Memory.memory.length;
    }

    @Override
    public byte getByte(long address) {
        return (byte)Memory.superGetByte((int)address, false);
    }

    @Override
    public void clear() {

    }

    @Override
    public void setByte(long address, byte value) {
        Memory.superSetByte((int) address, value);
    }
}
