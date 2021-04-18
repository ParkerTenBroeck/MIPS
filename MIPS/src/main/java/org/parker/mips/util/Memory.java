package org.parker.mips.util;

public interface Memory {

    long getSize();

    long getLong(long address, boolean bigEndian);
    int getWord(long address, boolean bigEndian);
    short getShort(long address, boolean bigEndian);
    byte getByte(long address);

    void setWOrd(long address, long value, boolean bigEndian);
    void setWord(long address, int value,  boolean bigEndian);
    void setShort(long address, int value, boolean bigEndian);
    void setShort(long address, short value, boolean bigEndian);
    void setByte(long address, int value);
    void setByte(long address, byte value);

    void add(long address, byte[] bytes);

}
