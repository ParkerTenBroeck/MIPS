/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.EventListener;
import javax.swing.AbstractButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author parke
 * @param <T>
 */
public class Holder<T> {
    
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
        if (valueListeners != null) {
            for (ValueListener vl : valueListeners) {
                vl.valueChanged(new ValueEvent(this));
            }
        }
    }
    private ArrayList<ValueListener> valueListeners;
    
    public void addValueListener(ValueListener vl) {
        if (valueListeners == null) {
            valueListeners = new ArrayList();
        }
        valueListeners.add(vl);
    }
    
    public static interface ValueListener extends EventListener {
        
        public void valueChanged(ValueEvent e);
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
        this.addValueListener((vl) -> {
            if (but.isSelected() != (Boolean) value) {
                but.setSelected((Boolean) value);
            }
        });
    }
    
    public void LinkJSlider(JSlider slide) {
        slide.setValue((Integer) value);
        slide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                if (slide.getValue() != (Integer) value) {
                    val((T) (Integer) (slide.getValue()));
                }
            }
        });
        this.addValueListener((vl) -> {
            if (slide.getValue() != (Integer) value) {
                slide.setValue((Integer) value);
            }
        });
    }
}
