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
package org.parker.mips.gui.userpanes;

import javax.swing.*;

public abstract class UserPane extends javax.swing.JPanel {

    private JLabel title = new JLabel();

    public UserPane(){
    }

    public abstract boolean close();

    /**
     * this is called whenever the UI requests to update the contents of the displayed panel
     * this method is only called when the component is visible
     * example of its use is updating the register values on the emulator state tab
     */
    public void update(){}

    protected final void setTitle(String title){
        this.title.setText(title);
    }
    public final String getTitle(){
        return this.title.getText();
    }
    public final JLabel getTitleLabel(){
        return this.title;
    }

}
