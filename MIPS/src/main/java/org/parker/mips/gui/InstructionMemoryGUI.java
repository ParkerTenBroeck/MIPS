/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import static org.parker.mips.compiler.InstructionToString.instructionToString;
import javax.swing.DefaultListModel;
import org.parker.mips.processor.Memory;
import static org.parker.mips.processor.Memory.superGetWord;
import static org.parker.mips.processor.Registers.getPc;

/**
 *
 * @author parke
 */
public class InstructionMemoryGUI extends javax.swing.JPanel{

    private static int toMiddle = 0;
 
    public InstructionMemoryGUI() {
        initComponents();
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

        modernScrollPane2 = new javax.swing.JScrollPane();
        instructionList = new javax.swing.JList<>();

        setOpaque(false);

        instructionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        instructionList.setFocusable(false);
        instructionList.setRequestFocusEnabled(false);
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
    private static javax.swing.JScrollPane modernScrollPane2;
    // End of variables declaration//GEN-END:variables
}
