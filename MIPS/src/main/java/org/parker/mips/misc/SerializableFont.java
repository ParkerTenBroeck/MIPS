package org.parker.mips.misc;

import java.io.Serializable;

public class SerializableFont implements Serializable {


    private String name;
    private int style;
    private int size;

    public SerializableFont(){
    }

    public SerializableFont(String name, int style, int size){
        this.name = name;
        this.style = style;
        this.size = size;
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
