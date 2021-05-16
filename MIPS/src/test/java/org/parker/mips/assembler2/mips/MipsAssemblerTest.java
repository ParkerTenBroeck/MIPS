package org.parker.mips.assembler2.mips;

import org.junit.jupiter.api.Test;
import org.parker.mips.architecture.emulator.mips.EmulatorMemory;
import org.parker.assembleride.core.MIPS;
import org.parker.retargetableassembler.base.assembler.Assembler;
import org.parker.mips.architecture.assembler.MipsAssembler;
import org.parker.mips.architecture.disassembler.MipsDisassembler;
import org.parker.retargetableassembler.util.Memory;
import org.parker.retargetableassembler.util.PagedMemory;

import java.io.File;

public class MipsAssemblerTest {


    @Test
    void mipsAssembler() {

        Assembler a = new MipsAssembler();

        File testDirectory = new File("src/test/resources/MIPS/Assembly/Linking Across Files");

        File[] files = testDirectory.listFiles();

        for(int i = 0; i < files.length; i ++ ) {
            if(files[i].isFile()){
                a.assemble(new File[]{files[i]});
            }else if(files[i].isDirectory()){
                a.assemble(files[i].listFiles());
            }
        }
    }
}