/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import org.parker.mips.gui.theme.lookandfeel.RoundedBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import org.parker.mips.FileHandler;
import org.parker.mips.gui.theme.ThemeHandler;
import org.parker.mips.OptionsHandler;

/**
 *
 * @author parke
 */
public class ASM_GUI extends javax.swing.JPanel {

    private static RoundedBorder border = new RoundedBorder(new Color(0, 0, 51), 0, 15);
    private final ASM_GUI instance;

    public static void clearText() {
        //asmTextPane.setText("");
        aSMFormattedTextArea1.textArea.setText("");
    }

    public static char[] getAllText() {
        return aSMFormattedTextArea1.textArea.getText().toCharArray();
        //return asmTextPane.getText().toCharArray();
    }

    public static void setTextAreaFromASMFile() {
        //aSMFormattedTextArea1.textArea.setText(new String(FileHandler.getLoadedASMFile()));
        setTextAreaFromList(FileHandler.getLoadedASMFile());
    }

    static void setEnable(boolean enabled) {
        aSMFormattedTextArea1.textArea.setEditable(enabled);
    }

    private static void setTextAreaFromList(ArrayList<String> list) {
        aSMFormattedTextArea1.textArea.setText(String.join("\n", list));
    }

    public static void loadCurrentTheme() {
        setTextAreaThemeFromName(OptionsHandler.currentSyntaxTheme.value);
    }

    public static void setTextAreaThemeFromName(String name) {
        aSMFormattedTextArea1.setTheme(name);
    }

    /**
     * Creates new form AMS_GUI
     */
    public ASM_GUI() {
        instance = this;
        initComponents();
        border.setColor(aSMFormattedTextArea1.getBackground());
        this.setBorder(border);

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
                Font temp = new Font(((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME)).getName(), Font.PLAIN, newFontSize);
                ThemeHandler.setThemeFromName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME, temp, true, true);

                ThemeHandler.setThemeFromName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME, temp, true, true);

                ThemeHandler.setThemeFromName(ThemeHandler.LABLE_TEXT_FONT_PROPERTY_NAME, temp, true, true);
            } else {
                int unit = aSMFormattedTextArea1.getVerticalScrollBar().getUnitIncrement();
                int scrolled = mouseWheelEvent.getUnitsToScroll() * 5;
                //System.out.println(scrolled);
                int currentPos = aSMFormattedTextArea1.getVerticalScrollBar().getValue();
                aSMFormattedTextArea1.getVerticalScrollBar().setValue(unit * scrolled + currentPos);
            }
        });

        aSMFormattedTextArea1.textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                FileHandler.asmTextAreaChange();
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aSMFormattedTextArea1 =         new org.parker.mips.gui.rsyntax.ASMFormattedTextArea(){
            @Override
            public void setBackground(Color color){
                super.setBackground(color);
                border.setColor(color);
                instance.setBackground(color);
                instance.repaint();
            }
        };

        setBackground(new java.awt.Color(102, 102, 0));
        setOpaque(false);

        aSMFormattedTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                aSMFormattedTextArea1KeyTyped(evt);
            }
        });

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

    private void aSMFormattedTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_aSMFormattedTextArea1KeyTyped

    }//GEN-LAST:event_aSMFormattedTextArea1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static org.parker.mips.gui.rsyntax.ASMFormattedTextArea aSMFormattedTextArea1;
    // End of variables declaration//GEN-END:variables
}
