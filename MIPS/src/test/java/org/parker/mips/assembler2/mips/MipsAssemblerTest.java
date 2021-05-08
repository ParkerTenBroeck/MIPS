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


    public static void main(String... args){
        MIPS.main(new String[0]);
        Memory m = new MipsAssembler().assemble(new File[]{new File("C:\\Users\\parke\\OneDrive\\Documents\\MIPS\\Projects\\bad-apple\\bad-apple - Copy (2).asm")});
        PagedMemory pMemory = (PagedMemory) m;

        if(pMemory != null) {
            byte[] temp = new byte[pMemory.getPageCount() * 4096];
            for (int p = 0; p < pMemory.getPageCount(); p++) {
                byte[] page = pMemory.getPage(p);
                for (int i = 0; i < 4096; i++) {
                    temp[i + p * 4096] = page[i];
                }
            }
            EmulatorMemory.setMemory(temp);
        }

        MipsDisassembler.disassemble();
    }

    @Test
    void bruh(){
        MIPS.main(new String[0]);
        new MipsAssembler().assemble(new File("src/test/resources/MIPS/Assembly/Linking Across Files/Program Demonstrating Linking").listFiles());
        MipsDisassembler.disassemble();
        while(true){

        }
    }

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