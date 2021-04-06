package org.parker.mips.assembler2.util;

import org.parker.mips.assembler2.base.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssemblyUnit {
    public final long startingAddress;
    public final long size;
    public final Map<String, Label> asuLabelMap;
    public final List<Data> data;

    public AssemblyUnit(long startingAddress, long size, List<Data> data, Map<String, Label> asuLabelMap){
        this.startingAddress = startingAddress;
        this.size = size;
        this.data = data;
        this.asuLabelMap = asuLabelMap;
    }

    public AssemblyUnit(int startingAddress, long size, List<Data> data, Map<String, Label> asuLabelMap){
        if(startingAddress == -1){
            this.startingAddress = -1;
        }else{
            this.startingAddress = startingAddress & 0xFFFFFFFFL;
        }
        this.size = size;
        this.data = data;
        this.asuLabelMap = asuLabelMap;
    }
}
