/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.theme.lookandfeel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import javax.swing.ScrollPaneLayout;

/**
 *
 * @author parke
 */
public class ModernScrollPane extends JScrollPane {

//    private static final int SB_SIZE = 10;
//
//    public ModernScrollPane(Component view) {
//        this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED, null);
//    }
//
//    public ModernScrollPane(int vsbPolicy, int hsbPolicy) {
//        this(null, vsbPolicy, hsbPolicy, null);
//    }
//
//    public ModernScrollPane() {
//        this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED, null);
//    }
//
    public ModernScrollPane() {

    }

    public ModernScrollPane(Color color) {
        // this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED, color);
    }
//
//    public ModernScrollPane(Component view, int vsbPolicy, int hsbPolicy, Color color) {
//
//        setBorder(null);
//
//        // Set ScrollBar UI
//        JScrollBar verticalScrollBar = getVerticalScrollBar();
//        verticalScrollBar.setOpaque(false);
//        verticalScrollBar.setUI(new ModernScrollBarUI(this, color));
//
//        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
//        horizontalScrollBar.setOpaque(false);
//        horizontalScrollBar.setUI(new ModernScrollBarUI(this, color));
//
//        setLayout(new ScrollPaneLayout() {
//            private static final long serialVersionUID = 5740408979909014146L;
//
//            @Override
//            public void layoutContainer(Container parent) {
//                Rectangle availR = ((JScrollPane) parent).getBounds();
//                availR.x = availR.y = 0;
//
//                // viewport
//                Insets insets = parent.getInsets();
//                availR.x = insets.left;
//                availR.y = insets.top;
//                availR.width -= insets.left + insets.right;
//                availR.height -= insets.top + insets.bottom;
//                if (viewport != null) {
//                    viewport.setBounds(availR);
//                }
//
//                boolean vsbNeeded = isVerticalScrollBarfNecessary();
//                boolean hsbNeeded = isHorizontalScrollBarNecessary();
//
//                // vertical scroll bar
//                Rectangle vsbR = new Rectangle();
//                vsbR.width = SB_SIZE;
//                vsbR.height = availR.height - (hsbNeeded ? vsbR.width : 0);
//                vsbR.x = availR.x + availR.width - vsbR.width;
//                vsbR.y = availR.y;
//                if (vsb != null) {
//                    vsb.setBounds(vsbR);
//                }
//
//                // horizontal scroll bar
//                Rectangle hsbR = new Rectangle();
//                hsbR.height = SB_SIZE;
//                hsbR.width = availR.width - (vsbNeeded ? hsbR.height : 0);
//                hsbR.x = availR.x;
//                hsbR.y = availR.y + availR.height - hsbR.height;
//                if (hsb != null) {
//                    hsb.setBounds(hsbR);
//                }
//            }
//        });
//
//        // Layering
//        setComponentZOrder(getVerticalScrollBar(), 0);
//        setComponentZOrder(getHorizontalScrollBar(), 1);
//        setComponentZOrder(getViewport(), 2);
//
//        viewport.setView(view);
//    }
//
//    private boolean isVerticalScrollBarfNecessary() {
//        Rectangle viewRect = viewport.getViewRect();
//        Dimension viewSize = viewport.getViewSize();
//        return viewSize.getHeight() > viewRect.getHeight();
//    }
//
//    private boolean isHorizontalScrollBarNecessary() {
//        Rectangle viewRect = viewport.getViewRect();
//        Dimension viewSize = viewport.getViewSize();
//        return viewSize.getWidth() > viewRect.getWidth();
//    }
}
