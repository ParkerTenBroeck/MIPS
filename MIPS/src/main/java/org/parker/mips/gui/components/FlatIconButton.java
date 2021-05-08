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


