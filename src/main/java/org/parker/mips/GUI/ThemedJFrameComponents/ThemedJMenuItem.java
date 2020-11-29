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
import javax.swing.JMenuItem;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicMenuUI;

/**
 *
 * @author parke
 */
public class ThemedJMenuItem extends JMenuItem implements ThemableComponent {

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

    public ThemedJMenuItem() {
        this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME));
        this.setForeground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME));

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME, this);
        
        this.setOpaque(true);
        
//        this.setUI(new BasicMenuItemUI() {
//
//            @Override
//            public void paint(Graphics g, JComponent j) {
//                super.paint(g, j);
//            }
//        });

        //this.setPreferredSize(new Dimension(this.getSize().width, this.getPreferredSize().height));
    }
}
