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
package org.parker.mips.architecture.syscall;

import org.parker.assembleride.plugin.base.BasePluginLoader;
import org.parker.assembleride.plugin.base.Plugin;
import org.parker.assembleride.plugin.base.PluginClassLoader;
import org.parker.assembleride.plugin.base.PluginDescription;
import org.parker.assembleride.plugin.exceptions.InvalidDescriptionException;
import org.parker.assembleride.plugin.exceptions.InvalidPluginException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

public class SystemCallPluginLoader extends BasePluginLoader {


    @Override
    protected PluginClassLoader createPluginLoader(File file, String yamlPath, Map<String, Object> loadedYaml, PluginDescription description, ClassLoader parent) throws MalformedURLException, InvalidDescriptionException, InvalidPluginException {
        return new PluginClassLoader(file, yamlPath, loadedYaml, description, parent);
    }

    public static SystemCallPlugin loadInternalPluginS(String yamlPath) throws InvalidDescriptionException, InvalidPluginException {
        return ensureSCP(new SystemCallPluginLoader().loadInternalPlugin(yamlPath));
    }

    public static SystemCallPlugin loadPluginS(File file) throws InvalidPluginException, InvalidDescriptionException {
        return ensureSCP(new SystemCallPluginLoader().loadPlugin(file));
    }

    protected static SystemCallPlugin ensureSCP(Plugin plugin) throws InvalidPluginException {
        if(!(plugin instanceof SystemCallPlugin)){
            throw new InvalidPluginException("Plugin: ");
        }else{
            return (SystemCallPlugin) plugin;
        }
    }
}
