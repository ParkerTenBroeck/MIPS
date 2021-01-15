/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.editor;

import org.parker.mips.FileHandler;
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

/**
 *
 * @author parke
 */
public abstract class Editor extends javax.swing.JPanel {

    private JLabel title;
    protected File currentFile;
    private boolean isSaved;

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
        Editor editor = new FormattedTextEditor(file);
    }

    protected Editor() {
        this(null);
    }

    protected Editor(File file) {
        this.currentFile = file;
        isSaved = true;
        this.addFocusListener(new asd(this));
        EditorHandler.addEditor(this);
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

    public String getDisplayName() {
        if (currentFile != null) {
            return currentFile.getName() + (isSaved ? "" : " *");
        } else {
            return "untitled" + (isSaved ? "" : " *");
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

    public boolean close() {

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
                    return true;
                } else {
                    return false;
                }
            case JOptionPane.NO_OPTION:
                EditorHandler.removeEditor(this);
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
            //Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
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
        isSaved = FileHandler.saveByteArrayToFile(getDataAsBytes(), currentFile);
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
                isSaved = FileHandler.saveByteArrayToFile(getDataAsBytes(), currentFile);
                return isSaved;
            }
        }
        return false;
    }

    public abstract byte[] getDataAsBytes();

}
