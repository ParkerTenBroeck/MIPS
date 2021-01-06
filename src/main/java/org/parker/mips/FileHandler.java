/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import org.parker.mips.gui.ASM_GUI;
import org.parker.mips.gui.MainGUI;

/**
 *
 * @author parke
 */
public class FileHandler {

    public static final String FILE_SEPERATOR = File.separator;
    public static final String FILE_DOT = ".";

    private static String currentLoadedASMFileAbsolutePath;
    private static File currentASMFile;
    private static File currentMXNFile;

    private static boolean isASMFileSaved = true;

    private static ArrayList<String> loadedASMFile;
    private static byte[] loadedMXNFile;

    public static boolean isASMFileSaved() {
        return isASMFileSaved;
    }

    public static boolean loadExampleFile(File file) {
        if (!loadFile(file)) {
            return false;
        }
        currentASMFile = null;
        currentMXNFile = null;
        return true;
    }

    public static boolean loadASMExampleFromStream(InputStream stream) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        ArrayList<String> temp = new ArrayList<String>();
        try {
            isr = new InputStreamReader(stream);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                temp.add(line);
            }

            loadedASMFile = temp;
            isASMFileSaved = true;
            currentASMFile = null;
            currentMXNFile = null;
            currentLoadedASMFileAbsolutePath = null;
            return true;
        } catch (Exception e) {
            logFileHandlerError("Failed to load stream:\n" + Log.getFullExceptionMessage(e));
        } finally {
            try {
                isr.close();
            } catch (Exception e) {

            }
            try {
                br.close();
            } catch (Exception e) {
            }
            try {
                stream.close();
            } catch (Exception e) {

            }
        }
        return true;
    }

    /**
     * This will treat all resources as a ASM file
     *
     * @param url to the resource
     * @return
     */
    public boolean loadExampleFromJarPath(URL url) {

        if (true) {
            isASMFileSaved = true;
            currentASMFile = null;
            currentMXNFile = null;
        } else {

        }
        return true;
    }

    public static boolean loadFile(File file) {
        if (!newFile(false)) {
            return false;
        }
        if (file == null || !file.exists()) {
            return false;
        }

        String extention = null;
        String path = null;
        try {
            extention = getExtension(file);
            path = file.getAbsolutePath().replace(FILE_DOT + extention, "");
        } catch (Exception e) {
            Log.logError(file.getPath() + " is not a valid file\n" + Log.getFullExceptionMessage(e));
            return false;
        }

        if (extention.equals("asm")) {
            currentMXNFile = new File(path + FILE_DOT + "mxn");//new File(file.getPath().replace("\\.asm", "\\.mxn"));
            currentASMFile = file;

            try {
                if (!currentMXNFile.exists()) {
                    currentMXNFile.createNewFile();
                }
            } catch (Exception e) {
                logFileHandlerError("Failed to create MXN File while loading ASM File:\n" + Log.getFullExceptionMessage(e));
            }

        } else if (extention.equals("mxn")) {
            currentASMFile = new File(path + FILE_DOT + "asm");//new File(file.getPath().replace("\\.mxn", "\\.asm"));;
            currentMXNFile = file;

            if (!currentASMFile.exists()) {
                currentASMFile = null;
            }
        } else if (extention.equals("mx")) {
            importMXFile(file);

        } else if (extention.equals("txt")) { // text based

            logFileHandlerWarning("Trying to load: " + file.getAbsolutePath() + " As an ASM File");

            currentMXNFile = new File(path + FILE_DOT + "mxn");//new File(file.getPath().replace("\\.asm", "\\.mxn"));
            currentASMFile = file;

            try {
                if (!currentMXNFile.exists()) {
                    currentMXNFile.createNewFile();
                }
            } catch (Exception e) {
                logFileHandlerError("Failed to create MXN File while loading ASM File:\n" + Log.getFullExceptionMessage(e));
            }

        } else if (extention.equals("bin")) { //bin based

            logFileHandlerWarning("Trying to load: " + file.getAbsolutePath() + " As an MXN File");

            currentASMFile = null;
            currentMXNFile = file;
        } else {
            logFileHandlerError(file.getPath() + "\n is not a valid file extention is not a supported file type: " + extention);
        }
        reloadAllFiles();
        return true;
    }

    public static void asmTextAreaChange() {
        isASMFileSaved = false;
    }

    public static boolean saveASMFileFromUserTextArea() {
        if (MainGUI.isLinked()) {
            reloadAllFiles();
            return true;
        }
        if (isASMFileSaved) {
            return true;
        }
        if (currentASMFile != null && currentASMFile.exists()) {
            boolean temp = writeUserTextAreaToASMFile();
            isASMFileSaved = temp;
            return temp;
        } else {
            return saveAsASMFileFromUserTextArea();
        }
    }

    public static boolean saveAsASMFileFromUserTextArea() {
        try {
            File pd = new File(ResourceHandler.DEFAULT_PROJECTS_PATH);
            JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
            fc.setSelectedFile(new File("project_" + pd.listFiles().length + "\\.asm"));
            int returnVal = fc.showOpenDialog(MainGUI.getFrame());

            if (returnVal != 0) {
                currentASMFile = File.createTempFile("ASMTemp", "\\.asm");
                boolean temp = writeUserTextAreaToASMFile();
                isASMFileSaved = false;
                return temp;
            }

            File chosenFile = fc.getSelectedFile();

            if (chosenFile == null) {
                //currentASMFile = File.createTempFile("temp", "asm");
                //return writeUserTextAreaToASMFile();
                logFileHandlerError("The file you selected does not exist? cannot save");
                return false;
            }

            if (!chosenFile.getName().contains(".")) {
                chosenFile = new File(chosenFile.getAbsolutePath() + "\\.asm");
            } else if (chosenFile.getName().endsWith("\\.asm")) {
                chosenFile = new File(chosenFile.getAbsolutePath().split("\\.")[0] + "\\.asm");
            }

            if (chosenFile.exists()) {
                int i = MainGUI.confirmBox("Warning", "This File Already Exists are you sure you want to overwrite it");

                if (i == 0) {
                    currentASMFile = chosenFile;
                    currentMXNFile = new File(currentASMFile.getAbsolutePath().replaceAll("\\.asm", "\\.mxn"));
                    return writeUserTextAreaToASMFile();
                }
            } else {
                //System.out.println(chosenFile.getParent());
                if (chosenFile.getParent().equals(ResourceHandler.DEFAULT_PROJECTS_PATH)) {
                    File pf = new File(chosenFile.getAbsolutePath().split("\\.")[0]);
                    if (!pf.exists()) {
                        pf.mkdir();
                    }
                    chosenFile = new File(pf.getAbsolutePath() + FileHandler.FILE_SEPERATOR + chosenFile.getName());
                    if (chosenFile.exists()) {
                        int i = MainGUI.confirmBox("Warning", "This File Already Exists are you sure you want to overwrite it");

                        if (i == 0) {
                            currentASMFile = chosenFile;
                            currentMXNFile = new File(currentASMFile.getAbsolutePath().replaceAll("\\.asm", "\\.mxn"));
                            return saveASMFileFromUserTextArea();
                            //isASMFileSaved = true;
                            //return true;
                        }
                    }

                }
                currentASMFile = chosenFile;
                currentMXNFile = new File(currentASMFile.getAbsolutePath().replaceAll("\\.asm", "\\.mxn"));
                return writeUserTextAreaToASMFile();

            }
        } catch (Exception e) {
            logFileHandlerError("There was an error while saving your file:\n" + Log.getFullExceptionMessage(e));
            return false;
        }
        return false;
    }

    public static boolean newFile(boolean reloadFiles) {
        if (isASMFileSaved) {
            isASMFileSaved = true;
            currentASMFile = null;
            currentMXNFile = null;
            //if (reloadFiles) {
            reloadAllFiles();
            //}
            return true;
        } else {
            int choice = MainGUI.confirmBox("Warning", "The File is not saved would you like to save");
            switch (choice) {
                case 0:
                    if (saveASMFileFromUserTextArea()) {
                        currentASMFile = null;
                        currentMXNFile = null;
                        //if (reloadFiles) {
                        reloadAllFiles();
                        //}
                        return true;
                    } else {
                        return false;
                    }
                case 1:
                    currentASMFile = null;
                    currentMXNFile = null;
                    //if (reloadFiles) {
                    reloadAllFiles();
                    //}
                    return true;

                default:
                    break;
            }
        }
        return false;
    }

    private static void reloadAllFiles() {
        if (currentMXNFile != null) {
            if (!currentMXNFile.exists()) {
                try {
                    currentMXNFile.createNewFile();
                } catch (Exception e) {
                    logFileHandlerError("Failed to create MXN file at: " + currentMXNFile.getAbsolutePath() + "\n" + Log.getFullExceptionMessage(e));
                }
            }
            loadedMXNFile = loadFileAsByteArray(currentMXNFile);

        } else {
            loadedMXNFile = new byte[]{};
        }
        if (currentASMFile != null) {
            loadedASMFile = loadFileAsStringList(currentASMFile);
            currentLoadedASMFileAbsolutePath = currentASMFile.getAbsolutePath();
        } else {
            loadedASMFile = new ArrayList<String>();
            currentLoadedASMFileAbsolutePath = "";
        }
        isASMFileSaved = true;
    }

    public static boolean writeUserTextAreaToASMFile() {
        if (currentASMFile == null) {
            return true;
        }
        currentASMFile.delete();

        try {
            FileWriter f2 = new FileWriter(currentASMFile, false);
            f2.write(ASM_GUI.getAllText());
            f2.close();
            currentLoadedASMFileAbsolutePath = currentASMFile.getAbsolutePath();
            reloadAllFiles();
            return true;
        } catch (Exception e) {
            logFileHandlerError("unable to write ASM File: \n" + Log.getFullExceptionMessage(e));
            return false;
        }

    }

    public static ArrayList<String> getLoadedASMFile() {
        if (loadedASMFile != null) {
            return (ArrayList<String>) loadedASMFile.clone();
        } else {
            return new ArrayList<String>();
        }
    }

    public static String getASMFilePath() {
        if (currentASMFile != null) {
            return currentASMFile.getAbsolutePath();
        } else {
            return currentLoadedASMFileAbsolutePath;
        }
    }

    public static byte[] loadFileAsByteArray(String path) {
        return loadFileAsByteArray(new File(path));
    }

    public static byte[] loadFileAsByteArray(File file) {
        try {
            Path path = file.toPath();
            return Files.readAllBytes(path);
        } catch (Exception e) {
            if (file != null) {
                logFileHandlerError("Failed to load Binary File: " + file.getAbsoluteFile() + " \n" + Log.getFullExceptionMessage(e));
            } else {
                logFileHandlerError("Failed to load Binary File: \n" + Log.getFullExceptionMessage(e));
            }
            return new byte[]{};
        }
    }

    public static ArrayList<String> loadFileAsStringList(File file) {
        try {
            Path path = file.toPath();
            List<String> list = Files.readAllLines(path);
            return new ArrayList(list);
        } catch (Exception e) {
            if (file != null) {
                logFileHandlerError("Failed to load Text File: " + file.getAbsoluteFile() + " \n" + Log.getFullExceptionMessage(e));
            } else {
                logFileHandlerError("Failed to load Text File: \n" + Log.getFullExceptionMessage(e));
            }
            return null;
        }
    }

    public static byte[] getLoadedMXNFile() {
        if (loadedMXNFile != null) {
            return loadedMXNFile.clone();
        } else {
            return new byte[]{};
        }
    }

    public static void saveByteArrayToMXNFile(byte[] byteArray) {
        if (currentMXNFile == null) {
            try {
                currentMXNFile = File.createTempFile("tempMXN", "mxn");
            } catch (Exception e) {
                logFileHandlerError("Cannot create temp MXN File \n" + Log.getFullExceptionMessage(e));
            }
        }
        try {
            Files.write(currentMXNFile.toPath(), byteArray);
            loadedMXNFile = byteArray;
            logFileHandlerMessage("Saved MXN file to: " + currentMXNFile.getAbsolutePath());
        } catch (Exception e) {
            logFileHandlerError("Cannot save MXN File\n" + Log.getFullExceptionMessage(e));
        }
    }

    private static void importMXFile(File file) {

        currentASMFile = null;
        currentMXNFile = null;
        isASMFileSaved = true;
        currentLoadedASMFileAbsolutePath = null;
        loadedASMFile = new ArrayList<String>();

        try {
            List<String> allLines = Files.readAllLines(file.toPath());
            byte[] tempBytes = new byte[allLines.size() * 4];
            for (int i = 0; i < allLines.size(); i++) {

                int num = Integer.parseUnsignedInt(allLines.get(i), 2);

                tempBytes[(i * 4)] = (byte) (num >> 24);
                tempBytes[(i * 4) + 1] = (byte) (num >> 16);
                tempBytes[(i * 4) + 2] = (byte) (num >> 8);
                tempBytes[(i * 4) + 3] = (byte) num;

            }

            loadedMXNFile = tempBytes;

        } catch (Exception e) {
            logFileHandlerError("Failed to import MX File: \n" + Log.getFullExceptionMessage(e));
        }
    }

    public static boolean openUserSelectedFile() {
        if (!newFile(false)) {
            return false;
        }
        final JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());

        //System.out.println(returnVal);
        if (returnVal != 0) {
            return false;
        }

        if (fc.getSelectedFile() == null || !fc.getSelectedFile().exists()) {
            return false;
        }
        return loadFile(fc.getSelectedFile());
    }

    private static void logFileHandlerError(String message) {
        Log.logError("[FileHandler] " + message);
    }

    private static void logFileHandlerWarning(String message) {
        Log.logWarning("[FileHandler] " + message);
    }

    private static void logFileHandlerMessage(String message) {
        Log.logMessage("[FileHandler] " + message);
    }

    public static String getExtension(String fileName) {
        char ch;
        int len;
        if (fileName == null
                || (len = fileName.length()) == 0
                || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\'
                || //in the case of a directory
                ch == '.') //in the case of . or ..
        {
            return "";
        }
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if (dotInd <= sepInd) {
            return "";
        } else {
            return fileName.substring(dotInd + 1).toLowerCase();
        }
    }

    public static String getExtension(File file) {
        return getExtension(file.getAbsolutePath());
    }
}
