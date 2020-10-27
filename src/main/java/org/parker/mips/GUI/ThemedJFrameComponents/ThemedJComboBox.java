/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.ButtonModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author parke
 */
public class ThemedJComboBox extends JComboBox implements ThemableComponent {

    private Color backgroundColor;
    private Color textColor;
    private Color hoveredBackgroundColor;
    private Color hoveredTextColor;
    private Font textFont;

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME:
                backgroundColor = (Color) pce.getNewValue();
                this.setBackground(backgroundColor);
                break;
            case ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME:
                this.textColor = (Color) pce.getNewValue();
                this.setForeground(textColor);
                break;
            case ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME:
                this.setFont((Font) pce.getNewValue());
                textFont = (Font) pce.getNewValue();
                break;
            case ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME:
                //this.setFont((Font) pce.getNewValue());
                hoveredBackgroundColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME:
                //this.setFont((Font) pce.getNewValue());
                hoveredTextColor = (Color) pce.getNewValue();
                break;
        }
    }

    public ThemedJComboBox() {
        this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME));

        //this.setForeground((Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME));
        textColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME);
        this.setForeground(textColor);

        backgroundColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME);
        this.setBackground(backgroundColor);

        hoveredBackgroundColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME);
        hoveredTextColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME);

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_2_PROPERTY_NAME, this);

        this.setRenderer(new CountryRenderer());
        this.setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {

                JButton b = new BasicArrowButton(BasicArrowButton.SOUTH) {
                    
                    @Override
                    public void paint(Graphics g
                    ) {

                        int height = getHeight();
                        int size = height / 4;

                        int x = (getWidth() - size) / 2;
                        int y = (height - size) / 2;

//                        ButtonModel m = getModel();
//                        if (m.isArmed()) {
//                            x++;
//                            y++;
//                        }
                        paintTriangleSouth(g, x, y, size);
                    }

                    private void paintTriangleSouth(Graphics g, int x, int y, int size) {
                        int tipX = x + (size - 2) / 2;
                        int tipY = y + (size - 1);
                        int baseX1 = tipX - (size - 1);
                        int baseX2 = tipX + (size - 1);
                        int baseY = y;
                        Polygon triangle = new Polygon();
                        triangle.addPoint(tipX, tipY);
                        triangle.addPoint(baseX1, baseY);
                        triangle.addPoint(baseX2, baseY);

                        g.setColor(textColor);
                        g.fillPolygon(triangle);
                        g.drawPolygon(triangle);

                    }
                };
                b.setOpaque(false);
                return b;

            }

//            @Override
//            public void paintCurrentValue(Graphics grphcs, Rectangle rctngl, boolean bln) {
//                // compiled code
//            }
//            @Override
//            public void paintCurrentValueBackground(Graphics grphcs, Rectangle rctngl, boolean bln) {
//                // compiled code
//            }
            @Override
            public void paint(Graphics g, JComponent jc) {
                g.setColor(backgroundColor);
                g.fillRect(0, 0, jc.getWidth(), jc.getHeight());
                Rectangle rect = rectangleForCurrentValue();
                paintCurrentValueBackground(g, rect, false);
                paintCurrentValue(g, rect, false);
                //super.paint(g, jc);
            }
        }
        );

        this.setBorder(null);

    }

    class CountryRenderer extends JLabel implements ListCellRenderer<String> {

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String text, int index,
                boolean isSelected, boolean cellHasFocus) {

            setOpaque(true);
            setFont(textFont);

            if (isSelected) {
                setBackground(hoveredBackgroundColor);
                setForeground(hoveredTextColor);
            } else {
                setBackground(backgroundColor);
                setForeground(textColor);
            }

            if (index == -1) {
                setText(text);
                setBackground(Color.red);
                setForeground(Color.BLUE);
            } else {
                setText(text);
            }
            return this;
        }

    }

}
