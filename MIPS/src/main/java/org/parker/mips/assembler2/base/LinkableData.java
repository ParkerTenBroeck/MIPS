package org.parker.mips.assembler2.base;

import org.parker.mips.assembler2.base.assembler.Assembler;
import org.parker.mips.assembler2.exception.LinkingException;

public interface LinkableData{
    void link(Assembler assembler, long sourceAddress) throws LinkingException;
}
