package org.parker.mips.gui.components;

import com.formdev.flatlaf.ui.FlatCheckBoxUI;
import org.parker.mips.gui.icons.FlatPlusIcon;

import javax.swing.*;

public class FlatIconButton extends JCheckBox {

    private Class<?> iconClazz;

    public FlatIconButton(){
        iconClazz = FlatPlusIcon.class;
        updateUI();
    }
    public FlatIconButton(Class<?> iconClazz){
        this.iconClazz = iconClazz;
        updateUI();
    }

    @Override
    public void updateUI() {
        this.setUI(new FlatIconButtonUI());
    }

    private class FlatIconButtonUI extends FlatCheckBoxUI{
        @Override
        public void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            try {
                this.icon = (Icon)iconClazz.getConstructor().newInstance();
            }catch (Exception e){

            }
        }
    }

}


