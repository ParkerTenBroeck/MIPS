/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.syscall;

import org.parker.mips.FileUtils;
import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.editor.Editor;
import org.parker.mips.gui.editor.rsyntax.FormattedTextEditor;
import org.parker.mips.plugin.PluginBase;
import org.parker.mips.plugin.PluginClassLoader;
import org.parker.mips.processor.Memory;
import org.parker.mips.processor.Processor;
import org.parker.mips.processor.Registers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author parke
 */
public abstract class SystemCallPlugin extends PluginBase {

    private final SystemCallPlugin instance;
    private final SystemCall[] systemCalls;
    private final SystemCall.SystemCallData[] systemCallData;

    private ArrayList<Node<ActionListener>> reigsteredFrameListeners = null;
    private ArrayList<Node<ActionListener>> registeredInternalExamples = null;
    private ArrayList<Node<ActionListener>> registeredGeneralListeners = null;

    public SystemCallPlugin() {
        {
            final ClassLoader classLoader = this.getClass().getClassLoader();
            if (!(classLoader instanceof PluginClassLoader)) {
                throw new IllegalStateException("JavaPlugin requires " + PluginClassLoader.class.getName() + " And not " + classLoader.getClass().getName());
            }
        }
        final PluginClassLoader classLoader = ((PluginClassLoader) this.getClass().getClassLoader());

        Set set = ((Map<String, Map<String, Object>>) classLoader.PLUGIN_YAML.get("system_calls")).entrySet();

        final int size = set.size();
        this.systemCalls = new SystemCall[size];
        this.systemCallData = new SystemCall.SystemCallData[size];

        Iterator iterator = set.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry next = (Map.Entry) iterator.next();

            Map<String, Object> tempMap = (Map<String, Object>) next.getValue();
            tempMap.put("SYSTEM_CALL_NAME", next.getKey());

            SystemCall.SystemCallData tempData;
            tempData = new SystemCall.SystemCallData(tempMap);
            systemCallData[i] = tempData;
            i++;
        }

        this.instance = this;
    }

    public static final class Node<T> {

        public final String name;
        private T data = null;
        private ArrayList<Node<T>> children = null;
        private Node<T> parent = null;

        public Node(String name, T data) {
            this.name = name;
            this.data = data;
        }

        public Node(String name) {
            this.name = name;
            this.data = null;
        }

        public Node(T data) {
            this.data = data;
            this.name = null;
        }

        public Node(String name, Node<T>[] children) {
            this.name = name;
            this.addChildren(children);
        }

        public Node<T> addChild(String name, T data) {
            Node<T> child = new Node(name, data);
            this.addChild(child);
            return child;
        }

        public Node<T> addChild(String name) {
            Node<T> child = new Node(name);
            this.addChild(child);
            return child;
        }

        public Node<T> addChild(T data) {
            Node<T> child = new Node(data);
            this.addChild(child);
            return child;
        }

        public Node<T> addChild(Node<T> child) {
            if (children == null) {
                children = new ArrayList();
            }

            child.setParent(this);
            this.children.add(child);
            return child;
        }

        public void addChildren(Node<T>[] children) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            for (Node<T> each : children) {
                each.setParent(this);
                this.children.add(each);
            }

        }

        public void addChildren(ArrayList<Node<T>> children) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            children.forEach(each -> each.setParent(this));
            this.children.addAll(children);
        }

        public boolean hasChildern() {
            return children == null;
        }

        public ArrayList<Node<T>> getChildren() {
            return children;
        }

        public ArrayList<Node<T>> getChildernAndDestroyParent() {
            if (this.children == null) {
                return null;
            }

            children.forEach(each -> each.setParent(null));
            return children;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        private void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public Node<T> getParent() {
            return parent;
        }

    }

    protected class ResourceActionLoader implements ActionListener {

        private final String resources;

        public ResourceActionLoader(String resources) {
            this.resources = resources;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
            	InputStream stream = CLASS_LOADER.getResourceAsStream(resources);
            	byte[] bytes = FileUtils.loadStreamAsByteArray(stream);
            	//String text = new String(bytes);
            	//new FormattedTextEditor(text);
                Editor.createEditor(bytes, null, FormattedTextEditor.class);
            //FileHandler.loadASMExampleFromStream(CLASS_LOADER.getResourceAsStream(resources));
            //ASM_GUI.setTextAreaFromASMFile();
          
            //Assembler.compileDefault();
            }catch(Exception e) {
            	
            }
        }

    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    /**
     * Registers a
     *
     * @param data
     *
     * @return if the data was register correctly or not
     */
    protected final boolean registerInternalExamples(Node<ActionListener> data) {
        this.registeredInternalExamples = data.getChildernAndDestroyParent();
        return true;
    }

    /**
     *
     * @param data
     * @return
     */
    protected final boolean registerFrameListeners(Node<ActionListener> data) {
        this.reigsteredFrameListeners = data.getChildernAndDestroyParent();
        return true;
    }

    /**
     *
     * @param data
     * @return
     */
    protected final boolean registerGeneralListeners(Node<ActionListener> data) {
        this.registeredGeneralListeners = data.getChildernAndDestroyParent();
        return true;
    }

    //protected final boolean register
    public final ArrayList<Node<ActionListener>> getInternalExamples() {
        return registeredInternalExamples;
    }

    public final ArrayList<Node<ActionListener>> getFrameListeners() {
        return this.reigsteredFrameListeners;
    }

    public final ArrayList<Node<ActionListener>> getGeneralListeners() {
        return this.registeredGeneralListeners;
    }

    private SystemCall.SystemCallData getSystemCallDataFromName(String name) {
        for (SystemCall.SystemCallData scd : systemCallData) {
            if (scd.SYSTEM_CALL_NAME.equals(name)) {
                return scd;
            }
        }
        return null;
    }

    protected abstract class PRSystemCall extends SystemCall {

        public PRSystemCall(String systemCallName) {
            super(getSystemCallDataFromName(systemCallName), systemCallName, instance);
        }
    }

    /**
     *
     * @param prsc The system call to be registered
     */
    protected final void registerSystemCall(PRSystemCall prsc) {
        for (int i = 0; i < systemCallData.length; i++) {
            if (systemCallData[i].SYSTEM_CALL_NAME.equals(prsc.DATA.SYSTEM_CALL_NAME)) {
                systemCalls[i] = prsc;
                return;
            }
        }
        throw new Error("System Call: " + prsc.DATA.SYSTEM_CALL_NAME + " Was not registered in plugin.yml");
    }

    public final SystemCall[] getSystemCalls() {
        return systemCalls.clone();
    }

    /**
     *
     * @return
     */
    protected final boolean throwBreakPoint() {

        if (MainGUI.canBreak()) {
            stopProcessor();
            LOGGER.log(Level.INFO, "Program has reached a breakpoint");
        }

        return MainGUI.canBreak();
    }

    /**
     * halts the processor the processor can only be started again by the user
     *
     */
    protected final void stopProcessor() {
        Processor.stop();
    }

    /**
     * resets the processor setting all registers to 0 and halting also reloads
     * memory if options is set
     */
    protected final void resetProcessor() {
        Processor.stop();
        Processor.reset();
    }

    /**
     *
     * @param index the index in bytes where the data is being stored at
     * @param val the full word (4 bytes) that will be stored at the index
     */
    protected final void setWord(int index, int val) {
        Memory.setWord(index, val);
    }

    /**
     *
     * @param index the index in bytes where the data is being stored at
     * @param val the half word (2 bytes) that will be stored at the index
     */
    protected final void setHelfWord(int index, int val) {
        Memory.setHalfWord(index, val);
    }

    /**
     *
     * @param index the index in bytes where the data is being stored at
     * @param val the byte (1 bytes) that will be stored at the index
     */
    protected final void setByte(int index, int val) {
        Memory.setByte(index, val);
    }

    /**
     *
     * @param index the index in bytes where the data being retrieved is held
     * @return returns a full word (4 bytes) at the location in memory of index
     */
    protected final int getWord(int index) {
        return Memory.getWord(index);
    }

    /**
     *
     * @param index the index in bytes where the data being retrieved is held
     * @return returns a half word (2 bytes) at the location in memory of index
     */
    protected final int getHalfWord(int index) {
        return Memory.getHalfWord(index);
    }

    /**
     *
     * @param index the index in bytes where the data being retrieved is held
     * @return returns a byte (1 bytes) at the location in memory of index
     */
    protected final int getByte(int index) {
        return Memory.getByte(index);
    }

    /**
     *
     * @param reg a value from 0 - 31 that represents the register number
     * @return returns the value held at the register
     */
    protected final int getRegister(int reg) {
        return Registers.getRegister(reg);

    }

    /**
     * sets the specified register to the value of val
     *
     * @param reg a value from 0 - 31 that represents the register number
     * @param val the integer that will be placed in the specified register
     */
    protected final void setRegister(int reg, int val) {
        Registers.setRegister(reg, val);
    }

    /**
     *
     * @return returns the current value of the Program counter
     */
    protected final int getPc() {
        return Registers.getPc();
    }

    /**
     *
     * @param val the value that the Program Counter will be set to
     */
    protected final void setPc(int val) {
        Registers.setPc(val);
    }

    /**
     *
     * @return returns the current value of the HIGH register
     */
    protected final int getHigh() {
        return Registers.getHigh();
    }

    /**
     *
     * @param val the value the HIGH register will be set to
     */
    protected final void setHigh(int val) {
        Registers.setHigh(val);
    }

    /**
     *
     * @return returns the current value of the LOW register
     */
    protected final int getLow() {
        return Registers.getLow();
    }

    /**
     *
     * @param val the value the LOW register will be set to
     */
    protected final void setLow(int val) {
        Registers.setLow(val);
    }

    /**
     * NOT IMPLEMENTED YET
     */
    protected final void throwNonInturuptableIntturupt() { //still in the works
        throw new Error("Not implemented yet");
    }

}
