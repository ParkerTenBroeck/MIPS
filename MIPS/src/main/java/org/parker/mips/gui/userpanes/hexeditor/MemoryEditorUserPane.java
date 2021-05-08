/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.mips.gui.userpanes.hexeditor;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.parker.mips.assembler.base.preprocessor.BaseExpressionCompiler;
import org.parker.mips.gui.components.FlatIconButton;
import org.parker.mips.gui.icons.FlatMinusIcon;
import org.parker.mips.gui.icons.FlatPlusIcon;
import org.parker.mips.gui.userpanes.UserPane;
import org.parker.mips.util.Memory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MemoryEditorUserPane extends UserPane {
    private JPanel panel1;
    private JTextField textField;
    private JPanel bfg;
    private JLabel lableTextField;
    private JComboBox groupSizeComboBox;
    private JComboBox columnsComboBox;
    private JRadioButton smallEndianRadioButton;
    private JRadioButton bigEndianRadioButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JLabel rowCountLabel;
    private Memory mem;

    public MemoryEditorUserPane(Memory processorMemory) {

        this.setTitle("Memory");
        this.mem = processorMemory;

        $$$setupUI$$$();

        textField.setToolTipText("Address can be typed as integer (123) " +
                "hex string (0xFA1) binary String (0b101) " +
                "or a base of your choosing ex base 6 (6x234) \n" +
                "Memory Labels defined in the program can also be used" +
                " although must be loaded at time of use");

        textField.addMouseListener(new MouseAdapter() {
            boolean inside = false;

            @Override
            public void mouseEntered(MouseEvent e) {
                inside = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                inside = false;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (textField.getText().equals("Label/Address")) {
                    textField.setText("");
                }
                if (!inside) {
                    textField.setFocusable(false);
                    textField.setFocusable(true);
                    //textField.
                }
            }
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    try {
                        String text = textField.getText();
                        int value;

                        value = BaseExpressionCompiler.parseInt(text);

                        ((EditableMemoryHexGrid) bfg).scrollToAddress(value);
                    } catch (Exception ex) {

                    }

                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    textField.setText("Label/Address");
                }
            }
        });

        bigEndianRadioButton.addChangeListener(e -> {
            ((EditableMemoryHexGrid) bfg).setBigEndian(bigEndianRadioButton.isSelected());
        });

        //+
        //checkBox2.setIcon(UIManager.getIcon("CheckBox.icon"));
        //checkBox2.setIcon(new Why());
        //UIManager.put("CheckBox.icon",  UIManager.getIcon("FileView.directoryIcon"));
        //System.out.println(UIManager.get("CheckBox.icon").getClass().toString());
        //checkBox2.putClientProperty("CheckBox.icon", new FlatPlusIcon());
        //checkBox1.putClientProperty("CheckBox.icon", "org.parker.mips.gui.icons.FlatPlusIcon");

        //UIManager.put("RadioButton.icon", (UIDefaults.ActiveValue) t -> new FlatPlusIcon());
        //UIManager.put("CheckBox.icon", (UIDefaults.ActiveValue) t -> new FlatPlusIcon());
        //bigEndianRadioButton.putClientProperty("RadioButton.icon", (UIDefaults.ActiveValue) t -> new FlatPlusIcon());
        //UIManager.put("CheckBox.arc", 999);
        //checkBox2.putClientProperty("CheckBoxUI", new yes());
        checkBox2.addActionListener(e -> {
            if (!checkBox2.isSelected()) {
                checkBox2.setSelected(true);
                changeRows(getRows() + 1);
            }
        });
        //-
        //checkBox1.setIcon(new FlatMinusIcon());
        checkBox1.addActionListener(e -> {
            if (!checkBox1.isSelected()) {
                checkBox1.setSelected(true);
                changeRows(getRows() - 1);
            }
        });
    }


    protected final void changeRows(int rows) {
        if (rows < 2)
            return;
        ((EditableMemoryHexGrid) bfg).updateThings(rows, ((EditableMemoryHexGrid) bfg).getColumns(), ((EditableMemoryHexGrid) bfg).getGroupSize());
        rowCountLabel.setText((rows - 1) + "");
    }

    public final int getRows() {
        return ((EditableMemoryHexGrid) bfg).getRows();
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public void update() {
        if (bfg == null) {
            return;
        }
        if (!(bfg instanceof EditableMemoryHexGrid)) {
            return;
        }
        ((EditableMemoryHexGrid) bfg).updateValues();
    }

    private void createUIComponents() {

        panel1 = this;
        bfg = new EditableMemoryHexGrid(21, 17, EditableHexGrid.GroupSize.Byte, mem);


        EditableMemoryHexGrid asd = ((EditableMemoryHexGrid) bfg);

        groupSizeComboBox = new JComboBox(EditableHexGrid.GroupSize.values());
        groupSizeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                asd.updateThings(asd.getRows(), asd.getColumns(), (EditableHexGrid.GroupSize) e.getItem());
            }
        });

        columnsComboBox = new JComboBox();
        columnsComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                asd.updateThings(asd.getRows(), Integer.parseInt((String) e.getItem()) + 1, asd.getGroupSize());
            }
        });

        checkBox1 = new FlatIconButton(FlatMinusIcon.class);
        checkBox2 = new FlatIconButton(FlatPlusIcon.class);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setViewportView(bfg);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textField = new JTextField();
        textField.setText("Lable/Address");
        panel2.add(textField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lableTextField = new JLabel();
        lableTextField.setText("Memory Lable/Hex Address");
        panel2.add(lableTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Endianness");
        panel2.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Columns");
        panel2.add(label2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Group Size");
        panel2.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(groupSizeComboBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("8");
        defaultComboBoxModel1.addElement("16");
        defaultComboBoxModel1.addElement("24");
        defaultComboBoxModel1.addElement("32");
        columnsComboBox.setModel(defaultComboBoxModel1);
        columnsComboBox.setSelectedIndex(1);
        panel2.add(columnsComboBox, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        smallEndianRadioButton = new JRadioButton();
        smallEndianRadioButton.setFocusable(false);
        smallEndianRadioButton.setText("Small-Endian");
        panel3.add(smallEndianRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bigEndianRadioButton = new JRadioButton();
        bigEndianRadioButton.setFocusable(false);
        bigEndianRadioButton.setSelected(true);
        bigEndianRadioButton.setText("Big-Endian");
        panel3.add(bigEndianRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checkBox2.setFocusable(false);
        checkBox2.setSelected(true);
        checkBox2.setText("");
        panel4.add(checkBox2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox1.setFocusable(false);
        checkBox1.setSelected(true);
        checkBox1.setText("");
        panel4.add(checkBox1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rowCountLabel = new JLabel();
        rowCountLabel.setText("20");
        panel4.add(rowCountLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Rows");
        panel2.add(label4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(bigEndianRadioButton);
        buttonGroup.add(smallEndianRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}