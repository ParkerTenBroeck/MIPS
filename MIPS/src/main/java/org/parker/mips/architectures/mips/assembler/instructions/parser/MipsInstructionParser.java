package org.parker.mips.architectures.mips.assembler.instructions.parser;

import org.parker.mips.architectures.mips.assembler.instructions.formatter.immediate.*;
import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.exception.InstructionNotFoundError;
import org.parker.mips.assembler.instruction.InstructionFormatter;
import org.parker.mips.assembler.instruction.InstructionParser;
import org.parker.mips.assembler.instruction.StandardInstruction;
import org.parker.mips.assembler.instruction.StandardLinkableInstruction;
import org.parker.mips.architectures.mips.assembler.instructions.formatter.register.*;
import org.parker.mips.architectures.mips.assembler.instructions.formatter.jump.MipsJumpFormatter;
import org.parker.mips.architectures.mips.assembler.instructions.formatter.jump.MipsTrapFormatter;
import java.util.HashMap;

public class MipsInstructionParser implements InstructionParser {


    private static final HashMap<String, InstructionFormatter> formatterSndMnemonicMap = new HashMap<>();
    private static final HashMap<String, InstructionFormatter> formatterLinkMnemonicMap = new HashMap<>();

    static{

        //register format
        for(MipsArithLogFormatter i : MipsArithLogFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsDivMultFormatter i : MipsDivMultFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsJumpRFormatter i : MipsJumpRFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsMoveFromFormatter i : MipsMoveFromFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsMoveToFormatter i : MipsMoveToFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsShiftFormatter i : MipsShiftFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        //jump format
        for(MipsJumpFormatter i : MipsJumpFormatter.values())
            formatterLinkMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsTrapFormatter i : MipsTrapFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        //immediate format
        for(MipsArithLogIFormatter i : MipsArithLogIFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsBranchFormatter i : MipsBranchFormatter.values())
            formatterLinkMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsBranchZFormatter i : MipsBranchZFormatter.values())
            formatterLinkMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsLoadIFormatter i : MipsLoadIFormatter.values())
            formatterSndMnemonicMap.put(i.name().toLowerCase(), i);

        for(MipsLoadStoreFormatter i : MipsLoadStoreFormatter.values())
            formatterLinkMnemonicMap.put(i.name().toLowerCase(), i);
    }

    @Override
    public StandardInstruction newInstance(String mnemonic, BaseAssembler assembler) {
        if(formatterSndMnemonicMap.containsKey(mnemonic)){
            return new StandardInstruction(formatterSndMnemonicMap.get(mnemonic), assembler);
        }else if(formatterLinkMnemonicMap.containsKey(mnemonic)){
            return new StandardLinkableInstruction(formatterLinkMnemonicMap.get(mnemonic), assembler);
        }else{
            throw new InstructionNotFoundError("Instruction: " + mnemonic + " not found", null, 0,0);
        }
    }
}
