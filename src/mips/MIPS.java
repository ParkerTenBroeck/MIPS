/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import GUI.Main_GUI;
import java.awt.Color;

/**
 *
 * @author parke
 */
public class MIPS {
    
    static final String[] INSTRUCTIONS = new String[]{"add","addu","addi","addiu","and","andi","div","divu","mult","multu","nor","or","ori","sll","sllv","sra","srav","srl","srlv","sub","subu","xor","xori","lhi","llo","slt","sltu","slti","sltiu","beq","bgtz","ble","bne","j","jal","jalr","jr","lb","lbu","lh","lhu","lw","sb","sh","sw","mfhi","mflo","mthi","mtlo","trap"};

    public static void main(String[] args) {
        Log.initLogger();
        ResourceHandler.extractResources();
        Main_GUI gui = new Main_GUI();
        
        for(int i = 0; i < 15; i ++){
            //Log.logCustomMessage("Bruh", true,true,true,new Color(Color.HSBtoRGB((float) (i / 15.0), 1, 1)),"");
        }
    }

    

}
