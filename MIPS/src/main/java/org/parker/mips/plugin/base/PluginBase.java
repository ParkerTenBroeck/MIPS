/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.base;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
@SuppressWarnings("unused")
public abstract class PluginBase implements Plugin {

    public final PluginDescription DESCRIPTION;
    public final Map<String, Object> YAML;
    public final File PLUGIN_FILE;
    protected final PluginClassLoader CLASS_LOADER;

    protected final Logger LOGGER;

    public PluginBase() {

        if (!(this.getClass().getClassLoader() instanceof PluginClassLoader)) {
            throw new SecurityException("Plugin requires " + PluginClassLoader.class.getName() + " And not " + this.getClass().getClassLoader().getClass().getName());
        }

        CLASS_LOADER = (PluginClassLoader) this.getClass().getClassLoader();

        this.DESCRIPTION = CLASS_LOADER.DESCRIPTION;
        this.YAML = CLASS_LOADER.PLUGIN_YAML;
        this.PLUGIN_FILE = CLASS_LOADER.FILE;

        this.LOGGER = new PluginLogger(this);
    }
}
