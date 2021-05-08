/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.assembleride.plugin.base;

import org.parker.assembleride.plugin.exceptions.InvalidDescriptionException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
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
        } catch (Exception e) {
            throw new InvalidDescriptionException("Cannot load plugin name");
        }
        try {
            this.MAIN = map.get("main").toString();
        } catch (Exception e) {
            throw new InvalidDescriptionException("Cannot load plugin main class path");
        }
        try {
            this.VERSION = map.get("version").toString();
        } catch (Exception e) {
            throw new InvalidDescriptionException("Cannot load plugin version");
        }

        try {
            this.DESCRIPTION = map.get("description").toString();
        } catch (Exception e) {
            throw new InvalidDescriptionException("Cannot load plugin description");
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
