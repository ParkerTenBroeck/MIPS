package org.parker.mips.assembler2.mips;

import org.junit.jupiter.api.Test;
import org.parker.mips.MIPS;
import org.parker.mips.assembler.base.assembler.Assembler;
import org.parker.mips.assembler.mips.MipsAssembler;
import org.parker.mips.assembler.mips.MipsDisassembler;

import java.io.File;

public class MipsAssemblerTest {


    public static void main(String... args){
        MIPS.main(new String[0]);
        new MipsAssembler().assemble(new File("C:\\GitHub\\MIPS\\MIPS/src/test/resources/MIPS/Assembly/Linking Across Files/Program Demonstrating Linking").listFiles());
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