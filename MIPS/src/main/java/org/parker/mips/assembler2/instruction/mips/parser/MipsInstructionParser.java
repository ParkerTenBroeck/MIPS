package org.parker.mips.assembler2.instruction.mips.parser;

import org.parker.mips.assembler2.instruction.InstructionFormatter;
import org.parker.mips.assembler2.instruction.InstructionParser;
import org.parker.mips.assembler2.instruction.StandardInstruction;
import org.parker.mips.assembler2.instruction.StandardLinkableInstruction;
import org.parker.mips.assembler2.instruction.mips.formatter.immediate.*;
import org.parker.mips.assembler2.instruction.mips.formatter.jump.MipsJumpFormatter;
import org.parker.mips.assembler2.instruction.mips.formatter.jump.MipsTrapFormatter;
import org.parker.mips.assembler2.instruction.mips.formatter.register.*;

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
    public StandardInstruction newInstance(String mnemonic) {
        if(formatterSndMnemonicMap.containsKey(mnemonic)){
            return new StandardInstruction(formatterSndMnemonicMap.get(mnemonic));
        }else if(formatterLinkMnemonicMap.containsKey(mnemonic)){
            return new StandardLinkableInstruction(formatterLinkMnemonicMap.get(mnemonic));
        }else{
            throw new RuntimeException("asda112121423243242342344545: " + mnemonic);
        }
    }
}
