package org.parker.mips.preferences;

import org.parker.mips.FileUtils;
import org.parker.mips.OptionsHandler;
import org.parker.mips.ResourceHandler;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class GlobalPreferences {

    private static final PreferencesNode ROOT_NODE = new PreferencesNode();

    public static void main(String[] args){
        readOptionsFromFile("C:/Users/parke/OneDrive/Documents/MIPS/Config/Preferences.yml");
        saveOptionsToFile("C:/Users/parke/OneDrive/Documents/MIPS/Config/Preferences2.yml");

    }

    private static final Logger LOGGER = Logger.getLogger(OptionsHandler.class.getName());

    public static void readOptionsFromDefaultFile() {
        readOptionsFromFile(ResourceHandler.DEFAULT_OPTIONS_FILE);
    }

    public static void saveOptionsToDefaultFile() {
        saveOptionsToFile(ResourceHandler.DEFAULT_OPTIONS_FILE);
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

    public static void saveOptionsToCustomFile(String name) {
        //if (name != null) {
        //    name = name.split("\\.")[0];
        //}
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

    private static Yaml generateYaml(){
        Representer r = new Representer(){
            {
                representers.put(String.class, new RepresentQuotedString());
            }
            class RepresentQuotedString implements Represent {
                public Node representData(Object data) {
                    String str = (String) data;
                    return representScalar(Tag.STR, str, '\'');
                }
            }
        };
        r.getPropertyUtils().setBeanAccess(BeanAccess.FIELD);
        r.getPropertyUtils().setAllowReadOnlyProperties(true);
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(r, options);
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml;
    }

    private static void readOptionsFromFile(String filePath) {

        File file = new File(filePath);

        Yaml yaml = generateYaml();

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            yaml.setBeanAccess(BeanAccess.FIELD);
            Map<String, Object> tempMap = (Map<String, Object>) yaml.load(in);

            ROOT_NODE.importFromMap(tempMap);

        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "Failed to load/import preferences: ", e);
        }finally{
            try{
                in.close();
            }catch (Exception e){

            }
        }

    }

    private void loadOptionsFromMap(Map<String, Object> map){

    }


    private static void saveOptionsToFile(String filePath) {

        File file = new File(filePath);

        //GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setPrettyPrinting();
        //gsonBuilder.generateNonExecutableJson();
        //Gson gson = gsonBuilder.create();
        Yaml yaml = generateYaml();

        Writer out = null;
        try {
            out = new FileWriter(file);

            Map<String, Object> why = ROOT_NODE.generateMap();

            //gson.toJson(why, out);
            yaml.dump(why, out);

        }catch(Exception e){

        }finally{
            try{
                out.flush();
            }catch (Exception e){

            }
            try{
                out.close();
            }catch (Exception e){

            }
        }
    }



}

