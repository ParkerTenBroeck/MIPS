package org.parker.mips.assembler2.util.linking;

import org.parker.mips.assembler2.base.Data;
import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.exception.LabelRedeclaredError;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AssemblyUnit implements Serializable {
    private long startingAddress = -1;
    private long size;
    private long alignment = 1;
    public final Map<String, Label> asuLabelMap;
    public final List<Data> data;

    public AssemblyUnit(List<Data> data, Map<String, Label> asuLabelMap){
        this.data = data;
        this.asuLabelMap = asuLabelMap;
    }
    public long getStartingAddress(){
        if(this.startingAddress == -1){
            return this.startingAddress;
        }
        return BaseAssembler.align(this.startingAddress, alignment);
    }

    public Map<String, Label> getAsuLabelMap() {
        return asuLabelMap;
    }

    public void addLabel(Label label){
        if (asuLabelMap.containsKey(label.mnemonic)) {
            throw new LabelRedeclaredError(label);
        }
        asuLabelMap.put(label.mnemonic, label);
    }

    public long getEndingAddress(){
        return getStartingAddress() + getSize();
    }
    public void setStartingAddress(long startingAddress){
        this.startingAddress = startingAddress;
    }
    public void setSize(long size){
        this.size = size;
    }

    public long getSize() {
        return this.size;
    }

    public void setAlignment(long alignment) {
        this.alignment = alignment;
    }
}
