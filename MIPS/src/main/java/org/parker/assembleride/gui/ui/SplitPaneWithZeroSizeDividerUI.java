/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.parker.assembleride.gui.ui;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;

public class SplitPaneWithZeroSizeDividerUI extends BasicSplitPaneUI {

    private final int dividerSize = 1;
    protected int dividerDragSize = 9;
    protected int dividerDragOffset = dividerDragSize / 2;

    private final PropertyChangeListener ensureSize;

    @SuppressWarnings("unused")
    public static ComponentUI createUI( JComponent c ) {
        return new SplitPaneWithZeroSizeDividerUI();
    }

    public SplitPaneWithZeroSizeDividerUI(){
        ensureSize = evt -> {
            if(evt.getPropertyName().equals(JSplitPane.DIVIDER_SIZE_PROPERTY)
                    && splitPane != null
                    && ((Number)evt.getNewValue()).intValue() != dividerSize){
                dividerDragSize = ((Number)evt.getNewValue()).intValue();
                dividerDragOffset = (dividerDragSize - 1) / 2;
                splitPane.setDividerSize(dividerSize);
            }
        };
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        splitPane.setDividerSize(dividerSize);
        c.addPropertyChangeListener(ensureSize);
        //this is to ensure that the overlapping mouse listener will be called before any other child components
        splitPane.setComponentZOrder(divider, splitPane.getComponentZOrder(splitPane.getRightComponent()));

    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        c.removePropertyChangeListener(ensureSize);
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new ZeroSizeDivider( this );
    }

    public class ZeroSizeDivider extends BasicSplitPaneDivider {

        public ZeroSizeDivider( BasicSplitPaneUI ui ) {
            super( ui );
            setBackground(UIManager.getColor( "controlShadow" ) );
            this.addMouseListener(new MouseAdapter() {

                Component last;

                @Override
                public void mouseEntered(MouseEvent e) {

                    Point location = e.getLocationOnScreen();

                    last = splitPane.getRightComponent().getComponentAt(location);
                    if(last == null) { //if the component was on the left side
                        last = splitPane.getLeftComponent().getComponentAt(location);
                    }

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if(last != null && last != divider){
                        last.dispatchEvent(e);
                    }
                }
            });
        }

        @Override
        public Point getLocation() {
            Point p = super.getLocation();
            if(orientation == JSplitPane.HORIZONTAL_SPLIT){
                p.x += dividerDragOffset;
            }else{
                p.y += dividerDragOffset;
            }
            return p;
        }

        @Override
        protected void setMouseOver(boolean mouseOver) {
            if(mouseOver != isMouseOver()) {
                if (mouseOver) {
                    splitPane.setCursor((orientation == JSplitPane.HORIZONTAL_SPLIT) ?
                            Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR) :
                            Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                } else {
                    splitPane.setCursor(Cursor.getDefaultCursor());
                }
            }
            super.setMouseOver(mouseOver);
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            if( orientation == JSplitPane.HORIZONTAL_SPLIT ) {
                x -= dividerDragOffset;
                width = dividerDragSize;
            } else {
                y -= dividerDragOffset;
                height = dividerDragSize;
            }
            super.setBounds( x, y, width, height );
        }

        @Override
        public void paint( Graphics g ) {
            g.setColor( getBackground() );
            if( orientation == JSplitPane.HORIZONTAL_SPLIT ) {
                g.drawLine( dividerDragOffset, 0, dividerDragOffset, getHeight() - 1 );
            }else {
                g.drawLine( 0, dividerDragOffset, getWidth() - 1, dividerDragOffset );
            }
        }
    }
}

