package org.parker.mips.architectures;

import org.parker.mips.architectures.mips.MipsArchitecture;
import org.parker.mips.architectures.mips.emulator.mips.Emulator;
import org.parker.mips.gui.userpanes.editor.EditorHandler;

public class ArchitectureAccess {

    public ComputerArchitecture ca = new MipsArchitecture();

    public static void onAssembleButton() {
        Emulator.reset();
        EditorHandler.saveAll();
        //assemble
    }
}
