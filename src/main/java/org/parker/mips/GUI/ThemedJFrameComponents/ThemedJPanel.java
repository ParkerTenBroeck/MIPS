/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.ThemedJFrameComponents;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditorSupport;
import javax.swing.JPanel;

/**
 *
 * @author parke
 */
public class ThemedJPanel extends JPanel implements ThemableComponent {

    private String backgroundColorName;

    public enum backgroundColorType {
        BACKGROUND_COLOR_1, BACKGROUND_COLOR_2, BACKGROUND_COLOR_3
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME:
                this.setBackground((Color) pce.getNewValue());
                break;
            case ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME:
                this.setBackground((Color) pce.getNewValue());
                break;
            case ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME:
                this.setBackground((Color) pce.getNewValue());
                break;
            default:
        }
    }

    public void setBackground(final String themeName) {
        ThemeHandler.removePropertyChangeListenerFromName(backgroundColorName, this);
        ThemeHandler.addPropertyChangeListenerFromName(themeName, this);
        backgroundColorName = themeName;
        //System.out.println(themeName);
        super.setBackground((Color) ThemeHandler.getThemeObjectFromThemeName(themeName));
        //System.out.println(this.getBackground());
    }

    @Override
    public void setBackground(Color color) {

    }

    @Override
    public void setForeground(Color color) {

    }

    public ThemedJPanel() {
        this(backgroundColorType.BACKGROUND_COLOR_1);
    }

    public ThemedJPanel(backgroundColorType type) {

        String name;

        switch (type) {
            case BACKGROUND_COLOR_1:
                name = ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME;
                //ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME, this);
                break;
            case BACKGROUND_COLOR_2:
                name = ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME;
                //ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME, this);
                break;
            case BACKGROUND_COLOR_3:
                name = ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME;
                //ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME, this);
                break;

            default:
                name = ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME;
        }
        backgroundColorName = name; 
        ThemeHandler.addPropertyChangeListenerFromName(name, this);
        super.setBackground((Color) ThemeHandler.getThemeObjectFromThemeName(name));
    }

}

class ConnectionInfo extends PropertyEditorSupport {

}
