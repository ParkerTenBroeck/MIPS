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

import org.parker.assembleride.gui.ToolBar;
import org.parker.assembleride.gui.docking.UserPaneTabbedPane;

import java.io.File;
import java.util.*;

/**
 *
 * @author parke
 */
public class EditorHandler {

    private static final Map<File, FileEditor> availableEditors = new WeakHashMap<>();
    private static FileEditor lastFoucsed;

    public static boolean isAllSaved() {
        for (FileEditor e : availableEditors.values()) {
            if (!e.isSaved()) {
                return false;
            }
        }
        return true;
    }

    public static boolean saveAll() {
        boolean allSaved = true;

        for (FileEditor e : availableEditors.values()) {
            allSaved &= e.save();
        }
        return allSaved;
    }

    public static boolean saveLastFocused() {
        if (lastFoucsed != null) {
            return lastFoucsed.save();
        } else {
            return false;
        }
    }

    public static void setLastFocused(FileEditor editor) {
        lastFoucsed = editor;
    }

    public static FileEditor getLastFocused() {
        return lastFoucsed;
    }

    public static final void addEditor(FileEditor editor) {
        if(availableEditors.values().contains(editor)){
            return;
        }
        availableEditors.put(editor.getFile(), editor);
        UserPaneTabbedPane.addUserPane(editor);
        lastFoucsed = editor;
    }

    public static final boolean isFileOpen(File file){
        //if(file != null) {
        //    String afp = file.getAbsolutePath();
        //    for (FileEditor e : editors) {
        //        if(e.getFile() != null) {
        //            if (e.getFile().getAbsolutePath().equals(afp)) {
        //                return true;
        //            }
        //        }
        //    }
        //}
        if(availableEditors.containsKey(file)){
            return true;//availableEditors.get(file).isOpen();
        }
        return false;
    }

    @Deprecated
    public static final void removeEditor(FileEditor editor) {
        //editors.remove(editor);
        //UserPaneTabbedPane.removeUserPane(editor);
        //if (lastFoucsed == editor) {
        //   lastFoucsed = null;
        //}
    }

    public static void switchCurrentViewToFile(File file) {
        if(isFileOpen(file)){
            String afp = file.getAbsolutePath();
            for(FileEditor e: availableEditors.values()){
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
        for(FileEditor editor: availableEditors.values()){
            if(editor.getFile() != null) {
                if (editor.getFile().equals(file)) {
                    return editor;
                }
            }
        }
        return null;
    }
}
