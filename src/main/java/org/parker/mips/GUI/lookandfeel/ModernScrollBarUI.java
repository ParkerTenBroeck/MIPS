/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.lookandfeel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.SystemColor.scrollbar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author parke
 */
public class ModernScrollBarUI extends BasicScrollBarUI {

    private JScrollPane sp;

    private static final int SCROLL_BAR_ALPHA_ROLLOVER = 250;
    private static final int SCROLL_BAR_ALPHA = 200;
    private static final int THUMB_SIZE = 8;
    private static final int SB_SIZE = 10;
    private static Color thumbColor = Color.LIGHT_GRAY;

    public ModernScrollBarUI(JScrollPane sp) {
        this.sp = sp;
    }

    public ModernScrollBarUI(JScrollPane sp, Color color) {
        this.sp = sp;
        if (color != null) {
            thumbColor = color;
        }
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new InvisibleScrollBarButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new InvisibleScrollBarButton();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
        int orientation = scrollbar.getOrientation();
        int x = thumbBounds.x;
        int y = thumbBounds.y;

        int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
        width = Math.max(width, THUMB_SIZE);

        int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
        height = Math.max(height, THUMB_SIZE);

        Graphics2D graphics2D = (Graphics2D) g.create();
        graphics2D.setColor(new Color(thumbColor.getRed(), thumbColor.getGreen(), thumbColor.getBlue(), alpha));
        graphics2D.fillRect(x, y, width, height);
        graphics2D.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        sp.repaint();
    }

    /**
     * Invisible Buttons, to hide scroll bar buttons
     */
    private static class InvisibleScrollBarButton extends JButton {

        private static final long serialVersionUID = 1552427919226628689L;

        private InvisibleScrollBarButton() {
            setOpaque(false);
            setFocusable(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
        }
    }
}
