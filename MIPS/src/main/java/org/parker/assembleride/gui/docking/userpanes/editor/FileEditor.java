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
import org.parker.assembleride.util.FileUtils;
import org.parker.assembleride.gui.docking.userpanes.UserPane;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
public abstract class FileEditor extends UserPane implements Editor{

    protected File currentFile;
    private boolean isSaved;
    private boolean isOpen;

    private static final Logger LOGGER = Logger.getLogger(FileEditor.class.getName());

    protected FileEditor(File file) {
        if(file == null){
            file = createTempFile();
        }
        assert  file.exists();
        assert  file.isFile();

        this.currentFile = file;
        isSaved = true;
        isOpen = true;
        EditorHandler.addEditor(this);
    }

    public final boolean isSaved() {
        return isSaved;
    }

    protected void setSaved(boolean val) {
        isSaved = val;
        setTitle(currentFile.getName() + (isSaved ? "" : " *"));
    }

    /**
     *
     * @return returns weather this editor has been closed or kept open
     */
    @Override
    public boolean close() {

        if (isSaved) {
            isOpen = false;
            return true;
        }
        int choice = BaseComputerArchitecture.createWarningQuestion(
                "Warning", currentFile.getName() + " is modified, would you like to save?");

        switch (choice) {
            case JOptionPane.YES_OPTION:
                if (save()) {
                    isOpen = false;
                    closeP();
                    return true;
                } else {
                    return false;
                }
            case JOptionPane.NO_OPTION:
                isOpen = false;
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

    protected File createTempFile(){
        try {
            return Files.createTempFile("hmm", "hmm").toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NotImplementedException();
        //return FileUtils.createTempFile();
    }

    public boolean save() {
        if (isSaved) {
            return true;
        }

        try {
            Files.write(currentFile.toPath(), getDataAsBytes());
            return false;
        }catch (Exception e){
            LOGGER.log(Level.WARNING, "Failed to save file: " + currentFile.getAbsolutePath() , e);
            return true;
        }
    }

    protected void closeP(){}

    public abstract byte[] getDataAsBytes();

}
