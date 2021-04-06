package org.parker.mips.assembler2.util;

import java.io.File;

public class Line {
    private String line;
    private int startingLine;
    private int endingLine;
    private int startingIndex;
    private int endingIndex;
    private File parentFile;
    private Line parentLine = null;

    public void setLine(String line){
        this.line = line;
    }
    public void setStartingLine(int startingLine){
        this.startingLine = startingLine;
    }
    public void setEndingLine(int endingLine){
        this.startingLine = endingLine;
    }
    public void setStartingIndex(int startingIndex){
        this.startingLine = startingIndex;
    }
    public void setEndingIndex(int endingIndex){
        this.startingLine = endingIndex;
    }
    public void setParentFile(File parentFile){
        this.parentFile = parentFile;
    }

    public String getLine(){
        return this.line;
    }

    public void trim(){
        int len = line.length();
        int st = 0;
        int off = 0;      /* avoid getfield opcode */
        byte[] val = line.getBytes();    /* avoid getfield opcode */

        while ((st < len) && (val[off + st] <= ' ')) {
            st++;
            startingIndex++;
        }
        while ((st < len) && (val[off + len - 1] <= ' ')) {
            len--;
            endingIndex--;
        }
        line =  ((st > 0) || (len < line.length())) ? line.substring(st, len) : line;
    }

    public void setParent(Line line){
        this.parentLine = line;
    }
}
