package org.parker.mips.assembler.instruction;

import org.parker.mips.assembler.base.assembler.BaseAssembler;

public interface InstructionParser {

    StandardInstruction newInstance(String mnemonic, BaseAssembler assembler);
}
