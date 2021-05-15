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

    @Override
    public PluginDescription getPluginDescription() {
        return DESCRIPTION;
    }
}
