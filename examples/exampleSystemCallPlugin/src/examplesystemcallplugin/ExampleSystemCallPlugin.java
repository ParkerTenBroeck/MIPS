/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examplesystemcallplugin;

import java.net.URL;
import org.parker.mips.plugin.syscall.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class ExampleSystemCallPlugin extends SystemCallPlugin {

    ExampleFrame exampleFrame = new ExampleFrame();

    public ExampleSystemCallPlugin() {

        registerSystemCall(new PRSystemCall("EXAMPLE_SET_HOURS") { //make sure that the name entered here and the name in ExampleSystemCallPlugin match this is for verification
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                exampleFrame.setHours(getRegister(4));
            }
        });
        registerSystemCall(new PRSystemCall("EXAMPLE_SET_MINS") {
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                exampleFrame.setMins(getRegister(4));
            }
        });
        registerSystemCall(new PRSystemCall("EXAMPLE_READ_HOURS") {
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                setRegister(2, exampleFrame.getHours());
            }
        });
        registerSystemCall(new PRSystemCall("EXAMPLE_READ_MINS") {
            @Override
            public void handleSystemCall() {
                exampleFrame.opExampleFrame();
                setRegister(2, exampleFrame.getMins());
            }
        });

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
