/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.theme.components;

import org.parker.mips.gui.theme.components.ThemableComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import org.parker.mips.Holder;
import org.parker.mips.gui.theme.ThemeHandler;

/**
 *
 * @author parke
 */
public class ThemedJCheckBoxMenuItem extends JCheckBoxMenuItem implements ThemableComponent {
    
    private Holder<Boolean> isSelected;
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME:
                this.setFont((Font) pce.getNewValue());
                break;
            case ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME:
                this.setForeground((Color) pce.getNewValue());
                break;
        }
    }
    
    public ThemedJCheckBoxMenuItem() {
        this.addActionListener((ae) -> {
            setSelected(super.isSelected());
        });
        
        this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME));
        this.setForeground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME));
        
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME, this);
        
        this.setOpaque(false);
        
//        this.setUI(new BasicCheckBoxMenuItemUI() {
//            
//            @Override
//            public void paint(Graphics g, JComponent j) {
//                super.paint(g, j);
//            }
//        });
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
