package org.parker.mips.plugin;

import org.parker.mips.architectures.ArchitecturePluginHandler;
import org.parker.mips.architectures.ComputerArchitecture;
import org.parker.mips.plugin.base.BasePluginLoader;
import org.parker.mips.plugin.base.Plugin;
import org.parker.mips.plugin.base.PluginClassLoader;
import org.parker.mips.plugin.base.PluginDescription;
import org.parker.mips.plugin.exceptions.InvalidDescriptionException;
import org.parker.mips.plugin.exceptions.InvalidPluginException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

public class ArchitecturePluginLoader extends BasePluginLoader {
    @Override
    protected PluginClassLoader createPluginLoader(File file, String yamlPath, Map<String, Object> loadedYaml, PluginDescription description, ClassLoader parent) throws MalformedURLException, InvalidDescriptionException, InvalidPluginException {
        ArchitecturePluginHandler.setToLoadDescription(description);
        return new PluginClassLoader(file, yamlPath, loadedYaml, description, parent);
    }

    public static ComputerArchitecture loadPluginS(File file, String yaml) throws InvalidDescriptionException, InvalidPluginException {
        Plugin p = new ArchitecturePluginLoader().loadPlugin(file, yaml);
        if(!(p instanceof ComputerArchitecture)){
            throw new InvalidPluginException("Cannot load ArchitecturePlugin, is instance of plugin but not of ComputerArchitecture");
        }else{
            return (ComputerArchitecture) p;
        }
    }
}
