package org.parker.mips.gui.icons;

import com.formdev.flatlaf.icons.FlatCheckBoxIcon;
import java.awt.*;

public class FlatMinusIcon extends FlatCheckBoxIcon {

    public FlatMinusIcon(){

    }

    @Override
    protected void paintBorder( Component c, Graphics2D g ) {
        if(g.getColor().equals(hoverBorderColor)) {
            int arcwh = arc;
            g.fillRoundRect(1, 5, 13, 5, arcwh, arcwh);
        }
    }

    @Override
    protected void paintBackground(Component c, Graphics2D g) {
        int arcwh = arc;
        g.fillRoundRect( 2, 6, 11, 3, arcwh, arcwh );
    }

    @Override
    protected void paintCheckmark(Component c, Graphics2D g) {}

    @Override
    protected void paintIndeterminate(Component c, Graphics2D g) {}
}
