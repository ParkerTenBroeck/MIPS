/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import org.parker.mips.assembler.data.MemoryLable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author parke
 */
public class ComboBoxSearchable {

    private ArrayList<String> items = new ArrayList();
    private JComboBox comboBox;

    public ComboBoxSearchable(JComboBox comboBox, ArrayList<MemoryLable> newItems) {

        items = new ArrayList();

        for (MemoryLable ml:newItems) {
            items.add(ml.name);
        }

        comboBox.setEditable(true);
        Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
        this.items = items;
        this.comboBox = comboBox;

        if (this.items != null) {
            this.comboBox.setModel(new DefaultComboBoxModel(this.items.toArray()));
        } else {
            this.comboBox.setModel(new DefaultComboBoxModel(new String[]{"Loading..."}));
        }

        ((JTextField) comboBox.getEditor().getEditorComponent()).setText(null);
        initEventListener(comboBox);
    }

    public ComboBoxSearchable(JComboBox comboBox) {
        comboBox.setEditable(true);
        this.comboBox = comboBox;
        ((JTextField) comboBox.getEditor().getEditorComponent()).setText(null);
        initEventListener(comboBox);
    }

    private void initEventListener(JComboBox comboBox) {
        comboBox.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ComboBoxKeyEvent(evt);
            }
        });
        comboBox.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                ComboBoxFocusGained(evt);
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                ComboBoxFocusLost(evt);
            }
        });
    }

    public void setList(ArrayList<String> items) {
        this.items = items;
        if (this.items != null) {
            this.comboBox.setModel(new DefaultComboBoxModel(this.items.toArray()));
            if (items.size() > 0) {
                ((JTextField) comboBox.getEditor().getEditorComponent()).setText(null);
            }
        } else {
            this.comboBox.setModel(new DefaultComboBoxModel(new String[]{"Loading..."}));
        }
    }

    public String getEnteredText() {
        return ((JTextField) comboBox.getEditor().getEditorComponent()).getText();
    }

    public void ComboBoxFocusGained(java.awt.event.FocusEvent evt) {

        try {
            comboBox.setModel(filterListByString(items, ""));
            comboBox.setSelectedIndex(0);
            ((JTextField) comboBox.getEditor().getEditorComponent()).setText(null);
            comboBox.showPopup();
        } catch (Exception e) {
            ((JTextField) comboBox.getEditor().getEditorComponent()).setText("Error No Items");
        }

    }

    public void ComboBoxFocusLost(java.awt.event.FocusEvent evt) {

//        if (comboBox.getItemAt(0) != null) {
//            String text = ((JTextField) evt.getSource()).getText();
//            comboBox.setModel(filterListByString(items, text));
//            ((JTextField) comboBox.getEditor().getEditorComponent())
//                    .setText((String) comboBox.getItemAt(0));
//        } else {
//            comboBox.setModel(filterListByString(items, ""));
//            ((JTextField) comboBox.getEditor().getEditorComponent())
//                    .setText(null);
//        }
    }

    public void ComboBoxKeyEvent(java.awt.event.KeyEvent evt) {
        final int key = evt.getKeyCode();
        if (key == 10) {
            try {
                String text = ((JTextField) evt.getSource()).getText();
                comboBox.setModel(filterListByString(items, text));
                comboBox.setSelectedIndex(0);
                ((JTextField) comboBox.getEditor().getEditorComponent())
                        .setText((String) comboBox.getSelectedItem());
            } catch (Exception e) {
                comboBox.setSelectedIndex(-1);
                ((JTextField) comboBox.getEditor().getEditorComponent())
                        .setText(null);
            }
        } else if (key != 37 && key != 38 && key != 39 && key != 40) {
            String text = ((JTextField) evt.getSource()).getText();
            comboBox.setModel(filterListByString(items, text));
            try {
                comboBox.setSelectedIndex(0);
            } catch (Exception e) {
            }
            ((JTextField) comboBox.getEditor().getEditorComponent()).setText(text);
            comboBox.showPopup();
        }
    }

    public void setEnteredText(String text) {
        ((JTextField) comboBox.getEditor().getEditorComponent()).setText(text);
    }

    public static DefaultComboBoxModel filterListByString(ArrayList<String> items, String searchQuarry) {
        ArrayList<String> sortedList = new ArrayList();
        for (String item : items) {
            if (item.startsWith(searchQuarry)) {
                sortedList.add(item);
            }
        }
        return new DefaultComboBoxModel(sortedList.toArray());
    }

}
