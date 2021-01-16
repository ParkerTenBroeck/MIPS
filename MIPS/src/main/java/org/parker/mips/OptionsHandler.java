/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import com.formdev.flatlaf.FlatDarkLaf;
import com.google.gson.*;
import org.parker.mips.gui.theme.IJThemeInfo;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class OptionsHandler {

    //General
    //logging
    public static final Holder<Boolean> logSystemMessages = new Holder(true);
    public static final Holder<Boolean> logMessages = new Holder(true);
    public static final Holder<Boolean> logWarnings = new Holder(true);
    public static final Holder<Boolean> logErrors = new Holder(true);

    //GUI options
    public static final Holder<Boolean> enableGUIAutoUpdateWhileRunning = new Holder(true);
    public static final Holder<Integer> GUIAutoUpdateRefreshTime = new Holder(100);

    //Compiler
    public static final Holder<Boolean> saveCleanedFile = new Holder(false);
    public static final Holder<Boolean> savePreProcessedFile = new Holder(false);
    public static final Holder<Boolean> saveCompilationInfo = new Holder(false);

    //PreProcessor
    public static final Holder<Boolean> includeRegDef = new Holder(true);
    public static final Holder<Boolean> includeSysCallDef = new Holder(true);

    //Processor
    //Run Time
    public static final Holder<Boolean> breakOnRunTimeError = new Holder(true);
    public static final Holder<Boolean> adaptiveMemory = new Holder(false);
    public static final Holder<Boolean> enableBreakPoints = new Holder(true);

    //Non RunTime
    public static final Holder<Boolean> reloadMemoryOnReset = new Holder(true);

    //System Calls
    public static final Holder<Boolean> logSystemCallMessages = new Holder(true);
    public static final Holder<Boolean> resetProcessorOnTrap0 = new Holder(false);

    //Theme Handler
    public static final Holder<IJThemeInfo> currentGUITheme = new Holder(new IJThemeInfo( "Flat Dark", null, true, null, null, null, null, null, FlatDarkLaf.class.getName() ));
    public static final Holder<String> currentEditorTheme = new Holder("Dark");
    public static final Holder<Font> currentGUIFont = new Holder(new Font("Segoe UI", 0, 15));
    public static final Holder<Font> currentEditorFont = new Holder(new Font("Segoe UI", 0, 15));

    private static final Logger LOGGER = Logger.getLogger(OptionsHandler.class.getName());

    public static void readOptionsFromDefaultFile() {
        readOptionsFromFile(ResourceHandler.DEFAULT_OPTIONS_FILE);
    }

    public static void readOptionsFromCustomFile(String name) {
        if (name != null) {
            name = name.split("\\.")[0];
        }
        readOptionsFromFile(ResourceHandler.USER_SAVED_CONFIG_PATH + FileUtils.FILE_SEPARATOR + name + "\\.json");
    }

    public static void readOptionsFromCustomFile(File file) {
        if (file != null) {
            readOptionsFromFile(file.getAbsolutePath());
        }
    }

    public static void saveOptionsToDefaultFile() {
        saveOptionsToFile(ResourceHandler.DEFAULT_OPTIONS_FILE);
    }

    public static void saveOptionsToCustomFile(String name) {
        if (name != null) {
            name = name.split("\\.")[0];
        }
        saveOptionsToFile(ResourceHandler.USER_SAVED_CONFIG_PATH + FileUtils.FILE_SEPARATOR + name + "\\.json");
    }

    public static void saveOptionsToCustomFile(File file) {
        if (file != null) {
            String path = file.getAbsolutePath();
            if (!path.contains("\\.")) {
                saveOptionsToFile(path + ".json");
            } else {
                saveOptionsToFile(path);
            }
        }
    }

    private static void readOptionsFromFile(String filePath) {

        File file = new File(filePath);
        FileReader reader = null;

        try {

            reader = new FileReader(file);

            JsonParser parser = new JsonParser();

            JsonObject jo = parser.parse(reader).getAsJsonObject();

            Gson gson = new Gson();

            Set<Map.Entry<String, JsonElement>> entries = jo.entrySet();//will return members of your object
            entries.forEach((entry) -> {

                try {
                    Field field = OptionsHandler.class.getDeclaredField(entry.getKey()); //gets the field from field name
                    Holder holder = (Holder) field.get(OptionsHandler.class); //gets the instance of the FINAL holder 

                    JsonElement tempJE = jo.getAsJsonObject(entry.getKey()).get("value");
                    Class<?> clazz = holder.val().getClass();
                    Object as = gson.fromJson(tempJE, clazz);
                    if (as != null) {
                        holder.val(as);
                    }

                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Field invalid " + entry.getKey() + " Ignoring and continuing", e);
                    //logOptionsHandlerError("Error Field invalid " + entry.getKey() + ":\n" + LogFrame.getFullExceptionMessage(e));
                }

            });
            LOGGER.log(Level.CONFIG, "Successfully loaded " + file.getName());
            //logOptionsHandlerSystemMessage("Successfully loaded " + file.getName() + "\n\n");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to read Options file", e);
            //logOptionsHandlerError("Failed to read Options file:\n" + LogFrame.getFullExceptionMessage(e));
        } finally {
            try {
                reader.close();
            } catch (Exception e) {

            }
        }

    }

    private static void saveOptionsToFile(String filePath) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Allowing the serialization of static fields    

        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.PRIVATE);
        gsonBuilder.setPrettyPrinting();

        //gsonBuilder.
        // Creates a Gson instance based on the current configuration
        Gson gson = gsonBuilder.create();
        Writer writer = null;
        File file = new File(filePath);
        try {
            //System.out.println("asdasdasd");
            writer = Files.newBufferedWriter(file.toPath());
            //System.err.println("asdasd");
            gson.toJson(new OptionsHandler(), writer);

            LOGGER.log(Level.INFO, "Successfully saved " + file.getName() );
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Failed to write Options file", e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {

            }
        }
    }

}
