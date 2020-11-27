/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.SystemCall;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.parker.mips.plugin.InvalidPluginException;
import org.parker.mips.plugin.PluginDescription;

/**
 *
 * @author parke
 */
final class SystemCallPluginClassLoader extends URLClassLoader {

    private final PluginDescription DESCRIPTION;
    private final File FILE;
    final SystemCallPlugin plugin;

    public SystemCallPluginClassLoader(final ClassLoader parent, final PluginDescription description, final File file) throws InvalidPluginException, MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);

        System.out.println(parent);
        this.DESCRIPTION = description;
        this.FILE = file;

        try {
            Class<?> jarClass = null;
            try {
                jarClass = this.findClass(description.MAIN);//Class.forName(description.MAIN, true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + "description.getMain()" + "'", ex);
            }

            System.out.println(jarClass.getClassLoader());
            //plugin = (SystemCallPlugin) jarClass.newInstance();

            Class<? extends SystemCallPlugin> pluginClass = null;
            try {
                pluginClass = jarClass.asSubclass(SystemCallPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.MAIN + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
            System.out.println(plugin.getClass().getClassLoader());
            System.out.println(this);
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    public SystemCallPluginClassLoader(URL url) {
        super(new URL[]{url});
        this.DESCRIPTION = null;
        this.FILE = null;
        this.plugin = null;
    }

    private SystemCallPlugin pluginInit;

    synchronized void initialize(SystemCallPlugin plugin) {

        System.out.println("bruh");
        if (plugin == null) {
            throw new Error("Initializing plugin cannot be null");
        }
        if (plugin.getClass().getClassLoader() != this) {
            throw new Error("Cannot initialize plugin outside of this class loader");
        }

        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!");
        }

        this.pluginInit = plugin;

        //plugin.init(description);
    }

}
