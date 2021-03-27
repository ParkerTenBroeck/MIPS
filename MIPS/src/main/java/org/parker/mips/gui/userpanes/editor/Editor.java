/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.userpanes.editor;

import org.parker.mips.FileUtils;
import org.parker.mips.ResourceHandler;
import org.parker.mips.gui.UserPaneTabbedPane;
import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.userpanes.UserPane;
import org.parker.mips.gui.userpanes.editor.rsyntax.FormattedTextEditor;
import org.parker.mips.preferences.Preference;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public abstract class Editor extends UserPane {

    protected File currentFile;
    private boolean isSaved;

    private final static HashMap<String, Class<?>> defaultFileExtensionEditor = new HashMap<String, Class<?>>();
    private final static ArrayList<Object> availableEditors = new ArrayList<>();


    private static final Logger LOGGER = Logger.getLogger(Editor.class.getName());

    //these are just the system editors idc that its hard coded eat me
    static{
        Editor.registerEditorWithDefaultExtensions(FormattedTextEditor.class, new String[]{"txt", "asm"});
        //SwingUtilities.invokeLater(new UI$1(new String[]{}));
    }


    private static void registerEditor(Editor editor){
        EditorHandler.addEditor(editor);
    }

    /**
     *
     * @param file the file that the editor will "edit"
     */
    public static void createEditor(File file){
        String extension = FileUtils.getExtension(file);
        Class<?> clazz = defaultFileExtensionEditor.getOrDefault(extension, FormattedTextEditor.class);
       createEditor(file, clazz);
    }

    /** creates an editor with no association to a file
     *
     * @param data the data to be loaded into the editor, each editor will load the data as it sees fit
     * @param name the title of the editor, since there is no accociated file this name will be used until the user decides to save the information to a file
     * @parma extension the type of file the data would be stored in
     */
    public static void createEditor(byte[] data, String name, String extension){
        Class<?> clazz = defaultFileExtensionEditor.getOrDefault(extension, FormattedTextEditor.class);
        createEditor(data, name, clazz);
    }

    /**
     *
     * @param data
     * @param name the name of the
     * @param clazz the editor to use
     */
    public static void createEditor(byte[] data, String name, Class<?> clazz){
        try {
            Constructor constructor = clazz.getDeclaredConstructor(byte[].class, String.class);
            constructor.setAccessible(true);
            Editor editor = (Editor)constructor.newInstance(data, name);
            registerEditor(editor);

        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "Cannot create editor", e);
        }
    }

    public static void createEditor(File file, Class<?> clazz){
        if(EditorHandler.isFileOpen(file)){
            EditorHandler.switchCurrentViewToFile(file);
            return;
        }
        try {
            Constructor constructor = clazz.getDeclaredConstructor(File.class);
            constructor.setAccessible(true);
            Editor editor = (Editor)constructor.newInstance(file);
            registerEditor(editor);

        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "Cannot create editor", e);
        }
    }

    public static void createEditor(){
        Class<?> clazz = FormattedTextEditor.class;

        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Editor editor = (Editor)constructor.newInstance();
            EditorHandler.addEditor(editor);

        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "Cannot create editor", e);
        }
    }

    protected static void registerEditorWithDefaultExtensions(Class<?> clazz, String[] accociatedExctentions){
        for(String ext: accociatedExctentions){
            defaultFileExtensionEditor.put(ext, clazz);
        }
        //temp
       registerEditorWithoutDefaultExtensions(new Object());
    }

    protected static void registerEditorWithoutDefaultExtensions(Object object){
        if(availableEditors.contains(object)){
            return;
        }else {
            availableEditors.add(object);
        }
    }

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
                    createEditor(f);
                }
            }
        }
        createEditor(file);
    }

    private Editor() {
        this(null);
    }

    protected Editor(File file) {
        this.currentFile = file;
        isSaved = true;
        this.addFocusListener(new asd(this));
    }

    private class asd implements FocusListener {

        private final Editor parent;

        public asd(Editor editor) {
            parent = editor;
        }

        @Override
        public void focusGained(FocusEvent fe) {
            EditorHandler.setLastFocused(parent);
            updateTitle();
        }

        @Override
        public void focusLost(FocusEvent fe) {
        }
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
            choice = MainGUI.createWarningQuestion("Warning", currentFile.getName() + " is modified, would you like to save?");
        } else {
            choice = MainGUI.createWarningQuestion("Warning", "untitled" + " is modified, would you like to save?");
        }

        switch (choice) {
            case JOptionPane.YES_OPTION:
                if (save()) {
                    EditorHandler.removeEditor(this);
                    Preference.removeAllObserversLinkedToObject(this);
                    closeS();
                    return true;
                } else {
                    return false;
                }
            case JOptionPane.NO_OPTION:
                EditorHandler.removeEditor(this);
                Preference.removeAllObserversLinkedToObject(this);
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

    public File getFalseFile() {
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

            UserPaneTabbedPane.setSelectedTab(this);
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
        UserPaneTabbedPane.setSelectedTab(this);
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
