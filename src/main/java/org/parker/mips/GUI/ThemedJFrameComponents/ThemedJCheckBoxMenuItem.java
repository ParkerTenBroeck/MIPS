/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.ThemedJFrameComponents;

import javax.swing.JCheckBoxMenuItem;
import org.parker.mips.Holder;

/**
 *
 * @author parke
 */
public class ThemedJCheckBoxMenuItem extends JCheckBoxMenuItem {

    private Holder<Boolean> isSelected;
    
    public ThemedJCheckBoxMenuItem(){
        this.addActionListener((ae) -> {
            setSelected(super.isSelected());
        });
    }
        

    @Override
    public boolean isSelected() {
        if (isSelected != null) {
            return isSelected.value;
        } else {
            return super.isSelected();
        }
    }

    public void setSelected(Holder<Boolean> value) {
        this.isSelected = value;
    }

    @Override
    public void setSelected(boolean value) {
        if (this.isSelected != null) {
            this.isSelected.value = value;
        } else {
            super.setSelected(value);
        }
    }

}
