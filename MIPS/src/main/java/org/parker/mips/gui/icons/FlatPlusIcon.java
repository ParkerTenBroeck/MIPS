package org.parker.mips.gui.icons;

import com.formdev.flatlaf.icons.FlatCheckBoxIcon;

import java.awt.*;

public class FlatPlusIcon extends FlatCheckBoxIcon {

    public FlatPlusIcon(){

    }

    @Override
    protected void paintBorder( Component c, Graphics2D g ) {
        if(g.getColor().equals(hoverBorderColor)) {
            int arcwh = arc;
            g.fillRoundRect(1, 5, 13, 5, arcwh, arcwh);
            g.fillRoundRect(5, 1, 5, 13, arcwh, arcwh);
        }
    }

    @Override
    protected void paintBackground(Component c, Graphics2D g) {
        int arcwh = arc;
        g.fillRoundRect( 2, 6, 11, 3, arcwh, arcwh );
        g.fillRoundRect(6, 2, 3, 11, arcwh, arcwh);
    }

    @Override
    protected void paintCheckmark(Component c, Graphics2D g) {}

    @Override
    protected void paintIndeterminate(Component c, Graphics2D g) {}
}
