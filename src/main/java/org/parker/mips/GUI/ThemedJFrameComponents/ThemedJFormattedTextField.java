/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import javax.swing.JFormattedTextField;

/**
 *
 * @author parke
 */
public class ThemedJFormattedTextField extends JFormattedTextField implements ThemableComponent {
    
    public ThemedJFormattedTextField() {
        
        this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.LABLE_TEXT_FONT_PROPERTY_NAME));
        
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.LABLE_TEXT_FONT_PROPERTY_NAME, this);
        
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME, this);
        
        this.setForeground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME));
        
        this.setBackground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME));
        
        this.setBorder(null);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.LABLE_TEXT_FONT_PROPERTY_NAME:
                this.setFont((Font) pce.getNewValue());
                break;
            case ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME:
                this.setForeground((Color) pce.getNewValue());
                break;
            case ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME:
                this.setBackground((Color) pce.getNewValue());
                break;
        }
    }
    
}
