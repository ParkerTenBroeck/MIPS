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
package org.parker.assembleride.gui.components;

import org.parker.assembleride.gui.icons.VerticalLabelIcon;

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
