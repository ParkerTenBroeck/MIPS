/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import org.parker.mips.gui.lookandfeel.RoundedBorder;
import static org.parker.mips.gui.InstructionToString.instructionToString;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import javax.swing.DefaultListModel;
import org.parker.mips.gui.ThemedJFrameComponents.ThemableComponent;
import org.parker.mips.gui.ThemedJFrameComponents.ThemeHandler;
import org.parker.mips.processor.Memory;
import static org.parker.mips.processor.Memory.superGetWord;
import static org.parker.mips.processor.Registers.getPc;

/**
 *
 * @author parke
 */
public class InstructionMemoryGUI extends javax.swing.JPanel implements ThemableComponent {

    private static int toMiddle = 0;

    private static RoundedBorder roundedBorder = new RoundedBorder(new Color(0, 0, 51), 0, 15);

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME:
                this.instructionList.setFont((Font) pce.getNewValue());
                break;
            case ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME:
                this.instructionList.setForeground((Color) pce.getNewValue());
                break;
            case ThemeHandler.TEXT_AREA_BACKGROUND_1_PROPERTY_NAME:
                this.roundedBorder.setColor((Color) pce.getNewValue());
                this.instructionList.setBackground((Color) pce.getNewValue());
                break;
        }
    }

    public InstructionMemoryGUI() {
        initComponents();

        //this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME));
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME, this);

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME, this);

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_AREA_BACKGROUND_1_PROPERTY_NAME, this);

        this.setBorder(roundedBorder);

        Color backgroundColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_AREA_BACKGROUND_1_PROPERTY_NAME);

        this.roundedBorder.setColor(backgroundColor);
        this.instructionList.setBackground(backgroundColor);

        Color textColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME);

        this.instructionList.setForeground(textColor);

        Font generalFont = (Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME);

        this.instructionList.setFont(generalFont);

    }

    public static void refresh() {
        instructionList.setSelectedIndex(getPc() / 4);
        instructionList.ensureIndexIsVisible((getPc() / 4) - toMiddle);
        instructionList.ensureIndexIsVisible((getPc() / 4) + toMiddle); //sets the  
    }

    public static void refreshValues() {

        DefaultListModel listModel = new DefaultListModel();

        for (int i = 0; i < Memory.getSize(); i += 4) {
            listModel.addElement(instructionToString(superGetWord(i)));
        }
        instructionList.setModel(listModel);

        if (instructionList.getModel().getSize() > 0) {
            toMiddle = (instructionList.getVisibleRect().height / instructionList.getCellBounds(0, 0).height) / 2;
        }

        refresh();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modernScrollPane2 = new org.parker.mips.gui.lookandfeel.ModernScrollPane();
        instructionList = new javax.swing.JList<>();

        setOpaque(false);

        instructionList.setBackground(new java.awt.Color(0, 0, 51));
        instructionList.setForeground(new java.awt.Color(204, 204, 204));
        instructionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        instructionList.setFocusable(false);
        instructionList.setRequestFocusEnabled(false);
        instructionList.setSelectionBackground(new java.awt.Color(0, 204, 153));
        modernScrollPane2.setViewportView(instructionList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modernScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modernScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JList<String> instructionList;
    private static org.parker.mips.gui.lookandfeel.ModernScrollPane modernScrollPane2;
    // End of variables declaration//GEN-END:variables
}