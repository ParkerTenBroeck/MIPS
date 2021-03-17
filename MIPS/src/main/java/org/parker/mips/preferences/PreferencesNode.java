package org.parker.mips.preferences;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreferencesNode {

    private final HashMap<String, Preference> PREFERENCES = new HashMap();
    private final HashMap<String, PreferencesNode> NODES = new HashMap();

    private final Logger LOGGER = Logger.getLogger(PreferencesNode.class.getName());


protected Map<String, Object> generateMap(){
    HashMap<String, Object> tempMap = new HashMap<>();

    for(Map.Entry<String, PreferencesNode> n: NODES.entrySet()){
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
protected void putPreference(String key, Serializable object){
    if(PREFERENCES.containsKey(key)){
        PREFERENCES.get(key).val(object);
    }else{
        Preference pref = new Preference(object);
        PREFERENCES.put(key, pref);
    }
}

protected Preference getPreference(String key, Serializable defaultValue){
    if(PREFERENCES.containsKey(key)){
        return PREFERENCES.get(key);
    }else{
        Preference pref = new Preference(defaultValue);
        PREFERENCES.put(key, pref);
        return pref;
    }
}

protected PreferencesNode getNode(String key){
    if(NODES.containsKey(key)){
        return NODES.get(key);
    }else{
        PreferencesNode node = new PreferencesNode();
        NODES.put(key, node);
        return node;
    }
}

protected PreferencesNode putNode(String key, PreferencesNode node){
    NODES.put(key, node);
    return NODES.get(key);
}

}