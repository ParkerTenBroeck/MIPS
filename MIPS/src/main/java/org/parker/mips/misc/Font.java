package org.parker.mips.misc;

import java.io.Serializable;

public class Font implements Serializable {

    private int size;
    private String name;
    private int style;

    public Font(){
    }

    public java.awt.Font toFont(){
       return new java.awt.Font(name, style, size);
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setSize(int size){
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
    public void setStyle(int style){
        this.style = style;
    }
    public int getStyle(){
        return this.style;
    }

}
