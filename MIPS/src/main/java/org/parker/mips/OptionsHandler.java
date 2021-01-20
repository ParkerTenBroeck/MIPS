/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import com.formdev.flatlaf.FlatDarkLaf;
import com.google.common.collect.HashMultimap;
import com.google.gson.*;
import org.parker.mips.assembler.AssemblerLevel;
import org.parker.mips.gui.theme.IJThemeInfo;
import org.parker.mips.processor.RunTimeLevel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author parke
 */
public class OptionsHandler {

    private static HashMultimap<Object, Option> map = HashMultimap.create();

    //General

    //Virtual Log
    //System
    public static final Option<Boolean> showStackTrace = new Option(false);
    public static final Option<Boolean> showCallerClass = new Option(false);
    public static final Option<Boolean> showCallerMethod = new Option(false);
    public static final Option<String> systemLogLevel = new Option(Level.INFO.getName());
    //Assembler
    public static final Option<String> assemblerLogLevel = new Option(AssemblerLevel.COMPILATION_MESSAGE.getName());
    public static final Option<String> runtimeLogLevel = new Option(RunTimeLevel.RUN_TIME_MESSAGE.getName());

    //GUI options
    public static final Option<Boolean> enableGUIAutoUpdateWhileRunning = new Option(true);
    public static final Option<Integer> GUIAutoUpdateRefreshTime = new Option(100);

    //Compiler
    public static final Option<Boolean> saveCleanedFile = new Option(false);
    public static final Option<Boolean> savePreProcessedFile = new Option(false);
    public static final Option<Boolean> saveCompilationInfo = new Option(false);

    //PreProcessor
    public static final Option<Boolean> includeRegDef = new Option(true);
    public static final Option<Boolean> includeSysCallDef = new Option(true);

    //Processor
    //Run Time
    public static final Option<Boolean> breakOnRunTimeError = new Option(true);
    public static final Option<Boolean> adaptiveMemory = new Option(false);
    public static final Option<Boolean> checkMemoryAlignment = new Option(false);
    public static final Option<Boolean> enableBreakPoints = new Option(true);

    //Non RunTime
    public static final Option<Boolean> reloadMemoryOnReset = new Option(true);

    //System Calls
    public static final Option<Boolean> logSystemCallMessages = new Option(true);
    public static final Option<Boolean> resetProcessorOnTrap0 = new Option(false);

    //Theme Handler
    public static final Option<IJThemeInfo> currentGUITheme = new Option(new IJThemeInfo( "Flat Dark", null, true, null, null, null, null, null, FlatDarkLaf.class.getName() ));
    public static final Option<String> currentEditorTheme = new Option("Dark");
    public static final Option<Font> currentGUIFont = new Option(new Font("Segoe UI", 0, 15));
    public static final Option<Font> currentEditorFont = new Option(new Font("Segoe UI", 0, 15));

    private static final Logger LOGGER = Logger.getLogger(OptionsHandler.class.getName());

    public static void readOptionsFromDefaultFile() {
        readOptionsFromFile(ResourceHandler.DEFAULT_OPTIONS_FILE);
    }

    private static final HashMultimap<Object, Option> optionLinkedObject = HashMultimap.create();

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
                    Option option = (Option) field.get(OptionsHandler.class); //gets the instance of the FINAL holder

                    JsonElement tempJE = jo.getAsJsonObject(entry.getKey()).get("value");
                    Class<?> clazz = option.val().getClass();
                    Object as = gson.fromJson(tempJE, clazz);
                    if (as != null) {
                        option.val(as);
                    }

                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Field invalid " + entry.getKey() + " Ignoring and continuing", e);
                }

            });
            LOGGER.log(Level.CONFIG, "Successfully loaded " + file.getName() + "\n");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to read Options file\n", e);
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
            writer = Files.newBufferedWriter(file.toPath());
            gson.toJson(new OptionsHandler(), writer);

            LOGGER.log(Level.CONFIG, "Successfully saved " + file.getName() );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to write Options file", e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {

            }
        }
    }

    public static void removeAllObserversLinkedToObject(Object object) {
        LOGGER.log(Level.FINER, "Removing all linked observers from all options from link: " + object);
        for(Option o : optionLinkedObject.get(object)){
            o.removeAllObserversFromLink(object);
        }
        optionLinkedObject.removeAll(object);
    }

    /**
     *
     * @author parke
     * @param <T>
     */
    public static class Option<T> extends Observable {

        private static final Logger LOGGER = Logger.getLogger(Option.class.getName());

        private HashMultimap<Object, Observer> observerLinkedObject = HashMultimap.create();

        protected T value;

        public Option() {
        }

        public Option(T value) {
            this.value = value;
        }

        public T val() {
            return value;
        }

        public void val(T val) {
            LOGGER.log(Level.FINER, "Changed Value of Holder");
            this.value = val;
            this.setChanged();
            this.notifyObservers(val);
            this.clearChanged();
        }

        public void addLikedObserver(Object link, Observer observer){
            LOGGER.log(Level.FINER, "Added Linked: " + link +  " Observer to Option");
            observerLinkedObject.put(link, observer);
            optionLinkedObject.put(link, this);
            this.addObserver(observer);
        }
        public void removeAllObserversFromLink(Object link){
            LOGGER.log(Level.FINER, "Removed all Linked Observer from Option from link: " + link);
            Set<Observer> set = observerLinkedObject.get(link);
            for(Observer o: set){
                this.deleteObserver(o);
            }
            observerLinkedObject.removeAll(link);
        }

        public void LinkJButton(Object link, AbstractButton but) {
            but.setSelected((Boolean) value);
            but.addActionListener((ae) -> {
                if (but.isSelected() != (Boolean) value) {
                    this.val((T) (Boolean) (but.isSelected()));
                }
            });
            this.addLikedObserver(link, (o, v) -> {
                if (but.isSelected() != (Boolean) v) {
                    but.setSelected((Boolean) v);
                }
            });
        }

        public void LinkJSlider(Object link, JSlider slide) {
            slide.setValue((Integer) value);
            slide.addChangeListener((ex) ->{
                    if (slide.getValue() != (Integer) value) {
                        val((T) (Integer) (slide.getValue()));
                    }
                });

            this.addLikedObserver(link, (o,v) -> {
                if (slide.getValue() != (Integer) v) {
                    slide.setValue((Integer) v);
                }
            });
        }

    }
}
