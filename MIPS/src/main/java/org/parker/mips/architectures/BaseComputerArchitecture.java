package org.parker.mips.architectures;

import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.MainGUI_2;
import org.parker.mips.gui.UserPaneTabbedPane;
import org.parker.mips.gui.userpanes.UserPane;
import org.parker.mips.gui.userpanes.editor.EditorHandler;
import org.parker.mips.gui.userpanes.hexeditor.MemoryEditorUserPane;
import org.parker.mips.preferences.Preferences;
import org.parker.mips.util.FileUtils;
import org.parker.mips.util.Memory;

import javax.annotation.CheckForNull;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class BaseComputerArchitecture implements ComputerArchitecture{

    private static JFrame frameInstance;
    private static ProjectInformation projectInformation;
    private static final Logger LOGGER = Logger.getLogger(BaseComputerArchitecture.class.getName() + ".MIPS");

    @Override
    public final JFrame createGUI() {
        frameInstance = new MainGUI(this);//new MainGUI(this);
        return frameInstance;
    }

    public abstract void onAssembleButton(ActionEvent ae);
    public abstract void onStartButton(ActionEvent ae, boolean isSelected);
    public abstract void onStopButton(ActionEvent ae);
    public abstract void onSingleStepButton(ActionEvent ae);
    public abstract void onResetButton(ActionEvent ae);
    public abstract void onDisassembleButton(ActionEvent ae);

    public abstract Memory getProcessorMemory();
    public abstract UserPane getEmulatorStatePanel();

    /**
     * This method will request to close the program, if the request is successful the program will exti
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

    @org.jetbrains.annotations.NotNull
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

    protected abstract Memory assemble(File[] files);

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

    public static class ProjectInformation implements Serializable{
        public File projectDIR;

    }
}
