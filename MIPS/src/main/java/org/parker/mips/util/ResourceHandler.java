/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.util;

import org.parker.mips.core.MIPS;
import org.parker.mips.gui.MainGUI;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parker.mips.util.FileUtils.FILE_SEPARATOR;

/**
 *
 * @author parke
 */
public class ResourceHandler {
    //public static final String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
    public static final String DEFAULT_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + FILE_SEPARATOR + "MIPS";

    public static final String DEFAULT_PROJECTS_PATH = DEFAULT_PATH + FILE_SEPARATOR + "Projects";
    public static final String DOCUMENTATION_PATH = DEFAULT_PATH + FILE_SEPARATOR + "Documentation";
    public static final String EXAMPLES_PATH = DEFAULT_PATH + FILE_SEPARATOR + "Examples";
    public static final String COMPILER_PATH = DEFAULT_PATH + FILE_SEPARATOR + "Compiler";
    public static final String SYS_CALLS_PLUGIN_PATH = DEFAULT_PATH + FILE_SEPARATOR + "SystemCallPlugins";

    //Config and related resources
    public static final String CONFIG_PATH = DEFAULT_PATH + FILE_SEPARATOR + "Config";
    public static final String USER_SAVED_CONFIG_PATH = CONFIG_PATH + FILE_SEPARATOR + "UserSavedConfig";
    public static final String DEFAULT_PERFERENCE_FILE = ResourceHandler.CONFIG_PATH + FILE_SEPARATOR + "Preferences.yml";

    //Themes and realted resources
    public static final String THEME_PATH = DEFAULT_PATH + FILE_SEPARATOR + "Themes";
    public static final String GUI_THEMES = THEME_PATH + FILE_SEPARATOR + "GUIThemes";
    public static final String EDITOR_THEMES = THEME_PATH + FILE_SEPARATOR + "EditorThemes";

    //Header and related resources
    public static final String STANDARD_HEADER_PATH = DEFAULT_PATH + FILE_SEPARATOR + "StandardHeaderFiles";
    public static final String SYS_CALL_DEF_HEADER_FILE = ResourceHandler.STANDARD_HEADER_PATH + FILE_SEPARATOR + "syscalldef.asm";
    public static final String REG_DEF_HEADER_FILE = ResourceHandler.STANDARD_HEADER_PATH + FILE_SEPARATOR + "regdef.asm";

    //Logging
    public static final String LOG_PATH = DEFAULT_PATH + FILE_SEPARATOR + "Log";
    public static final String LASTES_LOG = LOG_PATH + FILE_SEPARATOR + "latest.xml";

    private final static Logger LOGGER = Logger.getLogger(ResourceHandler.class.getName());

    public static boolean extractResources() {

        boolean temp = true;

        temp &= createDirectory(DEFAULT_PATH);
        temp &= createDirectory(DEFAULT_PROJECTS_PATH);
        temp &= createDirectory(DOCUMENTATION_PATH);
        temp &= createDirectory(EXAMPLES_PATH);
        temp &= createDirectory(LOG_PATH);

        //Config
        temp &= createDirectory(CONFIG_PATH);
        temp &= createDirectory(USER_SAVED_CONFIG_PATH);

        //Theme
        temp &= createDirectory(THEME_PATH);

        temp &= createDirectory(COMPILER_PATH);
        temp &= createDirectory(SYS_CALLS_PLUGIN_PATH);
        temp &= createDirectory(STANDARD_HEADER_PATH);

        temp &= extractResourceToFolder(DEFAULT_PATH, "Default");
        temp &= extractResourceToFolder(DOCUMENTATION_PATH, "Documentation");
        temp &= extractResourceToFolder(EXAMPLES_PATH, "Examples");
        temp &= extractResourceToFolder(STANDARD_HEADER_PATH, "StandardHeaderFiles");
        temp &= extractResourceToFolder(THEME_PATH, "Themes");

        if (temp) {
            LOGGER.log(Level.CONFIG, "Extracted all resourced with 0 errors\n");
        } else {
            LOGGER.log(Level.WARNING, "Extracted resources with errors\n");
        }
        return temp;
    }

    public static JFileChooser createFileChooser() {
        return new JFileChooser();
    }

    public static JFileChooser createFileChooser(String currentPath) {
        return new JFileChooser(currentPath);
    }

    public static int openFileChooser(JFileChooser fc) {
        return fc.showOpenDialog(MainGUI.getFrame());
    }

    private static boolean createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            LOGGER.log(Level.CONFIG, "Created Directory: " + path);
            return dir.mkdir();
        }
        return true;
    }

    private static boolean extractResourceToFolder(String destPath, String jarPath) {

        LOGGER.log(Level.CONFIG, "Started extraction of Resources: " + jarPath + " to Dest: " + destPath);

        String protocol = ResourceHandler.class.getResource("").getProtocol();
        if (Objects.equals(protocol, "jar")) { //run in jar

            try {
                //If folder exist, delete it.
                File temp = new File(destPath);
                if (!temp.exists()) {
                    temp.mkdir();
                }

                //System.out.println(destPath + " " + jarPath);
                JarFile jarFile = new JarFile(MIPS.JAR_PATH);
                Enumeration<JarEntry> enums = jarFile.entries();
                while (enums.hasMoreElements()) {
                    JarEntry entry = null;
                    try {
                        entry = enums.nextElement();
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE,"failed to get the next element", e);
                        //LogFrame.logError(LogFrame.getFullExceptionMessage(e));
                        continue;
                    }
                    if (entry.getName().startsWith(jarPath)) {
                        File toWrite = new File(destPath + FileUtils.FILE_SEPARATOR + entry.getName().replaceFirst(jarPath, ""));
                        //LogP.logMessage(toWrite.getAbsolutePath());
                        //System.out.println(toWrite.getAbsoluteFile() + " " + entry.getName());
                        if (entry.isDirectory()) {
                            if (!toWrite.exists()) {
                                toWrite.mkdirs();
                            }
                            continue;
                        }
                        if (toWrite.exists()) {
                            if (entry.getTime() > toWrite.lastModified()) { //if current entry was written later rewrite the file else continue

                            } else {
                                continue;
                            }
                        }
                        //System.out.println("bruh");
                        //LogP.logError(toWrite.getAbsolutePath());

                        InputStream in = new BufferedInputStream(jarFile.getInputStream(entry));
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(toWrite));
                        byte[] buffer = new byte[2048];
                        for (;;) {
                            int nBytes = in.read(buffer);
                            if (nBytes <= 0) {
                                break;
                            }
                            out.write(buffer, 0, nBytes);
                        }
                        out.flush();
                        out.close();
                        in.close();
                        toWrite.setLastModified(entry.getTime());//sets the last modified time

                        //System.out.println(entry.getName() + "wrote to " + toWrite.getAbsolutePath());
                    }
                    //LogP.logWarning(entry.getName());

                }
            } catch (Exception ex) {
                //System.out.println(ex);
                LOGGER.log(Level.FINEST,null, ex);
                //Logger.getLogger(ResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
                //LogP.logMessage("no");
                return false;

            }
            //LogP.logMessage("yes");
            return true;

        } else if (Objects.equals(protocol, "file")) { //run in ide
            //System.out.println(ResourceHandler.class.getResource("/" + jarPath).getFile());

            try {
                File source = new File("C:\\GitHub\\MIPS\\src\\main\\resources\\" + jarPath);
                File dest = new File(destPath);
                copyFolderReplaceOld(source, dest);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cannot copy resources to Documents folder. Wrong path defined?",e);
                return false;
            }
            return true;
        }
        return false;
    }

    public static void copyFolderReplaceOld(File source, File destination) {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            String files[] = source.list();

            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                copyFolderReplaceOld(srcFile, destFile);
            }
        } else {
            InputStream in = null;
            OutputStream out = null;

            if (destination.exists()) {
                if (source.lastModified() > destination.lastModified()) {

                } else {
                    return;
                }
            }
            LOGGER.log(Level.CONFIG, "ReWritting " + destination.getAbsolutePath());
            try {
                in = new FileInputStream(source);
                out = new FileOutputStream(destination);

                byte[] buffer = new byte[1024];

                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (Exception e) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception ex) {

                }

                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception ex) {

                }
                destination.setLastModified(source.lastModified());
            }
        }
    }

}
