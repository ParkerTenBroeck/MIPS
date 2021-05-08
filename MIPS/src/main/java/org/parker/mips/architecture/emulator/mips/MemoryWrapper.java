package org.parker.mips.architecture.emulator.mips;

import org.parker.retargetableassembler.util.Memory;

public class MemoryWrapper implements Memory {

    @Override
    public long getSize() {
        return EmulatorMemory.memory.length;
    }

    @Override
    public byte getByte(long address) {
        return (byte) EmulatorMemory.superGetByte((int)address, false);
    }

    @Override
    public void clear() {

    }

    @Override
    public void setByte(long address, byte value) {
        EmulatorMemory.superSetByte((int) address, value);
    }
}
