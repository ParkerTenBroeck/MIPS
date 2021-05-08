/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examplesystemcallplugin;

import org.parker.mips.architecture.syscall.SystemCallPlugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

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

        //
        registerInternalExamples(new Node("Root",
                new Node[]{
                    new Node("Test 1 Folder",
                            new Node[]{
                                new Node("File 3", new ResourceActionLoader("exampleProgram1.asm")),
                                new Node("File 4", new ResourceActionLoader("exampleProgram2.asm"))
                            }),
                    new Node("File 1", new ResourceActionLoader("exampleProgram1.asm")),
                    new Node("File 2", new ResourceActionLoader("exampleProgram2.asm"))
                }));

        //
        registerFrameListeners(new Node("Root",
                new Node[]{
                    new Node("Clock", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            exampleFrame.setVisible(true);
                            exampleFrame.requestFocus();
                        }
                    })}));

        //
        registerGeneralListeners(new Node("Root",
                new Node[]{
                    new Node("Example 1", (ActionListener) (ActionEvent ae) -> {
                        LOGGER.log(Level.INFO, "Example 1");
                    }),
                    new Node("Example SubFolder",
                            new Node[]{
                                new Node("Example 3", (ActionListener) (ActionEvent ae) -> {
                                    LOGGER.log(Level.INFO,"[Example SubFolder] Example 3");
                                }),
                                new Node("Example 4", (ActionListener) (ActionEvent ae) -> {
                                    LOGGER.log(Level.INFO,"[Example SubFolder] Example 4");
                                })}),
                    new Node("Example 2", (ActionListener) (ActionEvent ae) -> {
                        LOGGER.log(Level.INFO,"Example 2");
                    })}));
    }

    @Override
    public void onLoad() {
        //this can be used for any initiation after the constructor if needed
    }

    @Override
    public void onUnload() {

        exampleFrame.dispose();
    }
}
