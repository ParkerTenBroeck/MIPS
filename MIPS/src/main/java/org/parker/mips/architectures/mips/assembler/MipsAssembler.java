package org.parker.mips.architectures.mips.assembler;

import org.parker.mips.assembler.base.DataStatement;
import org.parker.mips.architectures.mips.assembler.instructions.parser.MipsInstructionParser;
import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.base.preprocessor.BasePreProcessor;
import org.parker.mips.assembler.instruction.InstructionParser;

import java.util.logging.Logger;

public class MipsAssembler extends BaseAssembler {

    private static final Logger LOGGER = Logger.getLogger(MipsAssembler.class.getName());

    private static final InstructionParser instructionParser = new MipsInstructionParser();

    @Override
    protected DataStatement getInstruction(String mnemonic) {
        return instructionParser.newInstance(mnemonic, this);
    }

    @Override
    protected BasePreProcessor createPreProcessor() {
        return new MipsPreProcessor(this);
    }
}
