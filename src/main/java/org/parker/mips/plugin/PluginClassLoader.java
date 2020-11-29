/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.parker.mips.MIPS;

/**
 *
 * @author parke
 */
public class PluginClassLoader extends URLClassLoader {

    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

    public final PluginDescription DESCRIPTION;
    public final Map<String, Object> PLUGIN_YAML;
    public final File FILE;
    public final Plugin plugin;

    public PluginClassLoader(final File file, final Map<String, Object> yaml, ClassLoader parent) throws MalformedURLException, InvalidPluginException {
        super(new URL[]{file.toURI().toURL()}, parent);

        PLUGIN_YAML = yaml;
        this.DESCRIPTION = new PluginDescription(yaml);
        this.FILE = file;

        try {
            Class<?> jarClass = null;
            try {

                jarClass = Class.forName(DESCRIPTION.MAIN.replace("/", "//."), true, this);//Class.forName(description.MAIN, true, this);//Class.forName(description.MAIN, true, this);F

            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class '" + DESCRIPTION.MAIN + "'", ex);
            }

            Class<? extends Plugin> pluginClass = null;
            try {
                pluginClass = jarClass.asSubclass(Plugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + DESCRIPTION.MAIN + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }
//
//    Set<String> getClasses() {
//        return classes.keySet();
//    }
//
//    @Override
//    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        return findClass(name, true);
//    }
//
//    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
////        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
////            throw new ClassNotFoundException(name);
////        }
//        Class<?> result = classes.get(name);
//
//        if (result == null) {
//            if (checkGlobal) {
//                result = super.findClass(name);
//            }
//
//            classes.put(name, result);
//        }
//
//        return result;
//    }
}
