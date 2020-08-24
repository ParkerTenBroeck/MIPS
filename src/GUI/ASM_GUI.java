/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.lookandfeel.ModernScrollBarUI;
import GUI.lookandfeel.RoundedBorder;
import java.awt.Color;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultStyledDocument;
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
        this.setBorder(new RoundedBorder(new Color(0, 0, 51), 0, 15));

        //asmScrollArea.getVerticalScrollBar().setUI(new ModernScrollBarUI(asmScrollArea));

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

        modernScrollPane1 = new GUI.lookandfeel.ModernScrollPane();
        asmTextPane = new javax.swing.JTextPane();

        setBackground(new java.awt.Color(102, 102, 0));
        setOpaque(false);

        asmTextPane.setEditable(false);
        asmTextPane.setBackground(new java.awt.Color(0, 0, 51));
        asmTextPane.setBorder(null);
        asmTextPane.setForeground(new java.awt.Color(204, 204, 204));
        asmTextPane.setCaretColor(new java.awt.Color(255, 255, 255));
        asmTextPane.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        asmTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                asmTextPaneKeyTyped(evt);
            }
        });
        modernScrollPane1.setViewportView(asmTextPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modernScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modernScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
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
    private static GUI.lookandfeel.ModernScrollPane modernScrollPane1;
    // End of variables declaration//GEN-END:variables
}
