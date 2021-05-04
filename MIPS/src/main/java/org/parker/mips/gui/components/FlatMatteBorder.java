package org.parker.mips.gui.components;


import javax.swing.*;
import javax.swing.border.MatteBorder;

public class FlatMatteBorder extends MatteBorder {


    public FlatMatteBorder(int up, int left, int down, int right) {
        super(up, left, down, right, UIManager.getColor("Component.borderColor"));
    }
}
