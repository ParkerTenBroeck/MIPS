/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import javax.swing.JLabel;

/**
 *
 * @author parke
 */
public class ThemedJLable extends JLabel implements ThemableComponent {

    public ThemedJLable(boolean activeText) {

        this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.LABLE_TEXT_FONT_PROPERTY_NAME));
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.LABLE_TEXT_FONT_PROPERTY_NAME, this);

        final String name;

        if (activeText) {
            name = ThemeHandler.TEXT_COLOR_ACTIVE_PROPERTY_NAME;
        } else {
            name = ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME;
            //ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME, this);
        }

        ThemeHandler.addPropertyChangeListenerFromName(name, this);
        this.setForeground((Color) ThemeHandler.getThemeObjectFromThemeName(name));

        this.setOpaque(false);
    }

    public ThemedJLable() {
        this(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME:
                this.setForeground((Color) pce.getNewValue());
                break;
            case ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME:
                this.setForeground((Color) pce.getNewValue());
                break;
            case ThemeHandler.TEXT_COLOR_ACTIVE_PROPERTY_NAME:
                this.setForeground((Color) pce.getNewValue());
                break;
            case ThemeHandler.LABLE_TEXT_FONT_PROPERTY_NAME:
                this.setFont((Font) pce.getNewValue());
                break;
        }
    }
}
