/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.lookandfeel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JMenu;

/**
 *
 * @author parke
 */
public class ModernJMenu extends JMenu {

    Color defaultColor = new Color(70, 70, 70);
    Color selectedColor = new Color(1, 176, 117);
    Color hoverColor = new Color(102, 102, 102);
    Color hoverSelectedColor = selectedColor.darker();
    Color textColor = new Color(204, 204, 204);

    @Override
    public void paintComponent(Graphics grphcs) {
        Graphics2D g2d = (Graphics2D) grphcs;
        if (this.isSelected()) {
            g2d.setColor(selectedColor);
        } else {
            g2d.setColor(defaultColor);
        }

        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

        if (this.getComponents().length != 0) {
            Font font = this.getFont();

            grphcs.setFont(font);
            int width = this.getWidth();
            int height = this.getHeight();
            grphcs.getFontMetrics(font).getWidths();
            String text = ">";
            Rectangle rect = new Rectangle(0, 0, width, height);

            FontMetrics metrics = grphcs.getFontMetrics(font);
            // Determine the X coordinate for the text
            int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g2d.setColor(textColor);
            grphcs.drawString(text, (int)(this.getWidth() - metrics.stringWidth(text) - 2), y);
        }

        Font font = this.getFont();

        grphcs.setFont(font);
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
        g2d.setColor(textColor);
        grphcs.drawString(text, x, y);

    }
}
