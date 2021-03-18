/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updater;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JProgressBar;

/**
 *
 * @author parke
 */
public class ModernProgressBar extends JProgressBar {

    private Color onColor = new Color(70, 70, 70);
    private Color offColor = new Color(110, 110, 110);

    @Override
    public void paint(Graphics g) {
        g.setColor(offColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(onColor);
        g.fillRect(0, 0, (int) (this.getWidth() * this.getPercentComplete()), this.getHeight());
    }
}
