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
package org.parker.assembleride.gui.userpanes.editor;

import org.parker.assembleride.gui.UserPaneTabbedPane;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class EditorHandler {

    private static final ArrayList<FileEditor> editors = new ArrayList<FileEditor>();
    private static FileEditor lastFoucsed;

    public static boolean isAllSaved() {
        for (FileEditor e : editors) {
            if (!e.isSaved()) {
                return false;
            }
        }
        return true;
    }

    public static boolean saveAll() {
        boolean allSaved = true;

        for (FileEditor e : editors) {
            allSaved &= e.save();
        }
        return allSaved;
    }

    public static void saveAsAll() {
        for (FileEditor e : editors) {
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

    public static void setLastFocused(FileEditor editor) {
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

    public static FileEditor getLastFocused() {
        return lastFoucsed;
    }

    public static final void addEditor(FileEditor editor) {
        if(editors.contains(editor)){
            return;
        }
        editors.add(editor);
        UserPaneTabbedPane.addUserPane(editor);
        lastFoucsed = editor;
    }

    public static final boolean isFileOpen(File file){
        if(file != null) {
            String afp = file.getAbsolutePath();
            for (FileEditor e : editors) {
                if(e.getFile() != null) {
                    if (e.getFile().getAbsolutePath().equals(afp)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static final void removeEditor(FileEditor editor) {
        editors.remove(editor);
        UserPaneTabbedPane.removeUserPane(editor);
        if (lastFoucsed == editor) {
            lastFoucsed = null;
        }
    }

    public static void switchCurrentViewToFile(File file) {
        if(isFileOpen(file)){
            String afp = file.getAbsolutePath();
            for(FileEditor e: editors){
                if(e.getFile() != null){
                    if(e.getFile().getAbsolutePath().equals(afp)){
                        setLastFocused(e);
                        UserPaneTabbedPane.setSelectedUserPane(e);
                    }
                }
            }
        }
    }

    public static FileEditor getEditorFromFile(File file) {
        for(FileEditor editor: editors){
            if(editor.getFile() != null) {
                if (editor.getFile().equals(file)) {
                    return editor;
                }
            }
        }
        return null;
    }
}
