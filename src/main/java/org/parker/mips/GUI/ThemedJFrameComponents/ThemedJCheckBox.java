/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.ThemedJFrameComponents;

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

/**
 *
 * @author parke
 */
public class ThemedJCheckBox extends JCheckBox implements ThemableComponent {

    private static Color defaultColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME);
    private static Color selectedColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME);
    private static Color hoverColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_HOVERED_PROPERTY_NAME);
    private static Color hoverSelectedColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME);
    private static Color textColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME);

    private Holder<Boolean> isSelected;
    private boolean isClicked = false;

    boolean mouseHovering = false;

    public ThemedJCheckBox() {
        this.setOpaque(false);
        this.addMouseListener(new ml());

        this.setFont((Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME));
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME, this);

        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_HOVERED_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME, this);
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME:
                defaultColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME:
                selectedColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.BUTTON_HOVERED_PROPERTY_NAME:
                hoverColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME:
                hoverSelectedColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME:
                textColor = (Color) pce.getNewValue();
                break;
            case ThemeHandler.BUTTON_TEXT_FONT_PROPERTY_NAME:
                this.setFont((Font) pce.getNewValue());
                break;
        }
    }

    private class ml implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {

        }

        @Override
        public void mousePressed(MouseEvent me) {
            setSelected(!isSelected());
            isClicked = true;
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            isClicked = false;
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            mouseHovering = true;

            repaint();
        }

        @Override
        public void mouseExited(MouseEvent me) {
            mouseHovering = false;
            repaint();
        }

    }

    @Override
    public void paint(Graphics grphcs) {

        grphcs.setColor(textColor);

        Font font = this.getFont();

        grphcs.setFont(font);

        int textWidth = this.getWidth();
        int textHeight = this.getHeight();
        grphcs.getFontMetrics(font).getWidths();
        String text = this.getText();
        Rectangle rect = new Rectangle(0, 0, textWidth, textHeight);

        FontMetrics metrics = grphcs.getFontMetrics(font);
        // Determine the X coordinate for the text

        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        //g.setFont(font);
        // Draw the String
        //g.drawString(text, x, y);
        int offset = 4;

        int boxHeight = metrics.getHeight() - 2;

        grphcs.drawString(text, boxHeight + 3 + offset, y);

        if (this.isSelected()) {
            if (this.isSelected() && mouseHovering && !isClicked) {
                grphcs.setColor(hoverSelectedColor);
            } else {
                grphcs.setColor(selectedColor);
            }
        } else {
            if (mouseHovering) {
                grphcs.setColor(hoverColor);
            } else {
                grphcs.setColor(defaultColor);
            }
        }

        grphcs.fillRect(offset, (int) (this.getHeight() / 2.0 - boxHeight / 2.0), boxHeight, boxHeight);
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
