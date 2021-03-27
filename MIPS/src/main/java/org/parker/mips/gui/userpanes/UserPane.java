package org.parker.mips.gui.userpanes;

import javafx.scene.layout.BorderPane;

import javax.swing.*;

public abstract class UserPane extends javax.swing.JPanel {

    private JLabel title = new JLabel();

    public UserPane(){

    }

    public abstract boolean close();

    public void updateValues(){};

    protected final void setTitle(String title){
        this.title.setText(title);
    }
    public final String getTitle(){
        return this.title.getText();
    }
    public final JLabel getTitleLabel(){
        return this.title;
    }

}
