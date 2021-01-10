/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.editor;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class EditorHandler {

    private static final ArrayList<Editor> editors = new ArrayList();

    private static void AddEditor(Editor editor) {
        editors.add(editor);
    }

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

    public static void loadFileIntoEditor(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if(file.isDirectory()){
             File[] files = file.listFiles();
             for(File f: files){
                 if(f.exists() && f.isFile()){
                     createEditor(f);
                 }
             }
        }
        createEditor(file);
    }
    private static void createEditor(File file){
        
    }

}
