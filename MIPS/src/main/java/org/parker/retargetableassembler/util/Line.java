/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.retargetableassembler.util;

import java.io.File;
import java.io.Serializable;

public class Line implements Serializable {

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
        this.endingLine = endingLine;
    }
    public void setStartingIndex(int startingIndex){
        this.startingIndex = startingIndex;
    }
    public void setEndingIndex(int endingIndex){
        this.endingIndex = endingIndex;
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

    public int getHumanLineNumber() {
        return startingLine + 1;
    }
    public int getLineNumber(){
        return startingLine;
    }

    public File getFile() {
        return this.parentFile;
    }
}
