package org.parker.mips.gui.userpanes;

import javax.swing.*;

public abstract class UserPane extends javax.swing.JPanel {

    private JLabel title = new JLabel();

    public UserPane(){
    }

    public abstract boolean close();

    /**
     * this is called whenever the UI requests to update the contents of the displayed panel
     * this method is only called when the component is visible
     * example of its use is updating the register values on the emulator state tab
     */
    public void update(){}

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
