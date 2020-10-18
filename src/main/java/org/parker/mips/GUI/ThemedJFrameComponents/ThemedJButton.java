/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.ThemedJFrameComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;

/**
 *
 * @author parke
 */
public class ThemedJButton extends JButton implements ThemableComponent {
    
    private static Color defaultColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME);
    private static Color selectedColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_CURRENTLY_PRESSED_PROPERTY_NAME);
    private static Color hoverColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_HOVERED_PROPERTY_NAME);
    private static Color hoverSelectedColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.BUTTON_HOVERED_CURRENTLY_PRESSED_PROPERTY_NAME);
    private static Color textColor = (Color) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME);
    
    boolean mouseHovering = false;
    boolean isClicked = false;
    
    public ThemedJButton() {
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
        
        if (isClicked || this.isSelected()) {
            if (this.isSelected() && mouseHovering) {
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
        
        grphcs.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        grphcs.setColor(textColor);
        
        Font font = this.getFont();
        
        grphcs.setFont(font);
        
        //System.out.println(font.isBold());
        
        int width = this.getWidth();
        int height = this.getHeight();
        grphcs.getFontMetrics(font).getWidths();
        String text = this.getText();
        Rectangle rect = new Rectangle(0, 0, width, height);
        
        FontMetrics metrics = grphcs.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        //g.setFont(font);
        // Draw the String
        //g.drawString(text, x, y);

        grphcs.drawString(text, x, y);
    }
    
}