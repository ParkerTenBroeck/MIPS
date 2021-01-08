/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.theme.components;

import org.parker.mips.gui.theme.components.ThemableComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JCheckBox;
import org.parker.mips.Holder;
import org.parker.mips.gui.theme.ThemeHandler;

/**
 *
 * @author parke
 */
public class ThemedJCheckBox extends JCheckBox {

    private Holder<Boolean> isSelected;

//    @Override
//    public boolean isSelected() {
//        if (isSelected != null) {
////            if(isSelected.value != super.isSelected()){
////                super.setSelected(isSelected.value);
////            }
//            return isSelected.value;
//        } else {
//            return super.isSelected();
//        }
//    }

    public void setSelected(Holder<Boolean> value) {
        this.isSelected = value;
    }

//    @Override
//    public void setSelected(boolean value) {
//        if (this.isSelected != null) {
//            this.isSelected.value = value;
//        }
//        super.setSelected(value);
//    }

}
