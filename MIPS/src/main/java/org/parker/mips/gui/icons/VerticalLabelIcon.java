package org.parker.mips.gui.icons;

import javax.swing.*;
import java.awt.*;

public class VerticalLabelIcon implements Icon {

    protected final JLabel label;
    protected final boolean bottomToTop;

    public VerticalLabelIcon(JLabel label) {
        this(label, true);
    }

    public VerticalLabelIcon(JLabel label, boolean bottomToTop) {
        this.label = label;
        label.setSize(label.getPreferredSize());
        this.bottomToTop = bottomToTop;
    }


    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        ((Graphics2D) g).rotate(bottomToTop ? -Math.toRadians(90) : Math.toRadians(90), label.getWidth() / 2, label.getHeight() / 2);
        g.translate((int) Math.round(-x - calcIconWidth() / 2.0 + calcIconHeight() / 2.0),
                (int) Math.round(-y + calcIconHeight() / 2.0 - calcIconWidth() / 2.0));
        label.paint(g);
    }

    protected double calcIconHeight() {
        return label.getWidth() * (bottomToTop ? -1 : 1);
    }

    protected double calcIconWidth() {
        return label.getHeight() * (bottomToTop ? -1 : 1);
    }

    @Override
    public int getIconWidth() {
        return label.getHeight();
    }

    @Override
    public int getIconHeight() {
        return label.getWidth();
    }
}
