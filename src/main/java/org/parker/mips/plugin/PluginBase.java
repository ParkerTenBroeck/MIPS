/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import java.io.File;

/**
 *
 * @author parke
 */
public abstract class PluginBase implements Plugin {

    public final PluginDescription DESCRIPTION;
    final public File PLUGIN_FILE;

    //private boolean isEnabled = false;
    public PluginBase() {
        {
            final ClassLoader classLoader = this.getClass().getClassLoader();

            if (!(classLoader instanceof PluginClassLoader)) {
                throw new IllegalStateException("JavaPlugin requires " + PluginClassLoader.class.getName() + " And not " + classLoader.getClass().getName());
            }
        }
        final PluginClassLoader classLoader = ((PluginClassLoader) this.getClass().getClassLoader());

        this.DESCRIPTION = classLoader.DESCRIPTION;
        this.PLUGIN_FILE = classLoader.FILE;
    }
}
