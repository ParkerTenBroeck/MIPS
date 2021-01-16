/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author parke
 * @param <T>
 */
public class Holder<T> extends Observable {
    
    protected T value;

    public Holder() {
    }

    public Holder(T value) {
        this.value = value;
    }

    public T val() {
        return value;
    }
    
    public void val(T val) {
        this.value = val;
        this.setChanged();
        this.notifyObservers(val);
        this.clearChanged();
    }
    
    public static class ValueEvent {
        
        public final Object source;
        
        ValueEvent(Object source) {
            this.source = source;
        }
    }
    
    public void LinkJButton(AbstractButton but) {
        but.setSelected((Boolean) value);
        but.addActionListener((ae) -> {
            if (but.isSelected() != (Boolean) value) {
                this.val((T) (Boolean) (but.isSelected()));
            }
        });
        this.addObserver((o, v) -> {
            if (but.isSelected() != (Boolean) v) {
                but.setSelected((Boolean) v);
            }
        });
    }
    
    public void LinkJSlider(JSlider slide) {
        slide.setValue((Integer) value);
        slide.addChangeListener((ex) ->{
                if (slide.getValue() != (Integer) value) {
                    val((T) (Integer) (slide.getValue()));
                }
            });

        this.addObserver((o,v) -> {
            if (slide.getValue() != (Integer) v) {
                slide.setValue((Integer) v);
            }
        });
    }
}
