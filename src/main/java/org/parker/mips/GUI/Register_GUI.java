/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI;

import org.parker.mips.GUI.lookandfeel.RoundedBorder;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import org.parker.mips.GUI.ThemedJFrameComponents.ThemableComponent;
import org.parker.mips.GUI.ThemedJFrameComponents.ThemeHandler;
import org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton;
import org.parker.mips.Processor.Registers;

/**
 *
 * @author parke
 */
public class Register_GUI extends javax.swing.JPanel implements ThemableComponent {

    RoundedBorder roundedBorder = new RoundedBorder(00, 15);

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME:
                this.pc1.setFont((Font) pce.getNewValue());
                this.pc.setFont((Font) pce.getNewValue());
                this.lowHigh.setFont((Font) pce.getNewValue());
                this.registers.setFont((Font) pce.getNewValue());
                break;
            case ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME:
                this.pc1.setBackground((Color) pce.getNewValue());
                this.pc.setBackground((Color) pce.getNewValue());
                this.lowHigh.setBackground((Color) pce.getNewValue());
                this.registers.setBackground((Color) pce.getNewValue());
                break;
            case ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME:
                roundedBorder.setColor((Color) pce.getNewValue());
                this.pc1.setGridColor((Color) pce.getNewValue());
                this.pc.setGridColor((Color) pce.getNewValue());
                this.lowHigh.setGridColor((Color) pce.getNewValue());
                this.registers.setGridColor((Color) pce.getNewValue());
                break;
            case ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME:
                this.pc1.setForeground((Color) pce.getNewValue());
                this.pc.setForeground((Color) pce.getNewValue());
                this.lowHigh.setForeground((Color) pce.getNewValue());
                this.registers.setForeground((Color) pce.getNewValue());
        }
    }

    /**
     * Creates new form Registers
     */
    public Register_GUI() {
        initComponents();

        //this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME));
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME, this);

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME, this);

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME, this);

        this.setBorder(roundedBorder);

        Font textFont = (Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME);

        this.pc1.setFont(textFont);
        this.pc.setFont(textFont);
        this.lowHigh.setFont(textFont);
        this.registers.setFont(textFont);

        Color backgroundColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME);

        roundedBorder.setColor(backgroundColor);
        this.pc1.setGridColor(backgroundColor);
        this.pc.setGridColor(backgroundColor);
        this.lowHigh.setGridColor(backgroundColor);
        this.registers.setGridColor(backgroundColor);

        Color textColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME);

        this.pc1.setForeground(textColor);
        this.pc.setForeground(textColor);
        this.lowHigh.setForeground(textColor);
        this.registers.setForeground(textColor);

        Color forgroundColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME);

        this.pc1.setBackground(forgroundColor);
        this.pc.setBackground(forgroundColor);
        this.lowHigh.setBackground(forgroundColor);
        this.registers.setBackground(forgroundColor);

        configTable(this.pc);
        configTable(this.pc1);
        configTable(this.lowHigh);
        configTable(this.registers);

        //this.pc.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        //this.pc1.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        //this.lowHigh.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        //this.registers.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        //Register_GUI.pc.setT
    }

    private static void configTable(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(90);
        colModel.getColumn(1).setPreferredWidth(250);
        colModel.getColumn(2).setPreferredWidth(80);
        colModel.getColumn(3).setPreferredWidth(90);
    }

    public static synchronized void updateVals() {
        for (int i = 1; i <= 31; i++) {
            setRegisterRow(registers, i - 1, Registers.getRegister(i));
        }

        setRegisterRow(pc, 0, Registers.getPc());

        setRegisterRow(lowHigh, 0, Registers.getLow());
        setRegisterRow(lowHigh, 1, Registers.getHigh());
        lowHigh.setValueAt(((long) Registers.getHigh() << 32) | (long) Registers.getLow(),
                2, 3);
    }

    private static void setRegisterRow(JTable table, int row, int val) {
        String bin = String.format("%32s", Integer.toBinaryString(val)).replaceAll(" ", "0");
        String hex = String.format("%8s", Integer.toHexString(val)).replaceAll(" ", "0");

        table.setValueAt(bin, row, 1);
        table.setValueAt(hex, row, 2);
        table.setValueAt(val, row, 3);
    }

    public static String intToHexString(int val) {
        return String.format("%8s", Integer.toHexString(val)).replaceAll(" ", "0");
    }

    public static String intToBinString(int val) {
        return String.format("%32s", Integer.toBinaryString(val)).replaceAll(" ", "0");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pc1 = new javax.swing.JTable();
        pc = new javax.swing.JTable();
        lowHigh = new javax.swing.JTable();
        registers = new javax.swing.JTable();

        setBackground(new java.awt.Color(102, 102, 102));
        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setOpaque(false);

        pc1.setBackground(new java.awt.Color(102, 102, 102));
        pc1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        pc1.setForeground(new java.awt.Color(204, 204, 204));
        pc1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Name", "Bin", "Hex", "Dec"}
            },
            new String [] {
                "Register", "Bin", "Hex", "Dec"
            }
        ));
        pc1.setEnabled(false);
        pc1.setOpaque(false);

        pc.setBackground(new java.awt.Color(102, 102, 102));
        pc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        pc.setForeground(new java.awt.Color(204, 204, 204));
        pc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"PC", null, null, null}
            },
            new String [] {
                "Register", "Bin", "Hex", "Dec"
            }
        ));
        pc.setEnabled(false);
        pc.setGridColor(new java.awt.Color(51, 51, 51));
        pc.setOpaque(false);

        lowHigh.setBackground(new java.awt.Color(102, 102, 102));
        lowHigh.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lowHigh.setForeground(new java.awt.Color(204, 204, 204));
        lowHigh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Low", null, null, null},
                {"High", null, null, null},
                {"Combined", null, null, null}
            },
            new String [] {
                "Register", "Bin", "Hex", "Dec"
            }
        ));
        lowHigh.setEnabled(false);
        lowHigh.setGridColor(new java.awt.Color(204, 204, 204));
        lowHigh.setOpaque(false);

        registers.setBackground(new java.awt.Color(102, 102, 102));
        registers.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        registers.setForeground(new java.awt.Color(204, 204, 204));
        registers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", null, null, null},
                {"2", null, null, null},
                {"3", null, null, null},
                {"4", null, null, null},
                {"5", null, null, null},
                {"6", null, null, null},
                {"7", null, null, null},
                {"8", null, null, null},
                {"9", null, null, null},
                {"10", null, null, null},
                {"11", null, null, null},
                {"12", null, null, null},
                {"13", null, null, null},
                {"14", null, null, null},
                {"15", null, null, null},
                {"16", null, null, null},
                {"17", null, null, null},
                {"18", null, null, null},
                {"19", null, null, null},
                {"20", null, null, null},
                {"21", null, null, null},
                {"22", null, null, null},
                {"23", null, null, null},
                {"24", null, null, null},
                {"25", null, null, null},
                {"26", null, null, null},
                {"27", null, null, null},
                {"28", null, null, null},
                {"29", null, null, null},
                {"30", null, null, null},
                {"31", null, null, null}
            },
            new String [] {
                "Register", "Bin", "Hex", "Dec"
            }
        ));
        registers.setEnabled(false);
        registers.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
            .addComponent(lowHigh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pc1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pc1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(pc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lowHigh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JTable lowHigh;
    private static javax.swing.JTable pc;
    private javax.swing.JTable pc1;
    private static javax.swing.JTable registers;
    // End of variables declaration//GEN-END:variables

}
