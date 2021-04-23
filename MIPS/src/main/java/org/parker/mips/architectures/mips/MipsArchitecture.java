package org.parker.mips.architectures.mips;

import org.parker.mips.architectures.BaseComputerArchitecture;
import org.parker.mips.architectures.mips.disassembler.MipsDisassembler;
import org.parker.mips.architectures.mips.emulator.mips.Emulator;
import org.parker.mips.architectures.mips.emulator.mips.MemoryWrapper;
import org.parker.mips.architectures.mips.syscall.SystemCallPlugin;
import org.parker.mips.architectures.mips.syscall.SystemCallPluginHandler;
import org.parker.mips.gui.userpanes.editor.EditorHandler;
import org.parker.mips.plugin.PluginLoader;
import org.parker.mips.util.Memory;
import org.parker.mips.util.ResourceHandler;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MipsArchitecture extends BaseComputerArchitecture {

    private static final Logger LOGGER = Logger.getLogger(MipsArchitecture.class.getName());

    @Override
    public void onLoad() {

        loadDefaultPlugins();
    }

    public void loadDefaultPlugins() {
        try {

            try {
                SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) PluginLoader.loadInternalPlugin("/org/parker/mips/internal/syscall/default.yml"));
                SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) PluginLoader.loadInternalPlugin("/org/parker/mips/internal/syscall/screen.yml"));
                SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) PluginLoader.loadInternalPlugin("/org/parker/mips/internal/syscall/userio.yml"));
            }catch(Exception e){
                LOGGER.log(Level.SEVERE, "Failed to load an internal Plugin", e);
            }

            File file = new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH);
            File files[] = file.listFiles();
            for (File f : files) {
                if (f.exists()) {
                    try {
                        SystemCallPlugin scp = (SystemCallPlugin) PluginLoader.loadPlugin(f);
                        SystemCallPluginHandler.registerSystemCallPlugin(scp);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Failed to load External Plugin: " + f.getAbsolutePath(), e);
                    }
                }
                SystemCallPluginHandler.regenerateStandardSysCallHeaderFile();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }


    @Override
    public void onAssembleButton(ActionEvent ae) {
            Emulator.reset();
            EditorHandler.saveAll();
            //assemble
    }

    @Override
    public void onStartButton(ActionEvent ae, boolean isSelected) {
        if (isSelected) {
            Emulator.start();
            //MainGUI.startAutoUpdate();
        } else {
            Emulator.stop();
        }
    }

    @Override
    public void onStopButton(ActionEvent ae) {
        Emulator.stop();
    }

    @Override
    public void onSingleStepButton(ActionEvent ae) {
        if (!Emulator.isRunning()) {
            Emulator.runSingleStep();
        }
    }

    @Override
    public void onResetButton(ActionEvent ae) {
        Emulator.reset();
    }

    @Override
    public void onDisassembleButton(ActionEvent ae) {
        MipsDisassembler.disassemble();
    }

    @Override
    public Memory getProcessorMemory() {
        return new MemoryWrapper();
    }
}
