/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.lookandfeel.RoundedBorder;
import java.awt.Color;
import java.util.List;
import mips.FileHandler;

/**
 *
 * @author parke
 */
public class ASM_GUI extends javax.swing.JPanel {

    public static void clearText() {
        //asmTextPane.setText("");
        aSMFormattedTextArea1.textArea.setText("");
    }

    public static char[] getAllText() {
        return aSMFormattedTextArea1.textArea.getText().toCharArray();
        //return asmTextPane.getText().toCharArray();
    }

    static void setTextAreaFromASMFile() {
        setTextAreaFromList(FileHandler.getASMList());
    }

    static void setEnable(boolean enabled) {
        aSMFormattedTextArea1.textArea.setEditable(enabled);
    }

    /**
     * Creates new form AMS_GUI
     */
    public ASM_GUI() {
        initComponents();
        this.setBorder(new RoundedBorder(new Color(0, 0, 51), 0, 15));

        //asmScrollArea.getVerticalScrollBar().setUI(new ModernScrollBarUI(asmScrollArea));
        //aSMFormattedTextArea1.textArea.setEditable(false);
        //aSMFormattedTextArea1.textArea.setStyledDocument(new DefaultStyledDocument());
        aSMFormattedTextArea1.setAllFontSize(15);

        aSMFormattedTextArea1.textArea.addMouseWheelListener(mouseWheelEvent
                -> {
            if (mouseWheelEvent.isControlDown()) {
                int scrolled = mouseWheelEvent.getUnitsToScroll();
                int newFontSize = aSMFormattedTextArea1.getAllFontSize() + scrolled;
                if (newFontSize <= 0) {
                    newFontSize = 1;
                }
                aSMFormattedTextArea1.setAllFontSize(newFontSize);
            } else {
                int unit = aSMFormattedTextArea1.getVerticalScrollBar().getUnitIncrement();
                int scrolled = mouseWheelEvent.getUnitsToScroll();
                int currentPos = aSMFormattedTextArea1.getVerticalScrollBar().getValue();
                
                aSMFormattedTextArea1.getVerticalScrollBar().setValue(unit*scrolled + currentPos);
            }
        });

    }

    public static void setTextAreaFromList(List<String> list) {
        clearText();

        if (list == null) {
            return;
        }

        //asmTextPane.setText(String.join("\n", list));
        aSMFormattedTextArea1.textArea.setText(String.join("\n", list));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aSMFormattedTextArea1 = new GUI.RSyntax.ASMFormattedTextArea();

        setBackground(new java.awt.Color(102, 102, 0));
        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aSMFormattedTextArea1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aSMFormattedTextArea1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public static void colorText() {
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static GUI.RSyntax.ASMFormattedTextArea aSMFormattedTextArea1;
    // End of variables declaration//GEN-END:variables
}
