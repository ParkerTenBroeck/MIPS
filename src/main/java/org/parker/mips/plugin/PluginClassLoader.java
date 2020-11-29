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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import org.parker.mips.MIPS;
import org.parker.mips.Processor.InternalSystemCallPlugins.DefaultSystemCalls.DefaultSystemCalls;

/**
 *
 * @author parke
 */
public class PluginClassLoader extends URLClassLoader {

    // private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    public final PluginDescription DESCRIPTION;
    public final Map<String, Object> PLUGIN_YAML;
    public final File FILE;
    public final Plugin plugin;

    public PluginClassLoader(final File file, final Map<String, Object> yaml, ClassLoader parent) throws MalformedURLException, InvalidPluginException {
        super(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());

        Thread.currentThread().setContextClassLoader(new ParentLastURLClassLoader(new URL[]{file.toURI().toURL()}));

        PLUGIN_YAML = yaml;
        this.DESCRIPTION = new PluginDescription(yaml);
        this.FILE = file;

        //new DefaultSystemCalls();

        try {
            Class<?> jarClass = null;
            try {
                if (file.getAbsolutePath().equals(new File(MIPS.JAR_PATH).getAbsolutePath())) {
                    jarClass = Thread.currentThread().getContextClassLoader().loadClass(DESCRIPTION.MAIN);//this.findClass(DESCRIPTION.MAIN);
                } else {
                    jarClass = Class.forName(DESCRIPTION.MAIN, true, this);//Class.forName(description.MAIN, true, this);//Class.forName(description.MAIN, true, this);F
                }
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class '" + DESCRIPTION.MAIN + "'", ex);
            }

            Class<? extends Plugin> pluginClass = null;
            try {
                //pluginClass = jarClass.asSubclass(Plugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + DESCRIPTION.MAIN + "' does not extend JavaPlugin", ex);
            }

            plugin = (Plugin) jarClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

//    private boolean needsModifying(String name) {
//        // TODO
//        return false;
//    }
//
//    private byte[] modifyClass(InputStream original) throws IOException {
//        byte[] bruh = new byte[10000];
//        original.read(bruh);
//        return bruh;
//    }
//
//    @Override
//    public Class<?> findClass(String name) throws ClassNotFoundException {
//        if (needsModifying(name)) {
//            try {
//                InputStream classData = getResourceAsStream(name.replace('.', '/') + ".class");
//                if (classData == null) {
//                    throw new ClassNotFoundException("class " + name + " is not findable");
//                }
//                byte[] array = modifyClass(classData);
//                return defineClass(name, array, 0, array.length);
//            } catch (Exception io) {
//                throw new ClassNotFoundException(io.toString());
//            }
//        } else {
//            return super.findClass(name);
//        }
//    }
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
