package org.parker.mips.preferences;

import com.google.common.collect.HashMultimap;
import org.parker.mips.OptionsHandler;

import javax.swing.*;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preference<T extends Serializable> extends Observable {

    private static final Logger LOGGER = Logger.getLogger(OptionsHandler.Option.class.getName());

    private HashMultimap<Object, Observer> observerLinkedObject = HashMultimap.create();
    private static final HashMultimap<Object, Preference> optionLinkedObject = HashMultimap.create();

    protected T value;

    protected Preference() {
    }

    protected Preference(T value) {
        this.value = value;
    }

    protected T val() {
        return value;
    }

    protected void val(T val) {
        LOGGER.log(Level.FINER, "Changed Value of Holder");
        this.value = val;
        this.setChanged();
        this.notifyObservers(val);
        this.clearChanged();
    }


    protected void addLikedObserver(Object link, Observer observer){
        LOGGER.log(Level.FINER, "Added Linked: " + link +  " Observer to Option");
        observerLinkedObject.put(link, observer);
        optionLinkedObject.put(link, this);
        this.addObserver(observer);
    }
    protected void removeAllObserversFromLink(Object link){
        LOGGER.log(Level.FINER, "Removed all Linked Observer from Option from link: " + link);
        Set<Observer> set = observerLinkedObject.get(link);
        for(Observer o: set){
            this.deleteObserver(o);
        }
        observerLinkedObject.removeAll(link);
    }

    protected void LinkJButton(Object link, AbstractButton but) {
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

    protected void LinkJSlider(Object link, JSlider slide) {
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

    protected void LinkJList(Object link, JList<T> list) {
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
