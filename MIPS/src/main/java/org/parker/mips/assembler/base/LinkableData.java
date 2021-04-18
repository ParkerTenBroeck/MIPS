package org.parker.mips.assembler.base;

import org.parker.mips.assembler.base.assembler.Assembler;
import org.parker.mips.assembler.exception.LinkingException;

public interface LinkableData{
    void link(Assembler assembler, long sourceAddress) throws LinkingException;
}
