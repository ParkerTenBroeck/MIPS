package org.parker.mips.assembler2.instruction;

import org.parker.mips.assembler2.base.assembler.Assembler;
import org.parker.mips.assembler2.base.assembler.BaseAssembler;

public interface InstructionParser {

    StandardInstruction newInstance(String mnemonic, BaseAssembler assembler);
}
