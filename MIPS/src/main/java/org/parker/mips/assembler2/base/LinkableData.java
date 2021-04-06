package org.parker.mips.assembler2.base;

import org.parker.mips.assembler2.util.Label;

import java.util.HashMap;

public interface LinkableData{
    void link(HashMap<String, Label> labelMap, long sourceAddress);
}
