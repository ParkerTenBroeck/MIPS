package org.parker.mips.architectures.mips.syscall;

import org.parker.mips.plugin.base.BasePluginLoader;
import org.parker.mips.plugin.base.Plugin;
import org.parker.mips.plugin.base.PluginClassLoader;
import org.parker.mips.plugin.base.PluginDescription;
import org.parker.mips.plugin.exceptions.InvalidDescriptionException;
import org.parker.mips.plugin.exceptions.InvalidPluginException;

import java.io.File;
import java.io.IOException;
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
