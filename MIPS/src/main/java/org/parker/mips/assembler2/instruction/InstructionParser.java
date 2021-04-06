package org.parker.mips.assembler2.instruction;

public interface InstructionParser {

    StandardInstruction newInstance(String mnemonic);
}
