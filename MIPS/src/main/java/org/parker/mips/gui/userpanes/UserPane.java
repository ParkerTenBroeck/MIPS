package org.parker.mips.gui.userpanes;

import javafx.scene.layout.BorderPane;

import javax.swing.*;

public abstract class UserPane extends javax.swing.JPanel {

    private JLabel title;

    public abstract String getDisplayName();

    public abstract boolean close();

    public void updateValues(){};

    protected void updateDisplayTitle() {
        title.setText(getDisplayName());
    }

    public void setTitleLable(JLabel lable) {
        this.title = lable;
        updateDisplayTitle();
    }

}
