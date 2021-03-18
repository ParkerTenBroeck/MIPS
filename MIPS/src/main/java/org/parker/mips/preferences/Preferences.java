package org.parker.mips.preferences;

import org.parker.mips.FileUtils;
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

public class Preferences {

    private final HashMap<String, Preference> PREFERENCES = new HashMap();
    private final HashMap<String, Preferences> NODES = new HashMap();

    private final static Logger LOGGER = Logger.getLogger(Preferences.class.getName());

    public static final Preferences ROOT_NODE = new Preferences();


    public static void readPreferencesFromDefaultFile() {
        if(new File(ResourceHandler.DEFAULT_PERFERENCE_FILE).exists()) {
            loadPreferencesFromFile(ResourceHandler.DEFAULT_PERFERENCE_FILE);
        }else{
            LOGGER.log(Level.WARNING, "Preferences file not found, default preferences will be used");
        }
    }

    public static void savePreferencesToDefaultFile() {
        savePreferencesToFile(ResourceHandler.DEFAULT_PERFERENCE_FILE);
    }

    public static void readPreferencesFromName(String name) {
        loadPreferencesFromFile(ResourceHandler.USER_SAVED_CONFIG_PATH + FileUtils.FILE_SEPARATOR + name + ".yml");
    }

    public static void savePreferencesToName(String name) {
        savePreferencesToFile(ResourceHandler.USER_SAVED_CONFIG_PATH + FileUtils.FILE_SEPARATOR + name + ".yml");
    }

    public static void savePreferencesToFile(String path){
        savePreferencesToFile(new File(path));
    }

    public static void loadPreferencesFromFile(String path){
        loadPreferencesFromFile(new File(path));
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

    public static void loadPreferencesFromFile(File file) {

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


    public static void savePreferencesToFile(File file) {

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

    //end of static stuff


    protected Map<String, Object> generateMap(){
        HashMap<String, Object> tempMap = new HashMap<>();

        for(Map.Entry<String, Preferences> n: NODES.entrySet()){
            tempMap.put(n.getKey(), n.getValue().generateMap());
        }


        for(Map.Entry<String, Preference> n: PREFERENCES.entrySet()){

            tempMap.put(n.getKey(), n.getValue().value);
        }

        return tempMap;
    }

    protected void importFromMap(Map<String, Object> map){

        for(Map.Entry<String, Object> n: map.entrySet()){
            if(n.getValue() instanceof Map){
                getNode(n.getKey()).importFromMap((Map<String, Object>) n.getValue());
            }else{
                if(!(n.getValue() instanceof Serializable) && n.getValue() != null){
                    LOGGER.log(Level.SEVERE, "Cannot import Preference that is not Serializable: " + n.getValue().getClass().getName() + " Skipping");
                    continue;
                }
                putPreference(n.getKey(), (Serializable) n.getValue());
            }
        }
    }
    public void putPreference(String key, Serializable object){
        if(PREFERENCES.containsKey(key)){
            PREFERENCES.get(key).val(object);
        }else{
            Preference pref = new Preference(object);
            PREFERENCES.put(key, pref);
        }
    }

    public Serializable getPreference(String key, Serializable defaultValue){
        return getRawPreference(key, defaultValue).val();
    }

    public Preference getRawPreference(String key, Serializable defaultValue){
        if(PREFERENCES.containsKey(key)){
            return PREFERENCES.get(key);
        }else{
            Preference pref = new Preference(defaultValue);
            PREFERENCES.put(key, pref);
            return pref;
        }
    }

    public Preferences getNode(String path) {
            if (path.equals(""))
                return this;
            if (path.equals("/"))
                return Preferences.ROOT_NODE;
            if (path.charAt(0) != '/')
                return getNode(new StringTokenizer(path, "/", true));
        // Absolute path.  Note that we've dropped our lock to avoid deadlock
        return Preferences.ROOT_NODE.getNode(new StringTokenizer(path.substring(1), "/", true));
    }

    /**
     * tokenizer contains <name> {'/' <name>}*
     */
    private Preferences getNode(StringTokenizer path) {
        String token = path.nextToken();
        if (token.equals("/"))  // Check for consecutive slashes
            throw new IllegalArgumentException("Consecutive slashes in path");

            Preferences child = NODES.get(token.toString());
            if (child == null) {
                //if (token.length() > MAX_NAME_LENGTH)
                //    throw new IllegalArgumentException(
                //            "Node name " + token + " too long");
                //child = childSpi(token);
                child = new Preferences();
                NODES.put(token.toString(), child);
                //if (child.newNode)
                //    enqueueNodeAddedEvent(child);
                //kidCache.put(token, child);
            }

            if (!path.hasMoreTokens()) {
                return child;
            }

            path.nextToken();  // Consume slash
            if (!path.hasMoreTokens())
                throw new IllegalArgumentException("Path ends with slash");
            return child.getNode(path);
    }

}