package org.parker.mips.gui.components;

import org.parker.mips.gui.icons.VerticalLabelIcon;

import javax.swing.*;

public class VerticalJLabel extends JLabel {

    private final JLabel label;

    public VerticalJLabel(String text){
        this(text, true);
    }

    public VerticalJLabel(String text, Icon icon){
        this(text, icon, true);
    }

    public VerticalJLabel(String text, boolean bottomToTop){
        this(text, null, bottomToTop);
    }

    public VerticalJLabel(String text, Icon icon, boolean bottomToTop){
        label = new JLabel(text){
            @Override
            public void updateUI() {
                super.updateUI();
                setSize(getPreferredSize());
            }
        };
        if(icon != null) {
            label.setIcon(icon);
        }
        super.setIcon(new VerticalLabelIcon(label, bottomToTop));
    }

    @Override
    public void setText(String text) {
        if(label != null) {
            this.label.setText(text);
        }
    }

    @Override
    public void setIcon(Icon icon) {
        if(label != null) {
            label.setIcon(icon);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if(label != null) {
            label.updateUI();
        }
    }
}
