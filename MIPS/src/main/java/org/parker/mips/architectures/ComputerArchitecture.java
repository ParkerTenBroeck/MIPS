package org.parker.mips.architectures;

import javax.swing.*;

public interface ComputerArchitecture{

    void onLoad();
    void requestSystemExit();
    void requestSystemExit(SystemClosingEvent sce);

    interface SystemClosingEvent{
        void onClose();
    }

    JFrame createGUI();
}
