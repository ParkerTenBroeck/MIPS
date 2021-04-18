/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.assembler_old.data;


public class MemoryLable {

    final public UserLine line;
    final public String name;
    int byteAddress;
    final int lableIndex; //the number of lables that have come before this atarting at the top of the file

    public MemoryLable(UserLine us, int lableIndex) {
        this.line = us;
        this.lableIndex = lableIndex;

        String temp;
        try {
            temp = us.line.replace(":", "").trim();
        } catch (Exception e) {
            temp = "";
        }
        this.name = temp;
    }

    public void setByteAddress(int nAddress) {
        this.byteAddress = nAddress;
    }
    
    public int getByteAddress(){
        return this.byteAddress;
    }
};