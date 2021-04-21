package org.parker.mips.architectures;

public interface ComputerArchitecture {

    void onLoad();
    void onExit();

    void onStartButton();
    void onSingleStepButton();
    void onStopButton();
    void onDisassembleButton();

}
