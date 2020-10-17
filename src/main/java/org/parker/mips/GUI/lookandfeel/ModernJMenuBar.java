/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.lookandfeel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JMenuBar;

/**
 *
 * @author parke
 */
public class ModernJMenuBar extends JMenuBar {

    Color defaultColor = new Color(51, 51, 51);

    @Override
    public void paintComponent(Graphics grphcs) {
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setColor(defaultColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

    }
}
