/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.base;

import org.parker.mips.architectures.BaseComputerArchitecture;
import org.parker.mips.core.MIPS;
import org.parker.mips.plugin.exceptions.InvalidDescriptionException;
import org.parker.mips.plugin.exceptions.InvalidPluginException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 *
 * @author parke
 */
public class PluginClassLoader extends URLClassLoader {

    // private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    public final PluginDescription DESCRIPTION;
    public final Map<String, Object> PLUGIN_YAML;
    public final File FILE;
    public final Plugin plugin;

    public PluginClassLoader(final File file, final String yamlPath, final Map<String, Object> yaml, PluginDescription description, ClassLoader parent) throws InvalidPluginException, InvalidDescriptionException, MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);

        this.PLUGIN_YAML = Collections.unmodifiableMap(yaml);
        this.DESCRIPTION = description;
        this.FILE = file;

        try {
            Class<?> jarClass;
            try {
                if (file.getAbsolutePath().equals(new File(MIPS.JAR_PATH).getAbsolutePath())) {
                    jarClass = this.findClass(DESCRIPTION.MAIN);
                } else {
                    jarClass = Class.forName(DESCRIPTION.MAIN, true, this);
                }
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class '" + DESCRIPTION.MAIN + "'", ex);
            }
            Class<? extends Plugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(Plugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + DESCRIPTION.MAIN + "' does not extend " + Plugin.class.getName(), ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No Valid public constructor for plugin: " + file.getAbsolutePath() + ":->" + yamlPath, ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type: " + file.getAbsolutePath() + ":->" + yamlPath, ex);
        }
    }

    @Override
    public InputStream getResourceAsStream(String data) {
        JarFile j = null;
        InputStream i = null;
        try {
            j = new JarFile(this.FILE);
            ZipEntry entry = j.getEntry(data);
            i = j.getInputStream(entry);
            if (i != null) {
                return i;
            }
        } catch (IOException ex) {
            try {
                if(j != null)
                j.close();
            } catch (Exception e) {
            }
            try {
                if(i != null)
                i.close();
            } catch (Exception e) {

            }
            return super.getResourceAsStream(data);
        }

        return null;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);

        if(c == null){
            if (name.startsWith(DESCRIPTION.MAIN)) { //this is the only way i could get it to work with internal lambda funcitons
                c = this.findClass(name);
            } else {
                c = super.loadClass(name, resolve);
            }
        }
        if(resolve){
            resolveClass(c);
        }
        return c;
    }


}
