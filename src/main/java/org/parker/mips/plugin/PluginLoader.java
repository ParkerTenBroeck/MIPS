/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.parker.mips.Log;
import org.parker.mips.MIPS;
import org.parker.mips.plugin.SystemCall.SystemCallPluginHandler;
import org.parker.mips.plugin.SystemCall.SystemCallPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 *
 * @author parke
 */
public class PluginLoader {

    public static final String DEFAULT_YAML_PATH = "plugin.yml";

    public static Plugin loadPluginWithCustomYAML(File file, String yamlPath) {
        return PluginLoader.loadPlugin(file, yamlPath);
    }

    public static Plugin loadPlugin(File file) {
        return PluginLoader.loadPlugin(file, DEFAULT_YAML_PATH);
    }

    private static Plugin loadPlugin(File file, String yamlPath) {

        Map<String, Object> loadedYaml = null;

        JarFile jar = null;
        InputStream stream = null;

        try {
            if (file.getAbsolutePath().equals(new File(MIPS.JAR_PATH).getAbsolutePath())) {
                stream = PluginLoader.class.getResourceAsStream(yamlPath);
            } else {
                jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry(yamlPath);

                if (entry == null) {
                    //throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
                }

                stream = jar.getInputStream(entry);
            }

            if (stream == null) {
                //throw new InvalidPluginException(yamlPath);
            }
            loadedYaml = (Map<String, Object>) new Yaml().load(stream);

        } catch (IOException ex) {
            //throw new InvalidDescriptionException(ex);
        } catch (YAMLException ex) {
            //throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }

        PluginClassLoader current = null;
        try {
            current = new PluginClassLoader(file, loadedYaml, PluginLoader.class.getClassLoader());
        } catch (MalformedURLException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidPluginException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (SystemCallPlugin) current.plugin;
    }

    public static void loadDefaultPlugins() {
        SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) loadPlugin(new File("C:\\GitHub\\MIPS\\examples\\exampleSystemCallPlugin\\dist\\exampleSystemCallPlugin.jar")));
        //loadExternalPlugin(new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH + "/exampleSystemCallPlugin.jar"));
        //loadInternalPlugin("/org/parker/mips/Processor/InternalSystemCallPlugins/test.yml");
    }

    public static void logPluginLoaderError(String message) {
        Log.logError("[Plugin Loader] " + message);
    }

    public static void logPluginLoaderWarning(String message) {
        Log.logWarning("[Plugin Loader] " + message);
    }

    public static void logPluginLoaderSystemMessage(String message) {
        Log.logSystemMessage("[Plugin Loader] " + message);
    }

}
