/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import org.parker.mips.gui.editor.Editor;
import org.parker.mips.gui.editor.EditorHandler;
import org.parker.mips.gui.editor.rsyntax.FormattedTextEditor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

import static com.formdev.flatlaf.FlatClientProperties.TABBED_PANE_TAB_CLOSABLE;
import static com.formdev.flatlaf.FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK;

/**
 *
 * @author parke
 */
public class EditorTabbedPane extends javax.swing.JPanel {
    
    public static void addEditor(Editor editor) {
        jTabbedPane1.add(editor, editor.getDisplayName());
        jTabbedPane1.setSelectedComponent(editor);
        JLabel label = new JLabel();
        jTabbedPane1.setTabComponentAt(jTabbedPane1.getSelectedIndex(), label);
        editor.setTitleLable(label);
    }
    
    public static void removeEditor(Editor editor) {
        jTabbedPane1.remove(editor);
    }
    
    public static void setSelectedTab(Editor editor) {
        jTabbedPane1.setSelectedComponent(editor);
    }
    
    public EditorTabbedPane() {
        initComponents();
        
        jTabbedPane1.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
        jTabbedPane1.putClientProperty(TABBED_PANE_TAB_CLOSABLE, true);
        //jTabbedPane1.putClientProperty(TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, "Close"); //USING THIS WILL CAUSE CRASHES
        jTabbedPane1.putClientProperty(TABBED_PANE_TAB_CLOSE_CALLBACK,
                (BiConsumer<JTabbedPane, Integer>) (tabPane, tabIndex) -> {
                    AWTEvent e = EventQueue.getCurrentEvent();
                    MouseEvent me = (e instanceof MouseEvent) ? ((MouseEvent) e) : null;
                    if (me == null) {
                        return;
                    }
                    if (me.getButton() == MouseEvent.BUTTON1) {
                        Editor editor = (Editor) tabPane.getComponentAt(tabIndex);
                        editor.close();
                    }
                });
        
        new FormattedTextEditor();
        
        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                EditorHandler.setLastFocused((Editor) jTabbedPane1.getSelectedComponent());
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setBackground(new java.awt.Color(102, 102, 0));
        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void aSMFormattedTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_aSMFormattedTextArea1KeyTyped

    }//GEN-LAST:event_aSMFormattedTextArea1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
