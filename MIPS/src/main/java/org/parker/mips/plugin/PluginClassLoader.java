/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import org.parker.mips.MIPS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
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

    public PluginClassLoader(final File file, final Map<String, Object> yaml, ClassLoader parent) throws MalformedURLException, InvalidPluginException, InvalidDescriptionException {
        super(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());

        PLUGIN_YAML = yaml;
        try {
            this.DESCRIPTION = new PluginDescription(yaml);
        } catch (Exception e) {
            throw new InvalidDescriptionException("Could not load some part of plugin description from: " + file.getAbsolutePath());
        }
        this.FILE = file;

        try {
            Class<?> jarClass;
            try {
                if (file.getAbsolutePath().equals(new File(MIPS.JAR_PATH).getAbsolutePath())) {
                    jarClass = this.findClass(DESCRIPTION.MAIN);
                } else {
                    jarClass = Class.forName(DESCRIPTION.MAIN, true, this);//Class.forName(description.MAIN, true, this);//Class.forName(description.MAIN, true, this);F
                }
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class '" + DESCRIPTION.MAIN + "'", ex);
            }

            Class<? extends Plugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(Plugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + DESCRIPTION.MAIN + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No Valid public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    @Override
    public Class<?> loadClass(String string) throws ClassNotFoundException {
        if (string.startsWith(DESCRIPTION.MAIN)) {
            return this.findClass(string);
        } else {
            return super.loadClass(string);
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
                j.close();
            } catch (Exception e) {
            }
            try {
                i.close();
            } catch (Exception e) {

            }
            return super.getResourceAsStream(data);
        }

        return null;
    }

    @Override
    public Class<?> loadClass(String string, boolean bool) throws ClassNotFoundException {
        if (string.startsWith(DESCRIPTION.MAIN)) {
            return this.findClass(string);
        } else {
            return super.loadClass(string, bool);
        }
    }
}
