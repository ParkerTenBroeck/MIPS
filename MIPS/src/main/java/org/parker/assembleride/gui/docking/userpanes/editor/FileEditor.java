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
package org.parker.assembleride.gui.docking.userpanes.editor;

import org.parker.assembleride.architecture.BaseComputerArchitecture;
import org.parker.assembleride.gui.MainGUI_old;
import org.parker.assembleride.util.FileUtils;
import org.parker.assembleride.util.ResourceHandler;
import org.parker.assembleride.gui.docking.UserPaneTabbedPane;
import org.parker.assembleride.gui.docking.userpanes.UserPane;
import org.parker.assembleride.preferences.Preference;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public abstract class FileEditor extends UserPane {

    protected File currentFile;
    private boolean isSaved;

    private static final Logger LOGGER = Logger.getLogger(FileEditor.class.getName());

    protected FileEditor(File file) {
        this.currentFile = file;
        isSaved = true;
        EditorHandler.addEditor(this);
    }



    public final void updateTitle() {
        if (currentFile != null) {
            setTitle(currentFile.getName() + (isSaved ? "" : " *"));
        } else {
            setTitle("untitled" + (isSaved ? "" : " *"));
        }
    }

    public final boolean isSaved() {
        return isSaved;
    }

    protected final void setSaved(boolean val) {
        isSaved = val;
        updateTitle();
    }

    @Override
    public final boolean close() {

        if (isSaved) {
            EditorHandler.removeEditor(this);
            return true;
        }
        int choice;
        if (currentFile != null) {
            choice = BaseComputerArchitecture.createWarningQuestion("Warning", currentFile.getName() + " is modified, would you like to save?");
        } else {
            choice = BaseComputerArchitecture.createWarningQuestion("Warning", "untitled" + " is modified, would you like to save?");
        }

        switch (choice) {
            case JOptionPane.YES_OPTION:
                if (save()) {
                    EditorHandler.removeEditor(this);
                    Preference.removeAllObserversLinkedToObject(this);
                    closeP();
                    return true;
                } else {
                    return false;
                }
            case JOptionPane.NO_OPTION:
                EditorHandler.removeEditor(this);
                Preference.removeAllObserversLinkedToObject(this);
                closeP();
                return true;
            case JOptionPane.CANCEL_OPTION:
                return false;
            default:
                return false;
        }
    }

    public final File getFile() {
        return currentFile;
    }

    public final File getFalseFile() {
        if (currentFile != null) {
            return currentFile;
        } else {
            File temp = createTempFile(getName(), ".asm");
            FileUtils.saveByteArrayToFileSafe(getDataAsBytes(), temp);
            return temp;
        }
    }

    protected final File createTempFile(String prefix, String suffix) {
        try {
            File file = Files.createTempFile(prefix, suffix).toFile();
            file.deleteOnExit();
            return file;
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

            UserPaneTabbedPane.setSelectedUserPane(this);
            EditorHandler.setLastFocused(this);
            JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
            int returnVal = fc.showOpenDialog(MainGUI_old.getFrame());

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
        UserPaneTabbedPane.setSelectedUserPane(this);
        EditorHandler.setLastFocused(this);
        JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI_old.getFrame());

        if (returnVal == JFileChooser.FILES_ONLY) {
            currentFile = fc.getSelectedFile();
            if (currentFile != null) {
                isSaved = FileUtils.saveByteArrayToFileSafe(getDataAsBytes(), currentFile);
                return isSaved;
            }
        }
        return false;
    }

    protected void closeP(){}

    public abstract byte[] getDataAsBytes();

}
