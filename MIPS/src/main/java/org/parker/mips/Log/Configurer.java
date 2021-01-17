package org.parker.mips.log;

import org.parker.mips.FileUtils;
import org.parker.mips.LogFrame;
import org.parker.mips.ResourceHandler;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.logging.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Configurer {

    public final static Logger LOGGER = Logger.getLogger(Configurer.class.getName());
    public final static Logger BASE_LOGGER = Logger.getLogger("org.parker.mips");

    private static boolean initt = false;
    public static void init(){
        if(initt) {
            return;
        }else {
            initt = true;
        }

        Logger java = Logger.getLogger("");
        java.setLevel(Level.OFF);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> BASE_LOGGER.log(Level.SEVERE, t + " Uncaught Excpetion at: ", e));

        {
            {
                FileOutputStream fos = null;
                ZipOutputStream zo = null;
                ZipEntry ze = null;
                try {
                    File file = new File(ResourceHandler.LASTES_LOG);
                    if (file.exists()) {
                        BasicFileAttributes view = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                        long time = view.lastModifiedTime().toMillis();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        String date = dateFormat.format(time);
                        String path = ResourceHandler.LOG_PATH + FileUtils.FILE_SEPARATOR + date + FileUtils.EXTENSION_SEPARATOR + "zip";
                        File zip = new File(path);
                        zip.createNewFile();
                        fos = new FileOutputStream(zip);
                        zo = new ZipOutputStream(fos);
                        ze = new ZipEntry(file.getName());
                        zo.putNextEntry(ze);
                        zo.write(Files.readAllBytes(file.toPath()));
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to Compress Last Log", e);
                }finally {
                    try{
                        zo.flush();
                        zo.closeEntry();
                        zo.close();
                    }catch(Exception e){

                    }
                    try{
                        fos.flush();
                        fos.close();
                    }catch(Exception e){

                    }
                }
            }

            try{
                File file = new File(ResourceHandler.LASTES_LOG);
                if(!file.exists()){
                    file.createNewFile();
                }
                Handler fh = new FileHandler(file.getAbsolutePath());
                fh.setLevel(Level.ALL);
                BASE_LOGGER.addHandler(fh);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to load Log File", e);
            }

            Handler ch = new ConsoleHandler();
            ch.setLevel(Level.ALL);
            BASE_LOGGER.addHandler(ch);

            Handler vh = new LogFrame.LogFrameHandler();
            vh.setLevel(Level.ALL);
            BASE_LOGGER.addHandler(vh);


            BASE_LOGGER.setLevel(Level.ALL);
        }


    }


}
