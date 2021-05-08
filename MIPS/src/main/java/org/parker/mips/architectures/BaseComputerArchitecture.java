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
package org.parker.mips.architectures;

import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.UserPaneTabbedPane;
import org.parker.mips.gui.userpanes.UserPane;
import org.parker.mips.gui.userpanes.editor.EditorHandler;
import org.parker.mips.gui.userpanes.hexeditor.MemoryEditorUserPane;
import org.parker.mips.plugin.base.PluginBase;
import org.parker.mips.preferences.Preferences;
import org.parker.mips.util.ByteMemory;
import org.parker.mips.util.FileUtils;
import org.parker.mips.util.Memory;

import javax.annotation.CheckForNull;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class BaseComputerArchitecture extends PluginBase implements ComputerArchitecture{

    protected static final Preferences ROOT_ARC_PREFS;

    private static MainGUI frameInstance;
    private static ProjectInformation projectInformation;

    private static final Logger LOGGER = Logger.getLogger(BaseComputerArchitecture.class.getName() + ".MIPS");
    private static boolean isAssembling;

    static{
        ROOT_ARC_PREFS = Preferences.ROOT_NODE.getNode("system/architectures/" + ArchitecturePluginHandler.getDescription().NAME);
    }

    public static Preferences getRootArcPrefs(){
        return ROOT_ARC_PREFS;
    }

    @Override
    public final JFrame createGUI() {
        frameInstance = new MainGUI(this);
        return frameInstance;
    }

    public void onAssembleButton(ActionEvent ae) {
        if(!isAssembling) {
            isAssembling = true;
            frameInstance.setControlsEnabled(false);
            new Thread(() -> {
                try {
                    stopEmulator();
                    EditorHandler.saveAll();
                    setEmulatorMemory(assembleDefault());
                    resetEmulator();
                }catch (Exception e){
                    isAssembling = false;
                    SwingUtilities.invokeLater(() -> frameInstance.setControlsEnabled(true));
                    throw e;
                }
                isAssembling = false;
                SwingUtilities.invokeLater(() -> frameInstance.setControlsEnabled(true));
            }).start();
        }
    }

    public void onStartButton(ActionEvent ae, boolean isSelected) {
        if (isSelected) {
            startEmulator();
        } else {
            stopEmulator();
        }
    }

    public void onStopButton(ActionEvent ae) {
        stopEmulator();
    }

    public void onSingleStepButton(ActionEvent ae) {
        //if (!Emulator.isRunning()) {
            singeStepEmulator();
        //}
    }

    public void onResetButton(ActionEvent ae) {
        resetEmulator();
    }

    public void onDisassembleButton(ActionEvent ae) {
        disassembleCurrentMemory();
    }

    protected abstract Memory assemble(File[] files);
    public abstract void startEmulator();
    public abstract void stopEmulator();
    public abstract void singeStepEmulator();
    public abstract void resetEmulator();
    public abstract void disassembleCurrentMemory();


    public abstract Memory getProcessorMemory();
    public void setEmulatorMemory(byte[] data){ setEmulatorMemory(new ByteMemory(data)); }
    public abstract void setEmulatorMemory(Memory mem);

    public abstract UserPane getEmulatorStatePanel();

    /**
     * This method will request to close the program, if the request is successful the program will exit
     * else the method will be returned from
     */
    @Override
    public void requestSystemExit() {
        requestSystemExit(null);
    }

    @Override
    public void requestSystemExit(SystemClosingEvent sce) {
        if (!EditorHandler.isAllSaved()) {
            int confirm = BaseComputerArchitecture.createWarningQuestion("Exit Confirmation", "You have unsaved work would you like to save before continuing?");

            if (confirm == JOptionPane.CANCEL_OPTION) {
                return;
            }else if (confirm == JOptionPane.YES_OPTION) {
                if (!EditorHandler.saveAll()) {
                    return;
                }
                Preferences.savePreferencesToDefaultFile();
                if(sce != null) {
                    sce.onClose();
                }
                System.exit(0);
            }
            if (confirm == JOptionPane.NO_OPTION) {
                Preferences.savePreferencesToDefaultFile();
                if(sce != null) {
                    sce.onClose();
                }
                System.exit(0);
            }
        } else {
            Preferences.savePreferencesToDefaultFile();
            if(sce != null) {
                sce.onClose();
            }
            System.exit(0);
        }
    }

    public void onMemoryButton(ActionEvent ae){
        UserPaneTabbedPane.addUserPane(new MemoryEditorUserPane(getProcessorMemory()));
    }

    protected final void addJMenu(JMenu toAdd){

    }

    @CheckForNull
    protected final Memory assembleDefault(){
        if(projectInformation != null){
            return assemble((File[]) getProjectFilesToAssemble().toArray());
        }else{
            LOGGER.log(Level.WARNING, "No project is open to assemble a file open a project");
        }
        return null;
    }

    protected List<File> getProjectFilesToAssemble(){
        return getProjectFilesToAssembleHelper(projectInformation.projectDIR);
    }

    private List<File> getProjectFilesToAssembleHelper(File myRoot){
        List<File> files = new ArrayList<>();
        for(File file: myRoot.listFiles()){
            if(file.isFile()){
                switch(FileUtils.getExtension(file.getAbsolutePath()).toLowerCase()){
                    case "asm":
                    case "s":
                    case "h":
                        files.add(file);
                    default:
                        break;
                }
            }else if(file.isDirectory()){
                files.addAll(getProjectFilesToAssembleHelper(myRoot));
            }
        }
        return files;
    }

    //static
    //messages (ok)
    public static void createPlaneMessage(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    public static void createPlaneInfo(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    public static void createWarningMessage(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, null, null);
    }

    public static void createErrorMessage(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, null, null, null);
    }

    //choices (yes, no)
    public static int createPlaneChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    public static int createInfoChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    public static int createWarningChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
    }

    public static int createErrorChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
    }

    //Question (yes, no, cancel)
    public static int createPlaneQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    public static int createInfoQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    public static int createWarningQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
    }

    public static int createErrorQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
    }

    //custom
    public static int createCustomOptionDialog(String title, String message, int i, int ii) {
        return createCustomOptionDialog(title, message, i, ii, null, null, null);
    }

    public static int createCustomOptionDialog(String title, String message, int i, int ii, Icon icon) {
        return createCustomOptionDialog(title, message, i, ii, icon, null, null);
    }

    public static int createCustomOptionDialog(String title, String message, int i, int ii, Icon icon, Object[] objects, Object object) {
        return JOptionPane.showOptionDialog(frameInstance, message, title, i, ii, icon, objects, object);
    }
}
