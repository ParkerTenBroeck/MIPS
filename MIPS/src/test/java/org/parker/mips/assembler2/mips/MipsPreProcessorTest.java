package org.parker.mips.assembler2.mips;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import org.parker.mips.assembler2.base.preprocessor.PreProcessedStatement;
import org.parker.mips.util.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MipsPreProcessorTest {


    private static class com{
        public File a;
        public File s;
    }

    @Test
    public void mipsPreProcessor(){
        MipsPreProcessor pp =  new MipsPreProcessor(new MipsAssembler());

        File[] allFiles = new File("src/test/resources/MIPS/PreProcessor/Comparison").listFiles();

        HashMap<String, com> mmap = new HashMap<String, com>();

        for(File f: allFiles){
            com te = mmap.getOrDefault(f.getName().substring(0,FileUtils.indexOfExtension(f.getName())), new com());

            if(FileUtils.getExtension(f).equals("asm")){
                te.a = f;
            }else if(FileUtils.getExtension(f).equals("s")){
                te.s = f;
            }
            mmap.put(f.getName().substring(0,FileUtils.indexOfExtension(f.getName())), te);
        }

        //List<PreProcessedStatement> statements = pp.preprocess(new File("src/test/resources/MIPS/PreProcessor/If Directive Test.asm"));

        for(Map.Entry<String, com> c:mmap.entrySet()){
            List<PreProcessedStatement> a = pp.preprocess(c.getValue().a);
            List<PreProcessedStatement> s = pp.preprocess(c.getValue().s);
            Assert.assertEquals("Not same size: ", a.size(), s.size());
            for(int i =0; i < a.size(); i ++){
                String temp = a.get(i).toString();
                Assert.assertEquals("fuck", a.get(i).toString(), s.get(i).toString());
            }
        }

        //for(int i = 0; i < statements.size(); i ++){
            //System.out.println(statements.get(i).toString());
        //}
    }

}