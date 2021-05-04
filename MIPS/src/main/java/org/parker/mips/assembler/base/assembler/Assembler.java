package org.parker.mips.assembler.base.assembler;

import org.parker.mips.util.Memory;

import java.io.File;

public interface Assembler {

    Memory assemble(File[] files);
}
