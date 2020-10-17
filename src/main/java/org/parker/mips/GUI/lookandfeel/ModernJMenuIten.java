/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.lookandfeel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JMenuItem;

/**
 *
 * @author parke
 */
public class ModernJMenuIten extends JMenuItem {

    Color defaultColor = new Color(70, 70, 70);
    Color selectedColor = new Color(1, 176, 117);
    Color hoverColor = new Color(102, 102, 102);
    Color hoverSelectedColor = selectedColor.darker();
    Color textColor = new Color(204, 204, 204);

    @Override
    public void paintComponent(Graphics grphcs) {
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setColor(defaultColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

    }
}
