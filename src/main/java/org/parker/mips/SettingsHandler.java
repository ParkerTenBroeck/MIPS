/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author parke
 */
public class SettingsHandler {

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
    public static final Holder<Boolean> linkedFile = new Holder(false);

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

    public static void readOptionsFromDefaultFile() {
        readOptionsFromFile(ResourceHandler.DEFAULT_OPTIONS_FILE);
    }

    public static void readOptionsFromCustomFile(String name) {
        if (name != null) {
            name = name.split("\\.")[0];
        }
        readOptionsFromFile(ResourceHandler.USER_SAVED_CONFIG_PATH + "\\" + name + "\\.json");
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
        saveOptionsToFile(ResourceHandler.USER_SAVED_CONFIG_PATH + "\\" + name + "\\.json");
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

            Set<Map.Entry<String, JsonElement>> entries = jo.entrySet();//will return members of your object
            entries.forEach((entry) -> {

                try {
                    Field field = SettingsHandler.class.getDeclaredField(entry.getKey()); //gets the field from field name
                    Holder holder = (Holder) field.get(SettingsHandler.class); //gets the instance of the FINAL holder 

                    JsonElement je = jo.getAsJsonObject(entry.getKey()).get("value"); //gets the value stored in the Options file

                    if (je.isJsonPrimitive()) {
                        if (je.getAsJsonPrimitive().isBoolean()) {
                            holder.value = (Boolean) je.getAsBoolean();
                        }
                        if (je.getAsJsonPrimitive().isString()) {
                            holder.value = (String) je.getAsString();
                        }
                        if (je.getAsJsonPrimitive().isNumber()) {
                            holder.value = (Integer) je.getAsNumber().intValue();
                        }
                    }

                } catch (Exception e) {
                    logOptionsHandlerError("Failed to read Options file: " + e);
                }

            });
            logOptionsHandlerSystemMessage("Successfully loaded " + file.getName() + "\n\n");
        } catch (Exception e) {
            logOptionsHandlerError("Failed to read Options file: " + e);
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
            gson.toJson(new SettingsHandler(), writer);

            logOptionsHandlerSystemMessage("Successfully saved " + file.getName() + "\n\n");
        } catch (Exception e) {
            logOptionsHandlerError("Failed to write Options file: " + e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {

            }
        }
    }

    private static void logOptionsHandlerError(String message) {
        Log.logError("[Options Handler] " + message);
    }

    private static void logOptionsHandlerWarning(String message) {
        Log.logWarning("[Options Handler] " + message);
    }

    private static void logOptionsHandlerSystemMessage(String message) {
        Log.logSystemMessage("[Options Handler] " + message);
    }

}
