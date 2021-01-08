/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.theme.components;


import javax.swing.JSlider;
import org.parker.mips.Holder;

/**
 *
 * @author parke
 */
public class ThemedJSlider extends JSlider {

    private Holder<Integer> value;

//    @Override
//    public void setValue(int value) {
//        if (this.value != null) {
//            this.value.value = value;
//        } else {
//            super.setValue(value);
//        }
//    }
    public void setValue(Holder<Integer> value) {
        this.value = value;
    }

//    @Override
//    public int getValue() {
//        if (this.value != null) {
//            return this.value.value;
//        } else {
//            return super.getValue();
//        }
//    }
}
