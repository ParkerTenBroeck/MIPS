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
package org.parker.assembleride.util;

import org.parker.assembleride.gui.MainGUI_old;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class FileUtils {

    public static final char FILE_SEPARATOR = File.separatorChar;
    public static final char EXTENSION_SEPARATOR = '.';

    private final static Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    public static byte[] loadFileAsByteArraySafe(File file) {
        try {
            Path path = file.toPath();
            return Files.readAllBytes(path);
        } catch (Exception e) {
            if (file != null) {
                LOGGER.log(Level.SEVERE,"Failed to load Binary File: " +((file!= null) ? "" : file.getAbsolutePath()), e);
            } else {
                LOGGER.log(Level.SEVERE,"Failed to load Binary File: " + ((file!= null) ? "" : file.getAbsolutePath()), e);
            }
            return new byte[]{};
        }
    }

    public static boolean saveByteArrayToFileSafe(byte[] byteArray, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            Path path = file.toPath();
            Files.write(path, byteArray);
            LOGGER.log(Level.FINE, "Saved file to: " + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot save File: " + ((file!= null) ? "" : file.getAbsolutePath()), e);
        }
        return false;
    }

    public static ArrayList<String> loadFileAsStringList(File file) {
        try {
            Path path = file.toPath();
            List<String> list = Files.readAllLines(path);
            ArrayList<String> returnVal = new ArrayList(list);
            LOGGER.log(Level.FINE, "Loaded File: " + file.getAbsolutePath());
            return returnVal;
        } catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Failed to load Text File: " + ((file!= null) ? "" : file.getAbsolutePath()),e);
            return null;
        }
    }

    public static String loadFileAsString(File file) {
        try {
            Path path = file.toPath();
            byte[] bytes = Files.readAllBytes(path);
            String returnVal = byteArrayToString(bytes);
            LOGGER.log(Level.FINE, "Loaded File: " + file.getAbsolutePath());
            return returnVal;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load Text File: " + ((file!= null) ? "" : file.getAbsolutePath()),e);
            return null;
        }
    }

    public static String byteArrayToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
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
        return filename.lastIndexOf(FILE_SEPARATOR);
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
        byte[] data = os.toByteArray();
        LOGGER.log(Level.FINE, "Loaded Stream");
        return data;
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to load stream", e);
		}
		return null;
	}

	public static File createTempSystemFile(){
        //return Files.createTempFile();
        throw new NotImplementedException();
    }
}
