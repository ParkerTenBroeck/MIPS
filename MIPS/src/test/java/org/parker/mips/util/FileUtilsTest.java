package org.parker.mips.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class FileUtilsTest {

    private static class FileTest{

        public final String extension;
        public final String withoutExtension;
        public final int indexOfExtension;
        public final int indexOfLastSeperator;

        public FileTest(String extension, String withoutExtension, int indexOfExtension, int indexOfLastSeperator){
            this.extension = extension;
            this.withoutExtension = withoutExtension;
            this.indexOfExtension = indexOfExtension;
            this.indexOfLastSeperator = indexOfLastSeperator;
        }
    }

    public static final Map<String, FileTest> asd = new HashMap<>();

    static{
        asd.put("C:\\GitHub\\MIPS\\MIPS\\build\\reports\\tests\\test\\index.html",
                new FileTest("html",
                        "C:\\GitHub\\MIPS\\MIPS\\build\\reports\\tests\\test\\index",
                        50,
                        44));

        asd.put("C:\\Users\\notteach.SCHOOL\\OneDrive - School Board of Location\\Courses\\3042 - bSeason\\P1 - COURSE1-9\\5 - Assembly Programming\\loop example.asm",
                new FileTest("asm",
                        "C:\\Users\\notteach.SCHOOL\\OneDrive - School Board of Location\\Courses\\3042 - bSeason\\P1 - COURSE1-9\\5 - Assembly Programming\\loop example",
                        136,
                        123));
    }

    @Test
    public void getExtension() {
        for(Map.Entry<String, FileTest> ft :asd.entrySet()){
            Assert.assertEquals("Extension Path: " + ft.getKey(), FileUtils.getExtension(ft.getKey()), ft.getValue().extension);
        }
    }

    @Test
    public void removeExtension() {
        for(Map.Entry<String, FileTest> ft :asd.entrySet()){
            Assert.assertEquals("Without Extension Path: " + ft.getKey(), FileUtils.removeExtension(ft.getKey()), ft.getValue().withoutExtension);
        }
    }

    @Test
    public void indexOfExtension() {
        for(Map.Entry<String, FileTest> ft :asd.entrySet()){
            Assert.assertEquals("Index of Extension Path: " + ft.getKey(), FileUtils.indexOfExtension(ft.getKey()), ft.getValue().indexOfExtension);
        }
    }

    @Test
    public void indexOfLastSeparator() {
        for(Map.Entry<String, FileTest> ft :asd.entrySet()){
            Assert.assertEquals("Index of Last Seperator Path: " + ft.getKey(), FileUtils.indexOfLastSeparator(ft.getKey()), ft.getValue().indexOfLastSeperator);
        }
    }
}