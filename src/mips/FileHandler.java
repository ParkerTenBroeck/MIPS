/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import GUI.ASM_GUI;
import GUI.Main_GUI;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author parke
 */
public class FileHandler {

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
            extention = file.getPath().split("\\.")[1];
            path = file.getPath().split("\\.")[0];
        } catch (Exception e) {
            Log.logError(file.getPath() + " is not a valid file");
            return false;
        }

        if (extention.equals("asm")) {
            currentMXNFile = new File(file.getPath().replace(".asm", ".mxn"));
            currentASMFile = file;

            try {
                if (!currentMXNFile.exists()) {
                    currentMXNFile.createNewFile();
                }
            } catch (Exception e) {
                logFileHandlerError("Failed to create MXN File while loading ASM File");
            }

        } else if (extention.equals("mxn")) {
            currentASMFile = new File(file.getPath().replace(".mxn", ".asm"));
            currentMXNFile = file;

            try {
                if (!currentASMFile.exists()) {
                    currentASMFile.createNewFile();
                }
            } catch (Exception e) {
                logFileHandlerError("Failed to create ASM File while loading ASM File");
            }

        } else if (extention.equals("mx")) {
            importMXFile(file);

        } else {
            logFileHandlerError(file.getPath() + "/n is not a valid file");
        }
        reloadAllFiles();
        return true;
    }

    public static void asmTextAreaChange() {
        isASMFileSaved = false;
    }

    public static boolean saveASMFileFromUserTextArea() {
        if (Main_GUI.isLinked()) {
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
            fc.setSelectedFile(new File("project_" + pd.listFiles().length + ".asm"));
            int returnVal = fc.showOpenDialog(Main_GUI.getFrame());

            File chosenFile = fc.getSelectedFile();

            if (chosenFile == null) {
                //currentASMFile = File.createTempFile("temp", "asm");
                //return writeUserTextAreaToASMFile();
                logFileHandlerError("The file you selected does not exist? cannot save");
                return false;
            }

            if (!chosenFile.getName().contains(".")) {
                chosenFile = new File(chosenFile.getAbsolutePath() + ".asm");
            } else if (chosenFile.getName().endsWith(".asm")) {
                chosenFile = new File(chosenFile.getAbsolutePath().split("\\.")[0] + ".asm");
            }

            if (chosenFile.exists()) {
                int i = Main_GUI.confirmBox("Warning", "This File Already Exists are you sure you want to overwrite it");

                if (i == 0) {
                    currentASMFile = chosenFile;
                    return writeUserTextAreaToASMFile();
                }
            } else {
                //System.out.println(chosenFile.getParent());
                if (chosenFile.getParent().equals(ResourceHandler.DEFAULT_PROJECTS_PATH)) {
                    File pf = new File(chosenFile.getAbsolutePath().split("\\.")[0]);
                    if (!pf.exists()) {
                        pf.mkdir();
                    }
                    chosenFile = new File(pf.getAbsolutePath() + "\\" + chosenFile.getName());
                    if (chosenFile.exists()) {
                        int i = Main_GUI.confirmBox("Warning", "This File Already Exists are you sure you want to overwrite it");

                        if (i == 0) {
                            currentASMFile = chosenFile;
                            return saveASMFileFromUserTextArea();
                            //isASMFileSaved = true;
                            //return true;
                        }
                    }

                }
                currentASMFile = chosenFile;
                return writeUserTextAreaToASMFile();

            }
        } catch (Exception e) {
            logFileHandlerError("There was an error while saving your file:" + e.getMessage());
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
            int choice = Main_GUI.confirmBox("Warning", "The File is not saved would you like to save");
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
            loadedMXNFile = loadFileAsByteArray(currentMXNFile);
        } else {
            loadedMXNFile = new byte[]{};
        }
        if (currentASMFile != null) {
            loadedASMFile = loadFileAsStringList(currentASMFile);
            currentLoadedASMFileAbsolutePath = currentASMFile.getAbsolutePath();
        } else {
            loadedASMFile = new ArrayList();
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
            return true;
        } catch (Exception e) {
            logFileHandlerError("unable to write ASM File:" + e.getMessage());
            return false;
        }
    }

    public static ArrayList<String> getLoadedASMFile() {
        if (loadedASMFile != null) {
            return (ArrayList<String>) loadedASMFile.clone();
        } else {
            return new ArrayList();
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
        try {
            return Files.readAllBytes(new File(path).toPath());
        } catch (Exception e) {
            Log.logError("Failed to load File:" + path);
            return new byte[]{};
        }
    }

    public static byte[] loadFileAsByteArray(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            if (file != null) {
                Log.logError("Failed to load File: " + file.getAbsoluteFile() + " " + e.getMessage());
            }
            return new byte[]{};
        }
    }

    public static ArrayList<String> loadFileAsStringList(File file) {
        try {
            return new ArrayList(Files.readAllLines(file.toPath()));
        } catch (Exception e) {
            if (file != null) {
                Log.logError("Failed to load File: " + file.getAbsoluteFile() + " " + e.getMessage());
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
        try {
            Files.write(currentMXNFile.toPath(), byteArray);
        } catch (Exception e) {
            logFileHandlerError("Cannot save MXN File" + e.getMessage());
        }
    }

    private static void importMXFile(File file) {

        currentASMFile = null;
        currentMXNFile = null;
        isASMFileSaved = true;
        currentLoadedASMFileAbsolutePath = null;
        loadedASMFile = new ArrayList();

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
            System.err.println(e);
        }
    }

    public static boolean openUserSelectedFile() {
        if (!newFile(false)) {
            return false;
        }
        final JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(Main_GUI.getFrame());

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

}
