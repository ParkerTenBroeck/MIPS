/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import org.parker.mips.plugin.exceptions.InvalidDescriptionException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author parke
 */
public class PluginDescription {

    //required
    public final String NAME;
    public final String MAIN;
    public final String VERSION;
    public final String DESCRIPTION;
    //not required
    public final String AUTHOR;
    public final ArrayList<String> AUTHORS;
    public final String WEBSITE;
    public final String PREFIX;

    public PluginDescription(Map<String, Object> map) throws InvalidDescriptionException {
        try {
            this.NAME = map.get("name").toString();
            this.MAIN = map.get("main").toString();
            this.VERSION = map.get("version").toString();
            this.DESCRIPTION = map.get("description").toString();
        } catch (Exception e) {
            throw new InvalidDescriptionException("Cannot load base plugin description");
        }

        String temp;
        try {
            temp = map.get("author").toString();
        } catch (Exception e) {
            temp = null;
        }
        this.AUTHOR = temp;

        ArrayList<String> tempA;
        try {
            tempA = (ArrayList<String>) map.get("authors");
        } catch (Exception e) {
            tempA = null;
        }
        this.AUTHORS = tempA;

        try {
            temp = map.get("website").toString();
        } catch (Exception e) {
            temp = null;
        }
        this.WEBSITE = temp;

        try {
            temp = map.get("prefix").toString();
        } catch (Exception e) {
            temp = null;
        }
        this.PREFIX = temp;
    }

    public PluginDescription(InputStream input) throws InvalidDescriptionException {
        this((Map<String, Object>) (new Yaml().load(input)));
    }

    public Map<String, Object> saveMap() {
        return null;
    }
}
