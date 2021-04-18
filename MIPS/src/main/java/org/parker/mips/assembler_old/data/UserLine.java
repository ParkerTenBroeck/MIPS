/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.assembler_old.data;

/**
 *
 * @author parke
 */
public class UserLine {

    public String line;
    public int realLineNumber;

    public UserLine(String line) {
        this.line = line;
    }

    public UserLine(int realLineNumber) {
        this.realLineNumber = realLineNumber;
    }

    public UserLine(String line, int realLineNumber) {
        this.line = line;
        this.realLineNumber = realLineNumber;
    }

    public UserLine() {
    }

    @Override
    public String toString() {
        return realLineNumber + ":" + line;
    }
}
