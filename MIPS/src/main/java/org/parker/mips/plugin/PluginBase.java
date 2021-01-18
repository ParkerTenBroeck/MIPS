/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public abstract class PluginBase implements Plugin {

    public final PluginDescription DESCRIPTION;
    final public File PLUGIN_FILE;
    final protected ClassLoader CLASS_LOADER;

    protected final Logger LOGGER;

    //private boolean isEnabled = false;
    public PluginBase() {
        {
            CLASS_LOADER = this.getClass().getClassLoader();

            if (!(CLASS_LOADER instanceof PluginClassLoader)) {
                throw new IllegalStateException("JavaPlugin requires " + PluginClassLoader.class.getName() + " And not " + CLASS_LOADER.getClass().getName());
            }
        }
        final PluginClassLoader classLoader = ((PluginClassLoader) this.getClass().getClassLoader());

        this.DESCRIPTION = classLoader.DESCRIPTION;
        this.PLUGIN_FILE = classLoader.FILE;

        this.LOGGER = new PluginLogger(this);
    }

    protected final URL getResources(String string) {
        return this.getClass().getClassLoader().getResource(string);
    }

    protected final InputStream getResourcesAsStream(String string) {
        return this.getClass().getClassLoader().getResourceAsStream(string);
    }
}
