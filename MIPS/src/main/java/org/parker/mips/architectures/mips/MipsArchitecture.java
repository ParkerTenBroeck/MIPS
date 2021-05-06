package org.parker.mips.architectures.mips;

import org.parker.mips.architectures.BaseComputerArchitecture;
import org.parker.mips.architectures.mips.assembler.MipsAssembler;
import org.parker.mips.architectures.mips.disassembler.MipsDisassembler;
import org.parker.mips.architectures.mips.emulator.mips.Emulator;
import org.parker.mips.architectures.mips.emulator.mips.EmulatorMemory;
import org.parker.mips.architectures.mips.emulator.mips.MemoryWrapper;
import org.parker.mips.architectures.mips.gui.MipsEmulatorState;
import org.parker.mips.architectures.mips.syscall.SystemCallPlugin;
import org.parker.mips.architectures.mips.syscall.SystemCallPluginHandler;
import org.parker.mips.architectures.mips.syscall.SystemCallPluginLoader;
import org.parker.mips.gui.userpanes.UserPane;
import org.parker.mips.gui.userpanes.editor.EditorHandler;
import org.parker.mips.preferences.Preference;
import org.parker.mips.preferences.Preferences;
import org.parker.mips.util.Memory;
import org.parker.mips.util.PagedMemory;
import org.parker.mips.util.ResourceHandler;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MipsArchitecture extends BaseComputerArchitecture {

    private static final Logger LOGGER = Logger.getLogger(MipsArchitecture.class.getName());

    public static final Preferences emulatorPrefs = ROOT_ARC_PREFS.getNode("emulator");
    public static final Preferences runtimePrefs = emulatorPrefs.getNode("runtime");
    public static final Preferences assemblerPrefs = ROOT_ARC_PREFS.getNode("assembler");
    public static final Preferences defaultSysCllPluginPrefs = ROOT_ARC_PREFS.getNode("plugins/systemCalls/Base");


    // Assembler
    public static final Preference<Boolean> savePreProcessedFile =	assemblerPrefs.getRawPreference("savePreProcessedFile",false);
    public static final Preference<Boolean> saveAssemblyInfo = assemblerPrefs.getRawPreference("saveAssemblyInfo",false);

    // PreProcessor
    public static final Preference<Boolean> includeRegDef = assemblerPrefs.getRawPreference("includeRegDef", true);
    public static final Preference<Boolean> includeSysCallDef = assemblerPrefs.getRawPreference("includeSysCallDef", true);

    // Processor
    // Run Time
    public static final Preference<Boolean> breakOnRunTimeError = runtimePrefs.getRawPreference("breakOnRunTimeError", true);
    public static final Preference<Boolean> adaptiveMemory = runtimePrefs.getRawPreference("adaptiveMemory", false);
    public static final Preference<Boolean> enableBreakPoints = runtimePrefs.getRawPreference("enableBreakPoints", true);

    // Non RunTime
    public static final Preference<Boolean> reloadMemoryOnReset = emulatorPrefs.getRawPreference("reloadMemoryOnReset", true);

    // System Calls
    public static final Preference<Boolean> resetProcessorOnTrap0 = defaultSysCllPluginPrefs.getRawPreference("resetProcessorOnTrap0", false);

    @Override
    public void onLoad() {
        loadDefaultSystemCallPlugins();
    }

    @Override
    public void onUnload() {

    }

    public void loadDefaultSystemCallPlugins() {
        try {
            try {
                SystemCallPluginHandler.registerSystemCallPlugin(SystemCallPluginLoader.loadInternalPluginS("/org/parker/mips/internal/syscall/default.yml"));
                SystemCallPluginHandler.registerSystemCallPlugin(SystemCallPluginLoader.loadInternalPluginS("/org/parker/mips/internal/syscall/screen.yml"));
                SystemCallPluginHandler.registerSystemCallPlugin(SystemCallPluginLoader.loadInternalPluginS("/org/parker/mips/internal/syscall/userio.yml"));
            }catch(Exception e){
                LOGGER.log(Level.SEVERE, "Failed to load an internal Plugin", e);
            }

            File file = new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH);
            File files[] = file.listFiles();
            for (File f : files) {
                if (f.exists()) {
                    try {
                        SystemCallPlugin scp = SystemCallPluginLoader.loadPluginS(f);
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
    protected Memory assemble(File[] files) {
        return new MipsAssembler().assemble(files);
    }

    @Override
    public void startEmulator() {
        Emulator.start();
    }

    @Override
    public void stopEmulator() {
        Emulator.stop();
    }

    @Override
    public void singeStepEmulator() {
        if (!Emulator.isRunning()) {
            Emulator.runSingleStep();
        }
    }

    @Override
    public void resetEmulator() {
        Emulator.reset();
        if(reloadMemoryOnReset.val()){
            EmulatorMemory.reload();
        }
    }

    @Override
    public void disassembleCurrentMemory() {
        MipsDisassembler.disassemble();
    }

    @Override
    public void setEmulatorMemory(Memory mem) {
        if(mem != null) {
            if (mem instanceof PagedMemory) {
                PagedMemory pMemory = (PagedMemory) mem;

                byte[] temp = new byte[pMemory.getPageCount() * 4096];
                for (int p = 0; p < pMemory.getPageCount(); p++) {
                    byte[] page = pMemory.getPage(p);
                    for (int i = 0; i < 4096; i++) {
                        temp[i + p * 4096] = page[i];
                    }
                }
                EmulatorMemory.setMemory(temp);
            } else {
                byte[] data = new byte[(int) mem.getSize()];
                for (int i = 0; i < data.length; i++) {
                    data[i] = mem.getByte(i);
                }
                EmulatorMemory.setMemory(data);
            }
        }
    }

    @Override
    public Memory getProcessorMemory() {
        return new MemoryWrapper();
    }

    @Override
    public UserPane getEmulatorStatePanel() {
        return new MipsEmulatorState();
    }
}
