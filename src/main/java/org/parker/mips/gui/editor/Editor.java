/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.editor;

import java.io.File;

/**
 *
 * @author parke
 */
public abstract class Editor extends javax.swing.JToolBar {

    protected String displayName;
    protected File currentFile;
    protected boolean isSaved;

    public Editor(File file) {
        this.currentFile = file;
    }
    
    public String getDisplayName(){
        return displayName;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public abstract boolean save();

}
