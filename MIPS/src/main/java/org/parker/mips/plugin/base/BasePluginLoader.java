/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.base;

import org.parker.mips.core.MIPS;
import org.parker.mips.plugin.exceptions.InvalidDescriptionException;
import org.parker.mips.plugin.exceptions.InvalidPluginException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */


public abstract class BasePluginLoader implements PluginLoader {

    public static final String DEFAULT_YAML_PATH = "plugin.yml";

    private static final Logger LOGGER = Logger.getLogger(BasePluginLoader.class.getName());

    public Plugin loadInternalPlugin(String yamlPath) throws InvalidDescriptionException, InvalidPluginException {
        return loadPlugin(new File(MIPS.JAR_PATH), yamlPath);
    }

    public Plugin loadPluginWithCustomYAML(File file, String yamlPath) throws InvalidDescriptionException, InvalidPluginException {
        return loadPlugin(file, yamlPath);
    }

    public Plugin loadPlugin(File file) throws InvalidDescriptionException, InvalidPluginException {
        return loadPlugin(file, DEFAULT_YAML_PATH);
    }

    public Plugin loadPlugin(File file, String yamlPath) throws InvalidDescriptionException, InvalidPluginException {

        if (yamlPath == null) {
            throw new InvalidPluginException("YAML path cannot be null");
        }

        if(file == null){//!(file.exists()) || file.isDirectory()){
            throw new InvalidPluginException("Plugin file: " + file.getAbsolutePath() + " cannot be null");
        }

        Map<String, Object> loadedYaml;

        JarFile jar = null;
        JarEntry entry;
        InputStream stream;


        if (file.getAbsolutePath().equals(new File(MIPS.JAR_PATH).getAbsolutePath())) {
            LOGGER.log(Level.FINE, "Plugin: " + file.getAbsolutePath() + ":->" + yamlPath + " is identified to be internal");
            stream = BasePluginLoader.class.getResourceAsStream(yamlPath);
        } else {
            LOGGER.log(Level.FINE, "Plugin: " + file.getAbsolutePath() + ":->" + yamlPath + " is identified to be external");

            try {
                jar = new JarFile(file);
            }catch (IOException e){
                throw new InvalidPluginException("There was an error while trying to read: " + file.getAbsolutePath() + ":->" + yamlPath, e);
            }

            entry = jar.getJarEntry(yamlPath);
            if (entry == null) {
                throw new InvalidDescriptionException("Jar does not contain: " + yamlPath);
            }
            try {
                stream = jar.getInputStream(entry);
            }catch (SecurityException | IOException | IllegalStateException e){
                throw new InvalidPluginException("There was an error while trying to read: " + file.getAbsolutePath() + ":->" + yamlPath, e);
            }
        }

        if (stream == null) {
            throw new InvalidPluginException("There was an error while trying to read: " + file.getAbsolutePath() + ":->" + yamlPath);
        }
        try {
            loadedYaml = (Map<String, Object>) new Yaml().load(stream);
        }catch (YAMLException e){
            throw new InvalidDescriptionException("there was an error while loading the YAML for plugin: " + file.getAbsolutePath() + ":->" + yamlPath, e);
        }
        if (loadedYaml == null) {
            throw new InvalidDescriptionException("YAML does not exist, was not loaded correctly or is empty for plugin: " + file.getAbsolutePath() + ":->" + yamlPath);
        }

        if (jar != null) {
            try {
                jar.close();
            }catch (Exception ignore){

            }
        }
        try{
            stream.close();
        }catch (Exception ignore){

        }

        PluginDescription description;
        try {
            description = new PluginDescription(loadedYaml);
        } catch (InvalidDescriptionException e) {
            throw new InvalidDescriptionException("Failed to load plugin description from plugin: " + file.getAbsolutePath() + ":->" + yamlPath, e);
        }

        PluginClassLoader loader;
        try {
            loader = createPluginLoader(file, yamlPath, loadedYaml, description, BasePluginLoader.class.getClassLoader());
        } catch (MalformedURLException e) {
            throw new InvalidPluginException("Malformed Plugin file path: " + file.getAbsolutePath() + ":->" + yamlPath, e);
        }

        if (loader.plugin == null) {
            throw new InvalidPluginException("failed to load Plugin: " + file.getAbsolutePath() + ":->" + yamlPath);
        }

        return loader.plugin;
    }

    protected abstract PluginClassLoader createPluginLoader(File file, String yamlPath, Map<String, Object> loadedYaml, PluginDescription description, ClassLoader parent) throws MalformedURLException, InvalidDescriptionException, InvalidPluginException;
}
