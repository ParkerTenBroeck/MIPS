/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.plaf.MenuBarUI;

/**
 *
 * @author parke
 */
public class ThemedJMenuBar extends JMenuBar implements ThemableComponent {

    Color backgroundColor;

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME:
                backgroundColor = (Color) pce.getNewValue();
                break;
        }
    }

    public ThemedJMenuBar() {

        backgroundColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME);

//        this.setUI(new MenuBarUI() {
//            @Override
//            public void paint(Graphics g, JComponent j) {
//                g.setColor(backgroundColor);
//                g.fillRect(0, 0, j.getWidth(), j.getHeight());
//            }
//        });
    }
}
