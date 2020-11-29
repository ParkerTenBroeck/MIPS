/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.syscall;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.parker.mips.gui.MainGUI;
import org.parker.mips.Log;
import org.parker.mips.processor.Memory;
import org.parker.mips.processor.Processor;
import org.parker.mips.processor.Registers;
import org.parker.mips.plugin.PluginBase;
import org.parker.mips.plugin.PluginClassLoader;

/**
 *
 * @author parke
 */
public abstract class SystemCallPlugin extends PluginBase {

    private final SystemCallPlugin instance;
    private final SystemCall[] systemCalls;
    private final SystemCall.SystemCallData[] systemCallData;
    
    private Object ad;

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

    public static class NamedActionListener {

        public final String FRAME_NAME;
        public final ActionListener AL;

        public NamedActionListener(String name, ActionListener al) {
            this.FRAME_NAME = name;
            this.AL = al;
        }
    }

    public static class Node<T> {

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

        public void addChildren(ArrayList<Node<T>> children) {
            if (children == null) {
                children = new ArrayList();
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

    @Override
    public void onLoad() {

    }

    @Override
    public boolean onUnload() {
        return true;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDissable() {

    }

    /**
     * This method returns all of opening events the plugin contains
     *
     * This allows for each plugin to have multiple frames accociated with it
     *
     * @return returns null if no opening events are used
     */
    public NamedActionListener[] getAllSystemCallFrameNamedActionListeners() {
        return null;
    }

    /**
     * NOT YET IMPLEMENTED
     *
     * @return
     */
    protected Node<URL> getInternalSystemCallExampleResources() {
        return null;
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

        public PRSystemCall(String systemCallName, Object o) {
            super(instance.getSystemCallDataFromName(systemCallName), systemCallName, instance);
        }

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
     * Used for when a SystemCall encounters some error during runtime
     *
     * WARNING errors can halt the program if enabled use Warning if program can
     * continue
     *
     * @param message the message that will be logged as a warning
     */
    protected final void logRunTimeSystemCallError(String message) {
        SystemCallPluginHandler.logRunTimeSystemCallError(message);
    }

    /**
     * Used for when a SystemCall has some error but can handle it and continue
     * to run
     *
     * @param message the message that will be logged as a warning
     */
    protected final void logRunTimeSystemCallWarning(String message) {
        SystemCallPluginHandler.logRunTimeSystemCallWarning(message);
    }

    /**
     * Used for when a SystemCall is needed to log a message
     *
     * @param message the message that will be logged
     */
    protected final void logRunTimeSystemCallMessage(String message) {
        SystemCallPluginHandler.logRunTimeSystemCallMessage(message);
    }

    /**
     *
     * @param message message to be loged
     */
    protected final void logSystemCallPluginError(String message) {
        Log.logError("[System Call Plugin] " + message);
    }

    /**
     *
     * @param message message to be loged
     */
    protected final void logSystemCallPluginWarning(String message) {
        Log.logWarning("[System Call Plugin] " + message);
    }

    /**
     *
     * @param message message to be loged
     */
    protected final void logSystemCallPluginMessage(String message) {
        Log.logMessage("[System Call Plugin] " + message);
    }

    /**
     *
     * @return
     */
    protected final boolean throwBreakPoint() {

        if (MainGUI.canBreak()) {
            stopProcessor();
            logRunTimeSystemCallMessage("Program has reached a breakpoint");
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
