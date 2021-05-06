package org.parker.mips.architectures;


import org.parker.mips.core.MIPS;
import org.parker.mips.plugin.ArchitecturePluginLoader;
import org.parker.mips.plugin.base.Plugin;
import org.parker.mips.plugin.base.PluginDescription;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchitecturePluginHandler {

    private static ComputerArchitecture ca;
    private static PluginDescription description;
    private final static Logger LOGGER = Logger.getLogger(ArchitecturePluginHandler.class.getName());

    public static synchronized void loadArchitecturePlugin() {
        if(ca != null){
            throw new IllegalStateException("Cannot load Architecture Plugin after startup");
        }
        ComputerArchitecture arcPlugin;
        try {
            arcPlugin = ArchitecturePluginLoader.loadPluginS(new File(MIPS.JAR_PATH), "/org/parker/mips/architectures/mips/plugin.yml");
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Failed to load Architecture Plugin", e);
            return;
        }

        ComputerArchitecture ca = (ComputerArchitecture) arcPlugin;
        ca.onLoad();

        ca.createGUI().setVisible(true);
    }

    public static void setToLoadDescription(PluginDescription description) {
        if(description != null){
            ArchitecturePluginHandler.description = description;
        }else{
            throw new IllegalStateException("Cannot set Description after initial load");
        }
    }

    public static PluginDescription getDescription(){
        return description;
    }

    public static void requestSystemExit(){
        ca.requestSystemExit();
    }
    public static void requestSystemExit(ComputerArchitecture.SystemClosingEvent sce){
        ca.requestSystemExit(sce);
    }
}
