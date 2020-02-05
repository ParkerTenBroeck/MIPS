/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.util.List;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import mips.FileWriteReader;

/**
 *
 * @author parke
 */
public class ASM_GUI extends javax.swing.JPanel {

    public static void clearText() {
        asmTextPane.setText("");
    }

    public static char[] getAllText() {
        return asmTextPane.getText().toCharArray();
    }

    static void setTextAreaFromASMFile() {
        setTextAreaFromList(FileWriteReader.getASMList());
    }

    static void setEnable(boolean enabled) {
        asmTextPane.setEditable(enabled);
    }

    /**
     * Creates new form AMS_GUI
     */
    public ASM_GUI() {
        initComponents();
        asmTextPane.setEditable(false);
        asmTextPane.setStyledDocument(new DefaultStyledDocument());
    }

    public static void setTextAreaFromList(List<String> list) {
        clearText();

        if (list == null) {
            return;
        }

        asmTextPane.setText(String.join("\n", list));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        asmScrollArea = new javax.swing.JScrollPane();
        asmTextPane = new javax.swing.JTextPane();

        asmTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                asmTextPaneKeyTyped(evt);
            }
        });
        asmScrollArea.setViewportView(asmTextPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(asmScrollArea, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(asmScrollArea, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void asmTextPaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_asmTextPaneKeyTyped
        FileWriteReader.fileChange();
        colorText();
    }//GEN-LAST:event_asmTextPaneKeyTyped

    public static void colorText() {
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JScrollPane asmScrollArea;
    private static javax.swing.JTextPane asmTextPane;
    // End of variables declaration//GEN-END:variables
}
