/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

    public static boolean saveByteArrayToFile(byte[] byteArray, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            Path path = file.toPath();
            Files.write(path, byteArray);
            logFileHandlerMessage("Saved file to: " + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            logFileHandlerError("Cannot save File\n" + Log.getFullExceptionMessage(e));
        }
        return false;
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

        } catch (Exception e) {
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
        } catch (Exception ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0];
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

    public static String loadFileAsString(File file) {
        try {
            Path path = file.toPath();
            byte[] bytes = Files.readAllBytes(path);
            return byteArrayToString(bytes);
        } catch (Exception e) {
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

	public static byte[] loadStreamAsByteArray(InputStream stream) {
		try {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        byte[] buffer = new byte[1024];
        int len;
 
        // read bytes from the input stream and store them in buffer
        while ((len = stream.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }
 
        return os.toByteArray();
		}catch(Exception e) {
			logFileHandlerError("Failed to load stream:\n" + Log.getFullExceptionMessage(e));
		}
		return null;
	}
}
