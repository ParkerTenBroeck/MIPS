package org.parker.mips.architectures;

import javax.swing.*;

public interface ComputerArchitecture {

    void onLoad();
    void requestSystemExit() throws UnableToExitException;

    JFrame createGUI();
}
