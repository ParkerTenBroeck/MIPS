/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.theme.components;


import javax.swing.JCheckBoxMenuItem;
import org.parker.mips.Holder;
/**
 *
 * @author parke
 */
public class ThemedJCheckBoxMenuItem extends JCheckBoxMenuItem {
    
    private Holder<Boolean> isSelected;
    
    public void setSelected(Holder<Boolean> value) {
        this.isSelected = value;
    }
    

}
