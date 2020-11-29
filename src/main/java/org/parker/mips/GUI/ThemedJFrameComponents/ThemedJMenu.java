/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.plaf.basic.BasicMenuUI;

/**
 *
 * @author parke
 */
public class ThemedJMenu extends JMenu implements ThemableComponent {

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

    public ThemedJMenu() {        
        this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME));
        this.setForeground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME));

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME, this);
        
        
        
//        this.setUI(new BasicMenuUI() {
//
//            @Override
//            public void paint(Graphics g, JComponent j) {
//                super.paint(g, j);
//            }
//        });
        //this.setPreferredSize(this.getPreferredSize());
    }

}
