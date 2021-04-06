package org.parker.mips.assembler2.operand;

import org.parker.mips.assembler2.util.Label;
import org.parker.mips.assembler2.util.LinkType;

import java.util.HashMap;

public interface LinkableOperand extends Operand {
    void link(HashMap<String, Label> labelMap, long sourceAddr, LinkType linkType);
}
