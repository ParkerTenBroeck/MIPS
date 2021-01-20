/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.editor;

import org.parker.mips.FileUtils;
import org.parker.mips.ResourceHandler;
import org.parker.mips.gui.EditorTabbedPane;
import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.editor.rsyntax.FormattedTextEditor;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public abstract class Editor extends javax.swing.JPanel {

    private JLabel title;
    protected File currentFile;
    private boolean isSaved;

    //Only used if file is null
    protected final String name;

    private static final Logger LOGGER = Logger.getLogger(Editor.class.getName());

    public static void loadFileIntoEditor(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.exists() && f.isFile()) {
                    createEditorFromFile(f);
                }
            }
        }
        createEditorFromFile(file);
    }

    private static void createEditorFromFile(File file) {
        new FormattedTextEditor(file, "");
    }

    protected Editor() {
        this(null, null);
    }

    protected Editor(File file, String name) {
        this.currentFile = file;
        if(file == null){
            if(name == null){
                this.name = "";
            }else {
                this.name = name;
            }
        }else{
            this.name = "";
        }
        isSaved = true;
        this.addFocusListener(new asd(this));
        EditorHandler.addEditor(this);
    }
    protected Editor(File file){
        this(file, null);
    }

    protected Editor(String name){
        this(null, name);
    }

    private class asd implements FocusListener {

        private final Editor parent;

        public asd(Editor editor) {
            parent = editor;
        }

        @Override
        public void focusGained(FocusEvent fe) {
            EditorHandler.setLastFocused(parent);
            updateDisplayTitle();
        }

        @Override
        public void focusLost(FocusEvent fe) {
        }
    }

    public void setTitleLable(JLabel lable) {
        this.title = lable;
        updateDisplayTitle();
    }

    public final String getDisplayName() {
        if (currentFile != null) {
            return currentFile.getName() + (isSaved ? "" : " *");
        } else {
            return (name.equals("") ? "untitled" : name) + (isSaved ? "" : " *");
        }
    }

    public final String getName() {
        if (currentFile != null) {
            return currentFile.getName();
        } else {
            return (name.equals("") ? "untitled" : name);
        }
    }

    protected void updateDisplayTitle() {
        title.setText(getDisplayName());
    }

    public final boolean isSaved() {
        return isSaved;
    }

    protected final void setSaved(boolean val) {
        isSaved = val;
    }

    public final boolean close() {

        if (isSaved) {
            EditorHandler.removeEditor(this);
            return true;
        }
        int choice;
        if (currentFile != null) {
            choice = MainGUI.createWarningQuestion("Warning", currentFile.getName() + " is modified, would you like to save?");
        } else {
            choice = MainGUI.createWarningQuestion("Warning", "untitled" + " is modified, would you like to save?");
        }

        switch (choice) {
            case JOptionPane.YES_OPTION:
                if (save()) {
                    EditorHandler.removeEditor(this);
                    closeS();
                    return true;
                } else {
                    return false;
                }
            case JOptionPane.NO_OPTION:
                EditorHandler.removeEditor(this);
                closeS();
                return true;
            case JOptionPane.CANCEL_OPTION:
                return false;
            default:
                return false;
        }
    }

    public final File getFile() {
        if (currentFile != null) {
            return currentFile;
        }
        return null;
    }

    public abstract File getFalseFile();

    protected final File createTempFile(String prefix, String suffix) {
        try {
            return Files.createTempFile(prefix, suffix).toFile();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create temporary file", ex);
        }
        return null;
    }

    public final boolean save() {
        if (isSaved) {
            return true;
        }

        if (currentFile == null) {

            EditorTabbedPane.setSelectedTab(this);
            EditorHandler.setLastFocused(this);
            JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
            int returnVal = fc.showOpenDialog(MainGUI.getFrame());

            if (returnVal == JFileChooser.FILES_ONLY) {
                currentFile = fc.getSelectedFile();
                if (currentFile == null) {
                    isSaved = false;
                    return false;
                }
            } else {
                isSaved = false;
                return false;
            }
        }
        if (currentFile.isDirectory()) {
            isSaved = false;
            return false;
        }
        isSaved = FileUtils.saveByteArrayToFileSafe(getDataAsBytes(), currentFile);
        return isSaved;

    }

    public final boolean saveAs() {
        EditorTabbedPane.setSelectedTab(this);
        EditorHandler.setLastFocused(this);
        JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());

        if (returnVal == JFileChooser.FILES_ONLY) {
            currentFile = fc.getSelectedFile();
            if (currentFile != null) {
                isSaved = FileUtils.saveByteArrayToFileSafe(getDataAsBytes(), currentFile);
                return isSaved;
            }
        }
        return false;
    }

    public void closeS(){};

    public abstract byte[] getDataAsBytes();

}
