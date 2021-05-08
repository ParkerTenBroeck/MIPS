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
package org.parker.mips.preferences;

import com.google.common.collect.HashMultimap;

import javax.swing.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preference<T extends Serializable> extends Observable {

    private static final Logger LOGGER = Logger.getLogger(Preference.class.getName());

    private HashMultimap<Object, Observer> observerLinkedObject = HashMultimap.create();
    private static final HashMultimap<Object, Preference> optionLinkedObject = HashMultimap.create();

    protected T value;


    public static void removeAllObserversLinkedToObject(Object object) {
        LOGGER.log(Level.FINER, "Removing all linked observers from all options from link: " + object);
        for(Preference o : optionLinkedObject.get(object)){
            o.removeAllObserversFromLink(object);
        }
        optionLinkedObject.removeAll(object);
    }


    protected Preference() {
    }

    protected Preference(T value) {
        this.value = value;
    }

    public T val() {
        return value;
    }

    public void val(T val) {
        LOGGER.log(Level.FINER, "Changed Value of Holder");
        this.value = val;
        this.setChanged();
        this.notifyObservers(val);
        this.clearChanged();
    }


    public void addLikedObserver(Object link, Observer observer){
        LOGGER.log(Level.FINER, "Added Linked: " + link +  " Observer to Option");
        observerLinkedObject.put(link, observer);
        optionLinkedObject.put(link, this);
        this.addObserver(observer);
    }
    public void removeAllObserversFromLink(Object link){
        LOGGER.log(Level.FINER, "Removed all Linked Observer from Option from link: " + link);
        Set<Observer> set = observerLinkedObject.get(link);
        for(Observer o: set){
            this.deleteObserver(o);
        }
        observerLinkedObject.removeAll(link);
    }

    public void LinkJButton(Object link, AbstractButton but) {
        but.setSelected((Boolean) value);
        but.addActionListener((ae) -> {
            if (but.isSelected() != (Boolean) value) {
                this.val((T) (Boolean) (but.isSelected()));
            }
        });
        this.addLikedObserver(link, (o, v) -> {
            if (but.isSelected() != (Boolean) v) {
                but.setSelected((Boolean) v);
            }
        });
    }

    public void LinkJSlider(Object link, JSlider slide) {
        slide.setValue((Integer) value);
        slide.addChangeListener((ex) ->{
            if (slide.getValue() != (Integer) value) {
                val((T) (Integer) (slide.getValue()));
            }
        });

        this.addLikedObserver(link, (o,v) -> {
            if (slide.getValue() != (Integer) v) {
                slide.setValue((Integer) v);
            }
        });
    }

    public void LinkJList(Object link, JList<T> list) {
        list.setSelectedValue(this.val(), true);

        list.addListSelectionListener(e -> {
            if(!this.val().equals(list.getSelectedValue())){
                this.val(list.getSelectedValue());
            }
        });
        this.addLikedObserver(link, (o, arg) -> {
            if(!this.val().equals(list.getSelectedValue())){
                list.setSelectedValue(this.val(), true);
            }
        });

    }

}
