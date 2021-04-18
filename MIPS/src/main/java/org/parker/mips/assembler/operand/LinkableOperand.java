package org.parker.mips.assembler.operand;

import org.parker.mips.assembler.exception.LinkingException;
import org.parker.mips.assembler.base.assembler.Assembler;
import org.parker.mips.assembler.util.linking.LinkType;


public interface LinkableOperand extends Operand {
    void link(Assembler assembler, long sourceAddr, LinkType linkType) throws LinkingException;
}
