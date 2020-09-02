/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.lookandfeel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author parke
 */
public class ModernButtonUI extends BasicButtonUI implements PropertyChangeListener{

    Color defaultColor = new Color(70, 70, 70);
    Color selectedColor = new Color(1, 176, 117);
    Color hoverColor = new Color(102, 102, 102);
    Color hoverSelectedColor = selectedColor.darker();
    Color textColor = new Color(204, 204, 204);

    boolean mouseHovering = false;
    boolean isClicked = false;

    Rectangle buttonRect = null;

    JComponent button;

    public ModernButtonUI(JComponent button) {
        this.button = button;
        button.addMouseListener(new ml());
        ThemeHandler.addPropertyChangeListenerFromName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME, this);
    }

    public ModernButtonUI(JComponent button, Rectangle buttonRect) {
        this.button = button;
        this.buttonRect = buttonRect;
        button.addMouseListener(new ml());
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch(pce.getPropertyName()){
            case ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME:
                defaultColor = (Color) pce.getNewValue();
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
            button.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            isClicked = false;
            button.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            mouseHovering = true;
            button.repaint();
        }

        @Override
        public void mouseExited(MouseEvent me) {
            mouseHovering = false;
            button.repaint();
        }

    }

    @Override
    public void paint(Graphics grphcs, JComponent jc) {

        if (buttonRect == null) {
            if (isClicked || ((AbstractButton) jc).isSelected()) {
                if (((AbstractButton) jc).isSelected() && mouseHovering) {
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

            grphcs.fillRect(0, 0, jc.getWidth(), jc.getHeight());

            grphcs.setColor(textColor);

            Font font = jc.getFont();

            grphcs.setFont(font);

            int width = jc.getWidth();
            int height = jc.getHeight();
            grphcs.getFontMetrics(font).getWidths();
            String text = ((AbstractButton) jc).getText();
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
        } else {
            if (isClicked || ((AbstractButton) jc).isSelected()) {
                if (((AbstractButton) jc).isSelected() && mouseHovering) {
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

            grphcs.fillRect(0, (int) (jc.getHeight() / 2.0 - buttonRect.height / 2.0), buttonRect.width, buttonRect.height);

            grphcs.setColor(textColor);

            Font font = jc.getFont();

            grphcs.setFont(font);

            int width = jc.getWidth();
            int height = jc.getHeight();
            grphcs.getFontMetrics(font).getWidths();
            String text = ((AbstractButton) jc).getText();
            Rectangle rect = new Rectangle(0, 0, width, height);

            FontMetrics metrics = grphcs.getFontMetrics(font);
            // Determine the X coordinate for the text

            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
            // Set the font
            //g.setFont(font);
            // Draw the String
            //g.drawString(text, x, y);

            grphcs.drawString(text, buttonRect.width + 5, y);
        }
    }

}
