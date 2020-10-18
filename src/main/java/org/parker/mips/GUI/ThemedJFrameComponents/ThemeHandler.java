/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.parker.mips.GUI.Main_GUI;

/**
 *
 * @author parke
 */
public class ThemeHandler {

    private static final ThemeHandler instance = new ThemeHandler();

    //start of theme variables declaration
    private static Font buttonTextFont = new Font("Tahoma", Font.BOLD, 13);
    private static Font lableTextFont = new Font("Tahoma", Font.BOLD, 13);;

    private static Color textColor1 = new Color(204, 204, 204);
    private static Color textColor2 = new Color(204, 204, 204);
    private static Color textColorActive = new Color(1, 176, 117);

    private static Color textAreaBackground1;
    private static Color textAreaBackground2;
    private static Color textAreaBackground3;

    private static Color backgroundColor1 = new Color(51, 51, 51);
    private static Color backgroundColor2 = new Color(70, 70, 70);
    private static Color backgroundColor3 = new Color(102, 102, 102);
    private static Color backgroundColor4 = new Color(170, 170, 170);

    private static Color buttonDefaultColor = new Color(70, 70, 70);
    private static Color buttonHovered = new Color(102, 102, 102);
    private static Color buttonCurrentlyPressed = new Color(1, 176, 117);
    private static Color buttonHoveredCurrentlyPressed = buttonCurrentlyPressed.darker();
    //end of theme variable declaration

    //start of propery change support declaration 
    private static final PropertyChangeSupport BUTTON_TEXT_FONT_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport LABLE_TEXT_FONT_PROP = new PropertyChangeSupport(instance);

    private static final PropertyChangeSupport TEXT_COLOR_1_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport TEXT_COLOR_2_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport TEXT_COLOR_ACTIVE_PROP = new PropertyChangeSupport(instance);

    private static final PropertyChangeSupport TEXT_AREA_BACKGROUND_1_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport TEXT_AREA_BACKGROUND_2_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport TEXT_AREA_BACKGROUND_3_PROP = new PropertyChangeSupport(instance);

    private static final PropertyChangeSupport BACKGROUND_COLOR_1_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport BACKGROUND_COLOR_2_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport BACKGROUND_COLOR_3_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport BACKGROUND_COLOR_4_PROP = new PropertyChangeSupport(instance);

    private static final PropertyChangeSupport BUTTON_DEFAULT_COLOR_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport BUTTON_HOVERED_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport BUTTON_CURRENTLY_PRESSED_PROP = new PropertyChangeSupport(instance);
    private static final PropertyChangeSupport BUTTON_HOVERED_CURRENTLY_PRESSED_PROP = new PropertyChangeSupport(instance);
    //end of property change support declaration

    //start of property name declaration
    public static final String BUTTON_TEXT_FONT_PROPERTY_NAME = "buttonTextFont";
    public static final String LABLE_TEXT_FONT_PROPERTY_NAME = "lableTextFont";

    public static final String TEXT_COLOR_1_PROPERTY_NAME = "textColor1";
    public static final String TEXT_COLOR_2_PROPERTY_NAME = "textColor2";
    public static final String TEXT_COLOR_ACTIVE_PROPERTY_NAME = "textColorActive";

    public static final String TEXT_AREA_BACKGROUND_1_PROPERTY_NAME = "textAreaBackground1";
    public static final String TEXT_AREA_BACKGROUND_2_PROPERTY_NAME = "textAreaBackground2";
    public static final String TEXT_AREA_BACKGROUND_3_PROPERTY_NAME = "textAreaBackground3";

    public static final String BACKGROUND_COLOR_1_PROPERTY_NAME = "backgroundColor1";
    public static final String BACKGROUND_COLOR_2_PROPERTY_NAME = "backgroundColor2";
    public static final String BACKGROUND_COLOR_3_PROPERTY_NAME = "backgroundColor3";
    public static final String BACKGROUND_COLOR_4_PROPERTY_NAME = "backgroundColor4";

    public static final String BUTTON_DEFAULT_COLOR_PROPERTY_NAME = "buttonDefaultColor";
    public static final String BUTTON_HOVERED_PROPERTY_NAME = "buttonHovered";
    public static final String BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME = "buttonCurrentlyPressed";
    public static final String BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME = "buttonHoveredCurrentlyPressed";
    //end of property name declaration

    public static ThemeHandler getInstance() {
        return instance;
    }

    public static void addPropertyChangeListenerFromName(String propertyName, PropertyChangeListener listener) {
        getThemeFromName(propertyName).pcs.addPropertyChangeListener(listener);
    }
    public static void removePropertyChangeListenerFromName(String propertyName, PropertyChangeListener listener){
        getThemeFromName(propertyName).pcs.removePropertyChangeListener(listener);
    }

    public static Object getThemeObjectFromThemeName(String propertyName) {
        return getThemeFromName(propertyName).themeVariable;
    }

    public static void setThemeFromName(String propertyName, Object themeObject, boolean firePropertyChange, boolean repaintFrame) {
        Theme tempTheme = getThemeFromName(propertyName);
        setThemeVariableFromName(propertyName, themeObject);
        if (firePropertyChange) {
            tempTheme.pcs.firePropertyChange(new PropertyChangeEvent(instance, propertyName, tempTheme.themeVariable, themeObject));
        }
        if (repaintFrame) {
            Main_GUI.getFrame().repaint();
        }

    }

    private static void setThemeVariableFromName(String name, Object themeObject) {
        switch (name) {

            case BUTTON_TEXT_FONT_PROPERTY_NAME:
                buttonTextFont = (Font) themeObject;
                return;
            case LABLE_TEXT_FONT_PROPERTY_NAME:
                lableTextFont = (Font) themeObject;
                return;

            case TEXT_COLOR_1_PROPERTY_NAME:
                textColor1 = (Color) themeObject;
                return;
            case TEXT_COLOR_2_PROPERTY_NAME:
                textColor2 = (Color) themeObject;
                return;
            case TEXT_COLOR_ACTIVE_PROPERTY_NAME:
                textColorActive = (Color) themeObject;
                return;

            case TEXT_AREA_BACKGROUND_1_PROPERTY_NAME:
                textAreaBackground1 = (Color) themeObject;
                return;
            case TEXT_AREA_BACKGROUND_2_PROPERTY_NAME:
                textAreaBackground2 = (Color) themeObject;
                return;
            case TEXT_AREA_BACKGROUND_3_PROPERTY_NAME:
                textAreaBackground3 = (Color) themeObject;
                return;

            case BACKGROUND_COLOR_1_PROPERTY_NAME:
                backgroundColor1 = (Color) themeObject;
                return;
            case BACKGROUND_COLOR_2_PROPERTY_NAME:
                backgroundColor2 = (Color) themeObject;
                return;
            case BACKGROUND_COLOR_3_PROPERTY_NAME:
                backgroundColor3 = (Color) themeObject;
                return;
            case BACKGROUND_COLOR_4_PROPERTY_NAME:
                backgroundColor4 = (Color) themeObject;
                return;

            case BUTTON_DEFAULT_COLOR_PROPERTY_NAME:
                buttonDefaultColor = (Color) themeObject;
                return;
            case BUTTON_HOVERED_PROPERTY_NAME:
                buttonHovered = (Color) themeObject;
                return;
            case BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME:
                buttonCurrentlyPressed = (Color) themeObject;
                return;
            case BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME:
                buttonHoveredCurrentlyPressed = (Color) themeObject;
                return;

            default:
                return;
        }
    }

    private static Theme getThemeFromName(String name) {
        switch (name) {

            case BUTTON_TEXT_FONT_PROPERTY_NAME:
                return new Theme(buttonTextFont, BUTTON_TEXT_FONT_PROP, BUTTON_TEXT_FONT_PROPERTY_NAME);
            case LABLE_TEXT_FONT_PROPERTY_NAME:
                return new Theme(lableTextFont, LABLE_TEXT_FONT_PROP, LABLE_TEXT_FONT_PROPERTY_NAME);

            case TEXT_COLOR_1_PROPERTY_NAME:
                return new Theme(textColor1, TEXT_COLOR_1_PROP, TEXT_COLOR_1_PROPERTY_NAME);
            case TEXT_COLOR_2_PROPERTY_NAME:
                return new Theme(textColor2, TEXT_COLOR_2_PROP, TEXT_COLOR_2_PROPERTY_NAME);
            case TEXT_COLOR_ACTIVE_PROPERTY_NAME:
                return new Theme(textColorActive, TEXT_COLOR_ACTIVE_PROP, TEXT_COLOR_ACTIVE_PROPERTY_NAME);

            case TEXT_AREA_BACKGROUND_1_PROPERTY_NAME:
                return new Theme(textAreaBackground1, TEXT_AREA_BACKGROUND_1_PROP, TEXT_AREA_BACKGROUND_1_PROPERTY_NAME);
            case TEXT_AREA_BACKGROUND_2_PROPERTY_NAME:
                return new Theme(textAreaBackground2, TEXT_AREA_BACKGROUND_2_PROP, TEXT_AREA_BACKGROUND_2_PROPERTY_NAME);
            case TEXT_AREA_BACKGROUND_3_PROPERTY_NAME:
                return new Theme(textAreaBackground3, TEXT_AREA_BACKGROUND_3_PROP, TEXT_AREA_BACKGROUND_3_PROPERTY_NAME);

            case BACKGROUND_COLOR_1_PROPERTY_NAME:
                return new Theme(backgroundColor1, BACKGROUND_COLOR_1_PROP, BACKGROUND_COLOR_1_PROPERTY_NAME);
            case BACKGROUND_COLOR_2_PROPERTY_NAME:
                return new Theme(backgroundColor2, BACKGROUND_COLOR_2_PROP, BACKGROUND_COLOR_2_PROPERTY_NAME);
            case BACKGROUND_COLOR_3_PROPERTY_NAME:
                return new Theme(backgroundColor3, BACKGROUND_COLOR_3_PROP, BACKGROUND_COLOR_3_PROPERTY_NAME);
            case BACKGROUND_COLOR_4_PROPERTY_NAME:
                return new Theme(backgroundColor4, BACKGROUND_COLOR_4_PROP, BACKGROUND_COLOR_4_PROPERTY_NAME);

            case BUTTON_DEFAULT_COLOR_PROPERTY_NAME:
                return new Theme(buttonDefaultColor, BUTTON_DEFAULT_COLOR_PROP, BUTTON_DEFAULT_COLOR_PROPERTY_NAME);
            case BUTTON_HOVERED_PROPERTY_NAME:
                return new Theme(buttonHovered, BUTTON_HOVERED_PROP, BUTTON_HOVERED_PROPERTY_NAME);
            case BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME:
                return new Theme(buttonCurrentlyPressed, BUTTON_CURRENTLY_PRESSED_PROP, BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME);
            case BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME:
                return new Theme(buttonHoveredCurrentlyPressed, BUTTON_HOVERED_CURRENTLY_PRESSED_PROP, BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME);

            default:
                return null;
        }
    }
}

class Theme {

    public final Object themeVariable;
    public final PropertyChangeSupport pcs;
    public final String propertyName;

    public Theme(Object themeVariable, PropertyChangeSupport pcs, String propertyName) {
        this.themeVariable = themeVariable;
        this.pcs = pcs;
        this.propertyName = propertyName;
    }
}
