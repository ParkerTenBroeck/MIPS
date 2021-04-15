package org.parker.mips.assembler2.mips;

import org.junit.Assert;
import org.junit.Test;
import org.parker.mips.assembler2.base.assembler.Assembler;
import org.parker.mips.assembler2.base.preprocessor.IntermediateStatement;
import org.parker.mips.assembler2.base.preprocessor.PreProcessedStatement;

import java.io.File;
import java.util.List;

public class MipsPreProcessorTest {


    @Test
    public void aVoid(){
        File testDirectory = new File("src/test/resources/assembly/mips");
        Assembler a = new MipsAssembler();

        //a.assemble(new File[]{new File("src/test/resources/assembly/mips/If Directive Test.asm")});

        MipsPreProcessor pp =  new MipsPreProcessor((MipsAssembler) a);

        List<PreProcessedStatement> statements = pp.preprocess(new File("src/test/resources/assembly/mips/If Directive Test.asm"));

        for(int i = 0; i < statements.size(); i ++){
            System.out.println(statements.get(i).toString());
        }


        File[] files = testDirectory.listFiles();

        for(int i = 0; i < files.length; i ++ ) {
            if(files[i].isFile()){
                a.assemble(new File[]{files[i]});
            }else if(files[i].isDirectory()){
                a.assemble(files[i].listFiles());
            }
        }

        Assert.assertEquals("test", "test");
    }

}