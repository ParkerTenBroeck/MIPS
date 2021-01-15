/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.editor;

import org.parker.mips.gui.EditorTabbedPane;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class EditorHandler {

    private static final ArrayList<Editor> editors = new ArrayList();
    private static Editor lastFoucsed;

    public static boolean isAllSaved() {
        for (Editor e : editors) {
            if (!e.isSaved()) {
                return false;
            }
        }
        return true;
    }

    public static boolean saveAll() {
        boolean allSaved = true;

        for (Editor e : editors) {
            allSaved &= e.save();
        }
        return allSaved;
    }

    public static void saveAsAll() {
        for (Editor e : editors) {
            e.saveAs();
        }
    }

    public static boolean saveLastFocused() {
        if (lastFoucsed != null) {
            return lastFoucsed.save();
        } else {
            return false;
        }
    }

    public static void saveAsLastFocused() {
        if (lastFoucsed != null) {
            lastFoucsed.saveAs();
        }
    }

    public static void setLastFocused(Editor editor) {
        lastFoucsed = editor;
    }

    /**
     *
     * @return retuns must return a file that contains the data represented in
     * the editor
     */
    public static File getFalseFileFromLastFocused() {
        if (lastFoucsed != null) {
            return lastFoucsed.getFalseFile();
        }
        return null;
    }

    public static Editor getLastFocused() {
        return lastFoucsed;
    }

    public static final void addEditor(Editor editor) {
        if(editors.contains(editor)){
            return;
        }
        editors.add(editor);
        EditorTabbedPane.addEditor(editor);
        lastFoucsed = editor;
    }

    public static final void removeEditor(Editor editor) {
        editors.remove(editor);
        EditorTabbedPane.removeEditor(editor);
        if (lastFoucsed == editor) {
            lastFoucsed = null;
        }
    }

}
