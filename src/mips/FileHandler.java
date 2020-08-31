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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import mips.processor.Memory;
import mips.processor.Processor;

/**
 *
 * @author parke
 */
public class FileHandler {

    private static File currentASMFile;
    private static File currentMXNFile;

    private static boolean isFileSaved = true;
    private static boolean isFileReadOnly = false;

    private static void importMXFile(File file) {

        currentASMFile = null;
        currentMXNFile = null;

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

            Memory.setMemory(tempBytes);
            saveMXNFile();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void fileChange() {
        isFileSaved = false;
    }

    public static void reloadFiles() {
        reloadMXNFile();
        reloadASMFile();
        Main_GUI.refreshAll();
    }

    public static void reloadMXNFile() {

        if (isFileReadOnly) {
            return;
        }

        if (currentMXNFile == null) {
            Memory.setMemory(null);
            return;
        }
        try {
            if (!currentMXNFile.getName().equals("temp.asm")) {
                File temp = new File(currentMXNFile.getPath().split("\\.")[0] + ".asm");
                if (temp.exists()) {
                    currentASMFile = temp;
                }
            } else {
                currentASMFile = null;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        try {
            Memory.setMemory(Files.readAllBytes(currentMXNFile.toPath()));
        } catch (Exception e) {
            Memory.setMemory(new byte[0]);
        }
    }

    private static void reloadASMFile() {

        if(isFileReadOnly){
            return;
        }
        
        if (currentASMFile == null) {
            ASM_GUI.setTextAreaFromList(null);
            return;
        }

        try {
            if (!currentASMFile.getName().equals("temp.mxn")) {
                File temp = new File(currentASMFile.getPath().split("\\.")[0] + ".mxn");

                if (temp.exists()) {
                    currentMXNFile = temp;
                }
            } else {
                currentMXNFile = null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            List<String> lines = Files.readAllLines(Paths.get(currentASMFile.getAbsolutePath()));
            ASM_GUI.setTextAreaFromList(lines);
        } catch (Exception e) {
            return;
        }
        Main_GUI.refreshAll();
    }

    public static void saveMXNFile() {
        if (isFileReadOnly && isFileSaved) {
            return;
        }
        try {
            if (currentASMFile != null && currentASMFile.exists()) {
                currentMXNFile = new File(currentASMFile.getPath().split("\\.")[0] + ".mxn");

                Files.write(currentMXNFile.toPath(), Memory.getMemory());
            } else {
                currentMXNFile = File.createTempFile("temp", ".mxn");

                Files.write(currentMXNFile.toPath(), Memory.getMemory());
            }
        } catch (Exception e) {

        }
    }

    public static boolean saveASMFile() {

        if (Main_GUI.isLinked()) {
            reloadASMFile();
            return true;
        }
        try {
            if (currentASMFile != null && currentASMFile.exists() && !isFileReadOnly) {
                writeToASMFile();
                isFileSaved = true;
                return true;
            } else {
                if(currentASMFile == null && isFileSaved){
                    return true;
                }else if(isFileReadOnly){
                     return true;
                }else {
                    return saveAsASMFile();
                }
               
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean isASMFileSaved() {
        return isFileSaved || Main_GUI.isLinked();
    }

    public static boolean saveAsASMFile() {
        try {
            File pd = new File(ResourceHandler.DEFAULT_PROJECTS_PATH);
            JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
            fc.setSelectedFile(new File("project_" +(pd.listFiles().length)+".asm"));
            int returnVal = fc.showOpenDialog(Main_GUI.getFrame());
            System.out.println(returnVal);
            File chosenFile = fc.getSelectedFile();

            if (chosenFile == null || returnVal != 0) {
                if(isFileReadOnly){
                    return false;
                }
                currentASMFile = File.createTempFile("temp", "asm");
                writeToASMFile();
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
                    isFileReadOnly = false;
                    writeToASMFile();
                    isFileSaved = true;
                    return true;
                }
            } else {
                System.out.println(chosenFile.getParent());
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
                            isFileReadOnly = false;
                            writeToASMFile();
                            isFileSaved = true;
                            return true;
                        }
                    }

                }
                currentASMFile = chosenFile;
                isFileReadOnly = false;
                writeToASMFile();
                isFileSaved = true;
                
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public static void writeToASMFile() {
        if (isFileReadOnly) {
            return;
        }
        currentASMFile.delete();

        try {
            FileWriter f2 = new FileWriter(currentASMFile, false);
            f2.write(ASM_GUI.getAllText());
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List getASMList() {
        try {
            return Files.readAllLines(Paths.get(currentASMFile.getAbsolutePath()));
        } catch (Exception e) {
            //Log.logError("No File Selected");
            return new ArrayList();
        }
    }

    public static String getASMFilePath() {
        try {
            return currentASMFile.getPath();
        } catch (Exception e) {
            //Log.logError("No File Selected");
            return "";
        }
    }

    public static void loadExampleFile(File file) {
        FileHandler.setFileReadOnly(true);

        String extention = null;
        String path = null;

        try {
            extention = file.getPath().split("\\.")[1];
            path = file.getPath().split("\\.")[0];
        } catch (Exception e) {

            Log.logError(file.getPath() + " is not a valid file");
            return;
        }

        if (extention.equals("asm")) {
            currentMXNFile = null;
            currentASMFile = file;

        } else if (extention.equals("mxn")) {
            currentASMFile = null;
            currentMXNFile = file;

        }
    }

    public static void loadFile(File file) {
        if (file.canWrite()) {
            FileHandler.setFileReadOnly(false);
        } else {
            FileHandler.setFileReadOnly(true);
        }
        if (file == null || !file.exists()) {
            return;
        }

        String extention = null;
        String path = null;
        try {
            extention = file.getPath().split("\\.")[1];
            path = file.getPath().split("\\.")[0];
        } catch (Exception e) {
            Log.logError(file.getPath() + " is not a valid file");
            return;
        }

        if (extention.equals("asm")) {
            currentMXNFile = null;
            currentASMFile = file;

        } else if (extention.equals("mxn")) {
            currentASMFile = null;
            currentMXNFile = file;

        } else if (extention.equals("mx")) {
            importMXFile(file);

        } else {

            Log.logError(file.getPath() + "/n is not a valid file");
        }
        reloadFiles();

        Processor.reset();
    }

    public static void openFilePopup() {
        final JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(Main_GUI.getFrame());

        if (fc.getSelectedFile() == null || !fc.getSelectedFile().exists()) {
            return;
        }
        loadFile(fc.getSelectedFile());

    }

    public static void setFileReadOnly(boolean state) {
        isFileReadOnly = state;
        isFileSaved = true;
    }

    public static void newFile() {
        if (isFileSaved) {
            currentASMFile = null;
            currentMXNFile = null;
            reloadFiles();
        } else {
            int choice = Main_GUI.confirmBox("Warning", "The File is not saved would you like to save");
            switch (choice) {
                case 0:
                    if (saveASMFile()) {
                        currentASMFile = null;
                        currentMXNFile = null;
                        reloadFiles();
                    }
                case 1:
                    currentASMFile = null;
                    currentMXNFile = null;
                    reloadFiles();

                default:
                    break;
            }
        }
    }

    public static byte[] loadFileAsByteArray(String path) throws IOException {
        return Files.readAllBytes(new File(path).toPath());
    }
}
