/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examplesystemcallplugin;

import java.net.URL;
import org.parker.mips.plugin.SystemCall.SystemCall;
import org.parker.mips.plugin.SystemCall.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class ExampleSystemCallPlugin extends SystemCallPlugin {

    ExampleFrame exampleFrame = new ExampleFrame();

    public ExampleSystemCallPlugin() {

        super(4, "Example_Plugin"); //initiates the plugin with 4 system calls with the name Example_Plugin

        SystemCall.SystemCallData[] scd = this.getSystemCallDataFromClass(this.getClass()); //loads the data stored in ExampleSystemCallPlugin

        this.systemCalls[0] = new SystemCall(scd[0], "EXAMPLE_SET_HOURS", this) { //make sure that the name entered here and the name in ExampleSystemCallPlugin match this is for verification
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                exampleFrame.setHours(getRegister(4));
            }
        };
        this.systemCalls[1] = new SystemCall(scd[1], "EXAMPLE_SET_MINS", this) {
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                exampleFrame.setMins(getRegister(4));
            }
        };
        this.systemCalls[2] = new SystemCall(scd[2], "EXAMPLE_READ_HOURS", this) {
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                setRegister(2, exampleFrame.getHours());
            }
        };
        this.systemCalls[3] = new SystemCall(scd[3], "EXAMPLE_READ_MINS", this) {
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                setRegister(2, exampleFrame.getMins());
            }
        };

    }

    @Override
    public void onLoad() {
        //this can be used for any initiation after the constructor if needed
    }

    @Override
    public boolean onUnload() {
        //nessisary code to unload the plugin
        return true; //return false if there was an error
    }

    
    @Override
    public NamedActionListener[] getAllSystemCallFrameNamedActionListeners() {

        return new NamedActionListener[]{new NamedActionListener("Clock", (ae) -> {
            this.exampleFrame.setVisible(true);
            this.exampleFrame.requestFocus();
        })};
    }

    @Override
    public Node<URL> getInternalSystemCallExampleResources() {

        Class<?> c = this.getClass();

        Node<URL> root = new Node("Root");
        Node<URL> temp = root.addChild("Path test 1");
        temp.addChild("Prog 1", c.getResource("exampleProgram1.asm"));
        temp.addChild("Prog 2", c.getResource("exampleProgram2.asm"));

        return root;
    }
}