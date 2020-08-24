/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static void checkPath() {
        File file = new File(DEFAULT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

    }

    public static boolean extractResources() {

        boolean temp = true;

        temp &= extractResourceToFolder(DOCUMENTATION_PATH, "documentation");
        temp &= extractResourceToFolder(EXAMPLES_PATH, "examples");
        File file = new File(DEFAULT_PROJECTS_PATH);
        if (!file.exists()) {
            temp &= file.mkdir();
        }

        return temp;
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
                JarFile jarFile = new JarFile(ResourceHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                Enumeration<JarEntry> enums = jarFile.entries();
                while (enums.hasMoreElements()) {
                    JarEntry entry = enums.nextElement();
                    if (entry.getName().startsWith(jarPath)) {
                        File toWrite = new File(destPath + "//" + entry.getName().replaceAll(jarPath, ""));

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
            } catch (IOException ex) {
                System.out.println(ex);
                //Log.logWarning(ex.toString());
//Logger.getLogger(Methods.class.getName()).log(Level.SEVERE, null, ex);
                //Log.logMessage("no");
                return false;

            }
            Log.logMessage("yes");
            return true;

        } else if (Objects.equals(protocol, "file")) { //run in ide
            //System.out.println(ResourceHandler.class.getResource("/" + jarPath).getFile());
            File source = new File("C:\\Users\\parke\\OneDrive\\Documents\\GitHub\\MIPS\\res\\" + jarPath);
            File dest = new File(destPath);
            copyFolderReplaceOld(source, dest);
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
            System.out.println("ReWritting " + destination.getAbsolutePath());
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
                }

                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                destination.setLastModified(source.lastModified());
            }
        }
    }
}
