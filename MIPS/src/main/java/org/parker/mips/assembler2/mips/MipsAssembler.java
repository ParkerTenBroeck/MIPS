package org.parker.mips.assembler2.mips;

import org.parker.mips.assembler2.base.DataStatement;
import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.base.preprocessor.BasePreProcessor;
import org.parker.mips.assembler2.instruction.InstructionParser;
import org.parker.mips.assembler2.mips.instructions.parser.MipsInstructionParser;

import java.io.File;
import java.util.logging.Logger;

public class MipsAssembler extends BaseAssembler {

    private static final Logger LOGGER = Logger.getLogger(MipsAssembler.class.getName());

    private static final InstructionParser instructionParser = new MipsInstructionParser();

    @Override
    protected DataStatement getInstruction(String mnemonic) {
        return instructionParser.newInstance(mnemonic);
    }

    @Override
    protected BasePreProcessor createPreProcessor() {
        return new MipsPreProcessor(this);
    }
}
