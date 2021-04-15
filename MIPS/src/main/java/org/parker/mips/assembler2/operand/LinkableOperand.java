package org.parker.mips.assembler2.operand;

import org.parker.mips.assembler2.base.assembler.Assembler;
import org.parker.mips.assembler2.exception.LinkingException;
import org.parker.mips.assembler2.util.linking.LinkType;


public interface LinkableOperand extends Operand {
    void link(Assembler assembler, long sourceAddr, LinkType linkType) throws LinkingException;
}
