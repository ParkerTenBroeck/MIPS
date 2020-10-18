/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author parke
 */
public class ResourceHandler {

    //public static final String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
    public static final String DEFAULT_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\MIPS";

    public static final String DEFAULT_PROJECTS_PATH = DEFAULT_PATH + "\\Projects";
    public static final String DOCUMENTATION_PATH = DEFAULT_PATH + "\\Documentation";
    public static final String EXAMPLES_PATH = DEFAULT_PATH + "\\Examples";
    public static final String CONFIG_PATH = DEFAULT_PATH + "\\Config";
    public static final String SYS_CALLS_PLUGIN_PATH = DEFAULT_PATH + "\\SystemCallPlugins";
    public static final String STANDARD_HEADER_PATH = DEFAULT_PATH + "\\StandardHeaderFiles";

    public static boolean extractResources() {

        boolean temp = true;

        temp &= createDirectory(DEFAULT_PATH);
        temp &= createDirectory(DEFAULT_PROJECTS_PATH);
        temp &= createDirectory(DOCUMENTATION_PATH);
        temp &= createDirectory(EXAMPLES_PATH);
        temp &= createDirectory(CONFIG_PATH);
        temp &= createDirectory(SYS_CALLS_PLUGIN_PATH);
        temp &= createDirectory(STANDARD_HEADER_PATH);

        temp &= extractResourceToFolder(DEFAULT_PATH, "Default");
        temp &= extractResourceToFolder(DOCUMENTATION_PATH, "Documentation");
        temp &= extractResourceToFolder(EXAMPLES_PATH, "Examples");
        temp &= extractResourceToFolder(STANDARD_HEADER_PATH, "StandardHeaderFiles");

        return temp;
    }

    private static boolean createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdir();
        }
        return true;
    }

    private static boolean extractResourceToFolder(String destPath, String jarPath) {

        String protocol = ResourceHandler.class.getResource("").getProtocol();
        if (Objects.equals(protocol, "jar")) { //run in jar

            try {
                //If folder exist, delete it.
                File temp = new File(destPath);
                if (!temp.exists()) {
                    temp.mkdir();
                }

                //Log.logMessage(destPath + " " + jarPath);
                JarFile jarFile = new JarFile(MIPS.JAR_PATH);
                Enumeration<JarEntry> enums = jarFile.entries();
                while (enums.hasMoreElements()) {
                    JarEntry entry = null;
                    try {
                        entry = enums.nextElement();
                    } catch (Exception e) {
                        continue;
                    }
                    if (entry.getName().startsWith(jarPath)) {
                        File toWrite = new File(destPath + "//" + entry.getName().replaceAll(jarPath, ""));
                        //Log.logMessage(toWrite.getAbsolutePath());
                        //System.out.println(toWrite.getAbsoluteFile());
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
                        //Log.logError(toWrite.getAbsolutePath());

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
                    //Log.logWarning(entry.getName());

                }
            } catch (Exception ex) {
                //System.out.println(ex);
                Log.logWarning(ex.toString());
                //Logger.getLogger(ResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
                //Log.logMessage("no");
                return false;

            }
            //Log.logMessage("yes");
            return true;

        } else if (Objects.equals(protocol, "file")) { //run in ide
            //System.out.println(ResourceHandler.class.getResource("/" + jarPath).getFile());

            try {
                File source = new File("C:\\GitHub\\MIPS\\src\\main\\resources\\" + jarPath);
                File dest = new File(destPath);
                copyFolderReplaceOld(source, dest);
            } catch (Exception e) {
                Log.logError("Cannot copy resources to Documents folder. Wrong path defined?");
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
            Log.logSystemMessage("ReWritting " + destination.getAbsolutePath());
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