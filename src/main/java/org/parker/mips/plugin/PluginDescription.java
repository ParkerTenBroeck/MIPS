/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author parke
 */
public class PluginDescription {

    public final String NAME;
    public final String MAIN;
    public final String VERSION;
    public final String DESCRIPTION;

    public PluginDescription(Map<String, Object> map) throws InvalidDescriptionException {
        try{
        this.NAME = map.get("name").toString();
        this.MAIN = map.get("main").toString();
        this.VERSION = map.get("version").toString();
        this.DESCRIPTION = map.get("description").toString();
        }catch(Exception e){
            throw new InvalidDescriptionException("Cannot load base plugin description");
        }
    }

    public PluginDescription(InputStream input) throws InvalidDescriptionException {
        this((Map<String, Object>) (new Yaml().load(input)));
    }

    public Map<String, Object> saveMap() {
        return null;
    }
}
