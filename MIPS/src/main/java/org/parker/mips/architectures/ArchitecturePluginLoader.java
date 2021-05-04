package org.parker.mips.architectures;


import org.parker.mips.architectures.mips.MipsArchitecture;

public class ArchitecturePluginLoader {

    private static ComputerArchitecture ca;

    public static synchronized void loadArchitecturePlugin() {
        if(ca != null){
            throw new IllegalStateException("Cannot load Architecture Plugin after startup");
        }
        ca = new MipsArchitecture();
        ca.onLoad();

        ca.createGUI().setVisible(true);
    }

    public static void requestSystemExit(){
        ca.requestSystemExit();
    }
    public static void requestSystemExit(ComputerArchitecture.SystemClosingEvent sce){
        ca.requestSystemExit(sce);
    }
}
