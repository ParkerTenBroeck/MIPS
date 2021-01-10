/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.parker.mips.gui.MainGUI;

/**
 *
 * @author parke
 */
public class FileHandler {

    public static final char FILE_SEPERATOR = File.separatorChar;
    public static final char EXTENSION_SEPARATOR = '.';
    //platform spesific
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

//    private static String currentLoadedASMFileAbsolutePath;
//    private static File currentASMFile;
//    private static File currentMXNFile;
//
//    private static boolean isASMFileSaved = true;
//
//    private static ArrayList<String> loadedASMFile;
//    private static byte[] loadedMXNFile;
//    public static boolean isASMFileSaved() {
//        return isASMFileSaved;
//    }
//    public static boolean loadExampleFile(File file) {
//        if (!loadFile(file)) {
//            return false;
//        }
//        currentASMFile = null;
//        currentMXNFile = null;
//        return true;
//    }
//    public static boolean loadASMExampleFromStream(InputStream stream) {
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        ArrayList<String> temp = new ArrayList<String>();
//        try {
//            isr = new InputStreamReader(stream);
//            br = new BufferedReader(isr);
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                temp.add(line);
//            }
//
//            loadedASMFile = temp;
//            isASMFileSaved = true;
//            currentASMFile = null;
//            currentMXNFile = null;
//            currentLoadedASMFileAbsolutePath = null;
//            return true;
//        } catch (Exception e) {
//            logFileHandlerError("Failed to load stream:\n" + Log.getFullExceptionMessage(e));
//        } finally {
//            try {
//                isr.close();
//            } catch (Exception e) {
//
//            }
//            try {
//                br.close();
//            } catch (Exception e) {
//            }
//            try {
//                stream.close();
//            } catch (Exception e) {
//
//            }
//        }
//        return true;
//    }
//    public static boolean loadFile(File file) {
//        if (!newFile(false)) {
//            return false;
//        }
//        if (file == null || !file.exists()) {
//            return false;
//        }
//
//        String extention = null;
//        String path = null;
//        try {
//            extention = getExtension(file);
//            path = file.getAbsolutePath().replace(EXTENSION_SEPARATOR + extention, "");
//        } catch (Exception e) {
//            Log.logError(file.getPath() + " is not a valid file\n" + Log.getFullExceptionMessage(e));
//            return false;
//        }
//
//        if (extention.equals("asm")) {
//            currentMXNFile = new File(path + EXTENSION_SEPARATOR + "mxn");//new File(file.getPath().replace("\\.asm", "\\.mxn"));
//            currentASMFile = file;
//
//            try {
//                if (!currentMXNFile.exists()) {
//                    currentMXNFile.createNewFile();
//                }
//            } catch (Exception e) {
//                logFileHandlerError("Failed to create MXN File while loading ASM File:\n" + Log.getFullExceptionMessage(e));
//            }
//
//        } else if (extention.equals("mxn")) {
//            currentASMFile = new File(path + EXTENSION_SEPARATOR + "asm");//new File(file.getPath().replace("\\.mxn", "\\.asm"));;
//            currentMXNFile = file;
//
//            if (!currentASMFile.exists()) {
//                currentASMFile = null;
//            }
//        } else if (extention.equals("mx")) {
//            importMXFile(file);
//
//        } else if (extention.equals("txt")) { // text based
//
//            logFileHandlerWarning("Trying to load: " + file.getAbsolutePath() + " As an ASM File");
//
//            currentMXNFile = new File(path + EXTENSION_SEPARATOR + "mxn");//new File(file.getPath().replace("\\.asm", "\\.mxn"));
//            currentASMFile = file;
//
//            try {
//                if (!currentMXNFile.exists()) {
//                    currentMXNFile.createNewFile();
//                }
//            } catch (Exception e) {
//                logFileHandlerError("Failed to create MXN File while loading ASM File:\n" + Log.getFullExceptionMessage(e));
//            }
//
//        } else if (extention.equals("bin")) { //bin based
//
//            logFileHandlerWarning("Trying to load: " + file.getAbsolutePath() + " As an MXN File");
//
//            currentASMFile = null;
//            currentMXNFile = file;
//        } else {
//            logFileHandlerError(file.getPath() + "\n is not a valid file extention is not a supported file type: " + extention);
//        }
//        reloadAllFiles();
//        return true;
//    }
//    public static boolean saveAsASMFileFromUserTextArea() {
//        try {
//            File pd = new File(ResourceHandler.DEFAULT_PROJECTS_PATH);
//            JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
//            fc.setSelectedFile(new File("project_" + pd.listFiles().length));
//            int returnVal = fc.showOpenDialog(MainGUI.getFrame());
//
//            if (returnVal != 0) {
//                currentASMFile = File.createTempFile("ASMTemp", ".asm");
//                boolean temp = writeUserTextAreaToASMFile();
//                isASMFileSaved = false;
//                return temp;
//            }
//
//            File chosenFile = fc.getSelectedFile();
//
//            if (chosenFile == null) {
//                //currentASMFile = File.createTempFile("temp", "asm");
//                //return writeUserTextAreaToASMFile();
//                logFileHandlerError("The file you selected does not exist? cannot save");
//                return false;
//            }
//
//            if (getExtension(chosenFile.getPath()).equals("")) {
//                chosenFile = new File(chosenFile.getAbsolutePath() + EXTENSION_SEPARATOR + "asm");
//            }
//
//            if (chosenFile.exists()) {
//                int i = MainGUI.confirmBox("Warning", "This File Already Exists are you sure you want to overwrite it");
//
//                if (i == 0) {
//                    currentASMFile = chosenFile;
//                    currentMXNFile = new File(removeExtension(currentASMFile.getAbsolutePath()) + EXTENSION_SEPARATOR + "mxn");
//                    return writeUserTextAreaToASMFile();
//                }
//            } else {
//                if (chosenFile.getParent().equals(ResourceHandler.DEFAULT_PROJECTS_PATH)) {//this creates a new project folder
//
//                    File pf = new File(removeExtension(chosenFile.getAbsolutePath()));
//
//                    if (!pf.exists()) { // creates a folder in the project directory with the name given by the user
//                        pf.mkdir();
//                    }
//
//                    String tempName = pf.getAbsolutePath() + FileHandler.FILE_SEPERATOR + removeExtension(pf.getName()); //create a path to a file with no extention with the same name as the directory
//                    File generatedASMFile = new File(tempName + EXTENSION_SEPARATOR + "asm");
//                    File generatedMXNFile = new File(tempName + EXTENSION_SEPARATOR + "mxn");
//
//                    if (generatedASMFile.exists() || generatedMXNFile.exists()) {
//
//                        int i = -1;
//
//                        if (generatedASMFile.exists() && !generatedASMFile.exists()) {//only ASM file exists
//                            i = MainGUI.confirmBox("Warning", generatedASMFile.getName() + " Already Exists are you sure you want to overwrite it");
//                        } else if (!generatedASMFile.exists() && generatedASMFile.exists()) {//only MXN file exists
//                            i = MainGUI.confirmBox("Warning", generatedMXNFile.getName() + " Already Exists are you sure you want to overwrite it");
//                        } else if (generatedASMFile.exists() && generatedASMFile.exists()) {//both ASM and MXN files exist
//                            i = MainGUI.confirmBox("Warning", generatedASMFile.getName() + " and " + generatedMXNFile.getName() + " Already Exists are you sure you want to overwrite them");
//                        }
//
//                        if (i == 0) {
//                            currentASMFile = generatedASMFile;
//                            currentMXNFile = generatedMXNFile;
//                            return saveASMFileFromUserTextArea();
//                        }
//                        return false;
//                    }
//
//                }
//                currentASMFile = chosenFile;
//                currentMXNFile = new File(removeExtension(currentASMFile.getAbsolutePath()) + EXTENSION_SEPARATOR + "mxn");
//                return writeUserTextAreaToASMFile();
//
//            }
//        } catch (Exception e) {
//            logFileHandlerError("There was an error while saving your file:\n" + Log.getFullExceptionMessage(e));
//            return false;
//        }
//        return false;
//    }
//    public static boolean newFile(boolean reloadFiles) {
//        if (isASMFileSaved) {
//            isASMFileSaved = true;
//            currentASMFile = null;
//            currentMXNFile = null;
//            //if (reloadFiles) {
//            reloadAllFiles();
//            //}
//            return true;
//        } else {
//            int choice = MainGUI.confirmBox("Warning", "The File is not saved would you like to save");
//            switch (choice) {
//                case 0:
//                    if (saveASMFileFromUserTextArea()) {
//                        currentASMFile = null;
//                        currentMXNFile = null;
//                        //if (reloadFiles) {
//                        reloadAllFiles();
//                        //}
//                        return true;
//                    } else {
//                        return false;
//                    }
//                case 1:
//                    currentASMFile = null;
//                    currentMXNFile = null;
//                    //if (reloadFiles) {
//                    reloadAllFiles();
//                    //}
//                    return true;
//
//                default:
//                    break;
//            }
//        }
//        return false;
//    }
    public static boolean saveStringToFile(File file, String string) {
        if (file == null) {
            return true;
        }
        file.delete();

        try {
            FileWriter f2 = new FileWriter(file, false);
            f2.write(string);
            f2.close();
            return true;
        } catch (Exception e) {
            logFileHandlerError("unable to write ASM File: \n" + Log.getFullExceptionMessage(e));
            return false;
        }

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

    public static void saveByteArrayToFile(byte[] byteArray, File file) {
        if (file == null) {
            try {
                file = File.createTempFile("tempBIN", "bin");
            } catch (Exception e) {
                logFileHandlerError("Cannot create temp bin File \n" + Log.getFullExceptionMessage(e));
            }
        }
        try {
            if (!file.exists()) {
                logFileHandlerWarning("File does not exist, Creating new file");
                file.createNewFile();
            }
            Path path = file.toPath();
            Files.write(path, byteArray);
            logFileHandlerMessage("Saved file to: " + file.getAbsolutePath());
        } catch (Exception e) {
            logFileHandlerError("Cannot save File\n" + Log.getFullExceptionMessage(e));
        }
    }

    public static byte[] importMXFile(File file) {

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

            return tempBytes;

        } catch (IOException | NumberFormatException e) {
            logFileHandlerError("Failed to import MX File: \n" + Log.getFullExceptionMessage(e));
        }
        return new byte[0];
    }

    public static File openUserSelectedFile() {

        final JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());

        //System.out.println(returnVal);
        if (returnVal != 0) {
            return null;
        }

        if (fc.getSelectedFile() == null || !fc.getSelectedFile().exists()) {
            return null;
        }
        return fc.getSelectedFile();
    }

    public static byte[] readFileAsByteArray(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }

    public static ArrayList<String> loadFileAsStringList(File file) {
        try {
            Path path = file.toPath();
            List<String> list = Files.readAllLines(path);
            return new ArrayList(list);
        } catch (IOException e) {
            if (file != null) {
                logFileHandlerError("Failed to load Text File: " + file.getAbsoluteFile() + " \n" + Log.getFullExceptionMessage(e));
            } else {
                logFileHandlerError("Failed to load Text File: \n" + Log.getFullExceptionMessage(e));
            }
            return null;
        }
    }

    public static String loadFileAsString(File file) {
        try {
            Path path = file.toPath();
            byte[] bytes = Files.readAllBytes(path);
            return byteArrayToString(bytes);
        } catch (IOException e) {
            if (file != null) {
                logFileHandlerError("Failed to load Text File: " + file.getAbsoluteFile() + " \n" + Log.getFullExceptionMessage(e));
            } else {
                logFileHandlerError("Failed to load Text File: \n" + Log.getFullExceptionMessage(e));
            }
            return null;
        }
    }

    public static String byteArrayToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
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

    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? -1 : extensionPos;
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }
}
