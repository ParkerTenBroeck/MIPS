package org.parker.mips.architectures;

import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.UserPaneTabbedPane;
import org.parker.mips.gui.userpanes.hexeditor.MemoryEditorUserPane;
import org.parker.mips.util.Memory;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("unused")
public abstract class BaseComputerArchitecture implements ComputerArchitecture{

    private static JFrame frameInstance;

    @Override
    public final JFrame createGUI() {
        frameInstance = new MainGUI(this);
        return frameInstance;
    }

    public abstract void onAssembleButton(ActionEvent ae);
    public abstract void onStartButton(ActionEvent ae, boolean isSelected);
    public abstract void onStopButton(ActionEvent ae);
    public abstract void onSingleStepButton(ActionEvent ae);
    public abstract void onResetButton(ActionEvent ae);
    public abstract void onDisassembleButton(ActionEvent ae);

    public abstract Memory getProcessorMemory();

    @Override
    public void requestSystemExit() throws UnableToExitException {

    }

    public void onMemoryButton(ActionEvent ae){
        UserPaneTabbedPane.addEditor(new MemoryEditorUserPane(getProcessorMemory()));
    }

    protected final void addJMenu(JMenu toAdd){

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
