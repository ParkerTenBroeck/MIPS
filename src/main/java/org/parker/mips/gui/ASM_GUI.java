/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import org.parker.mips.FileHandler;
import org.parker.mips.OptionsHandler;

/**
 *
 * @author parke
 */
public class ASM_GUI extends javax.swing.JPanel {

//    public static void clearText() {
//        //asmTextPane.setText("");
//        aSMFormattedTextArea1.textArea.setText("");
//    }
//
//    public static char[] getAllText() {
//        return aSMFormattedTextArea1.textArea.getText().toCharArray();
//        //return asmTextPane.getText().toCharArray();
//    }
//
//    public static void setTextAreaFromASMFile() {
//        //aSMFormattedTextArea1.textArea.setText(new String(FileHandler.getLoadedASMFile()));
//        setTextAreaFromList(FileHandler.getLoadedASMFile());
//    }
//
//
//    private static void setTextAreaFromList(ArrayList<String> list) {
//        aSMFormattedTextArea1.textArea.setText(String.join("\n", list));
//    }

//    public static void loadCurrentTheme() {
//        setTextAreaThemeFromName(OptionsHandler.currentEditorTheme.val());
//    }

//    public static void setTextAreaThemeFromName(String name) {
//        aSMFormattedTextArea1.setTheme(name);
//    }

    /**
     * Creates new form AMS_GUI
     */
    public ASM_GUI() {
        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(102, 102, 0));
        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 559, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 364, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void aSMFormattedTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_aSMFormattedTextArea1KeyTyped

    }//GEN-LAST:event_aSMFormattedTextArea1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
