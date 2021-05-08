/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.assembleride.gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.google.common.io.Files;
import org.parker.assembleride.architectures.BaseComputerArchitecture;
import org.parker.mips.architecture.MipsArchitecture;
import org.parker.mips.architecture.emulator.mips.EmulatorMemory;
import org.parker.mips.architecture.gui.MipsEmulatorState;
import org.parker.mips.architecture.gui.SystemCallPluginInfoFrame;
import org.parker.assembleride.core.MIPS;
import org.parker.assembleride.core.SystemPreferences;
import org.parker.assembleride.gui.userpanes.editor.EditorHandler;
import org.parker.assembleride.gui.userpanes.editor.rsyntax.FormattedTextEditor;
import org.parker.assembleride.gui.theme.ThemeHandler;
import org.parker.assembleride.log.LogPanel;
import org.parker.mips.architecture.syscall.SystemCallPlugin;
import org.parker.mips.architecture.syscall.SystemCallPlugin.Node;
import org.parker.mips.architecture.syscall.SystemCallPluginHandler;
import org.parker.mips.architecture.emulator.mips.Emulator;
import org.parker.assembleride.util.DesktopBrowser;
import org.parker.assembleride.util.FileUtils;
import org.parker.assembleride.util.ResourceHandler;
import org.parker.assembleride.util.UpdateHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Parker TenBroeck
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MainGUI extends javax.swing.JFrame {

    private static boolean autoUpdateRunning;
    private static MainGUI instance;
    private static BaseComputerArchitecture bca;

    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());

    public static synchronized boolean canBreak() {
        return enableBreak.isSelected();
    }

    @SuppressWarnings("unchecked")
    private static synchronized void startAutoUpdate() {
        MainGUI.startButton.setSelected(true);

        MainGUI.autoUpdateRunning = true;

        if (!SystemPreferences.enableGUIAutoUpdateWhileRunning.val()) {
            return;
        }
        Thread autoUpdateThread = new Thread(() -> {

            while (autoUpdateRunning && SystemPreferences.enableGUIAutoUpdateWhileRunning.val()) {
                MainGUI.refresh();
                try {
                    Thread.sleep(SystemPreferences.GUIAutoUpdateRefreshTime.val());
                } catch (Exception ignored) {

                }
            }
        });
        autoUpdateThread.setName("autoUpdate");
        autoUpdateThread.start();
    }

    public static synchronized void stopAutoUpdate() {
        MainGUI.startButton.setSelected(false);
        MainGUI.autoUpdateRunning = false;
        MainGUI.refresh();
    }

    public static Component getFrame() {
        return mainPanel;
    }

    public static void refresh() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            register_GUI1.update();
            //InstructionMemoryGUI.refresh();
            InstructionsRan.setText(Long.toString(Emulator.getInstructionsRan()));
            UserPaneTabbedPane.updateOpenUserPanes();

            /*
            Line line = Debugger.getDataLineFromAddress(Registers.getPc());
            if(line != null) {
                //LOGGER.log(Level.INFO, (line.getHumanLineNumber() + ": " + line.getLine()));
                FileEditor e = EditorHandler.getEditorFromFile(line.getFile());
                if (e instanceof FormattedTextEditor) {
                    ((FormattedTextEditor) e).setHighlightedLine(line.getLineNumber());
                }
            }

             */
        });
    }

    /**
     * Creates new form Main_GUI
     */
    @SuppressWarnings("null")
    public MainGUI(BaseComputerArchitecture bca) {
        initComponents();
        this.bca = bca;

        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Images/Icons/PNG/logo4.png");
            assert url != null;
            ImageIcon icon = new ImageIcon(url);
            this.setIconImage(icon.getImage());
            //new SVGUniverse()

            //this.setIconImage(new FlatSVGIcon("Images/Icons/SVG/logo4.svg", 255, 255).getImage());

            aboutButton.setIcon(new FlatSVGIcon("Images/Icons/PNG/informationDialog.svg", (int) (aboutButton.getWidth() / 1.5), (int) (aboutButton.getHeight() / 1.5)));
            //aboutLinkedFile.setIcon(new FlatSVGIcon("images/informationDialog.svg", (int) (aboutLinkedFile.getWidth() / 1.5), (int) (aboutLinkedFile.getHeight() / 1.5)));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load Icon", e);
        }

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        MipsArchitecture.enableBreakPoints.LinkJButton(this, enableBreak);

        MipsArchitecture.saveAssemblyInfo.LinkJButton(this, saveAssemblyInformationButton);
        MipsArchitecture.savePreProcessedFile.LinkJButton(this, savePreProcessedFileButton);

        MipsArchitecture.breakOnRunTimeError.LinkJButton(this, breakProgramOnRTEButton);
        MipsArchitecture.adaptiveMemory.LinkJButton(this, adaptiveMemoryMenuButton);

        SystemPreferences.enableGUIAutoUpdateWhileRunning.LinkJButton(this,enableGUIUpdatingWhileRunningButton);

        assembleButton.addActionListener((ae) -> {
            LOGGER.log(Level.FINER, "Assemble Button Action Preformed");
            bca.onAssembleButton(ae);
        });

        disassembleButton.addActionListener(e -> {
            LOGGER.log(Level.FINER, "Disassemble Button Action Preformed");
            bca.onDisassembleButton(e);
        });

        startButton.addActionListener((ae) -> {
            LOGGER.log(Level.FINER, "Start Processor Button Action Preformed");
            bca.onStartButton(ae, startButton.isSelected());
        });

        stopButton.addActionListener((ae) -> {
            LOGGER.log(Level.FINER, "Stop Processor Button Action Preformed");
            bca.onStopButton(ae);
        });

        singleStepButton.addActionListener((ae) -> {
            LOGGER.log(Level.FINER, "Single Step Processor Button Action Preformed");
            bca.onSingleStepButton(ae);
            refresh();
        });

        resetButton.addActionListener((ae) -> {
            LOGGER.log(Level.FINER, "Reset Processor Button Action Preformed");
            bca.onResetButton(ae);
        });

        memoryButton.addActionListener((ae) -> {
            LOGGER.log(Level.FINER, "Memory Button Action Preformed");
            bca.onMemoryButton(ae);
        });

        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            LOGGER.log(Level.FINER, "Main Window Exit Action Preformed");
            bca.requestSystemExit();
            }
        };

        this.addWindowListener(exitListener);

        //Generates examples
        try {

            ActionListener al = evt -> {
                Emulator.stop();
                Emulator.reset();
                //if (FileHandler.loadExampleFile(new File(((ThemedJMenuItem) evt.getSource()).getName()))) {
                //new FormattedTextEditor();
                File file = new File(((JMenuItem) evt.getSource()).getName());

                new FormattedTextEditor(file);
                //Editor.createEditor(FileUtils.loadFileAsByteArraySafe(file), FileUtils.removeExtension(file.getName()), FormattedTextEditor.class);
                throw new RuntimeException("eat ass and chew bubblegum");
                //Assembler.assembleDefault();
                //}
            };

            for (Component comp : Objects.requireNonNull(generateJMenuFromFile(new File(ResourceHandler.EXAMPLES_PATH), al)).getMenuComponents()) {
                MainGUI.exampleMenu.add(comp);
            }

        } catch (Exception ignored) {
        }


        Thread.currentThread().setName("GUI");

        refresh();

        ThemeHandler.updateUI();
        this.setVisible(true);

        if (instance != null) {
            instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
        }else {
            instance = this;
        }
    }

    public static void reloadSystemCallPluginLists() {
        systemCallFrameJMenu.removeAll();
        registerSystemCallPluginsJMenu.removeAll();

        ArrayList<SystemCallPlugin> plugins = SystemCallPluginHandler.getRegisteredSystemCalls();
        for (SystemCallPlugin plugin : plugins) {

            {
            	javax.swing.JMenu tempMenu = new javax.swing.JMenu();
                tempMenu.setText(plugin.DESCRIPTION.NAME);
                javax.swing.JMenuItem tempItem = new javax.swing.JMenuItem();
                tempItem.setText("Open SystemCall Plugin Info Frame");
                tempItem.addActionListener((ae) -> {
                    new SystemCallPluginInfoFrame(plugin);
                    //generate some plugin info frame from the plugin
                });
                tempMenu.add(tempItem);

                tempItem = new javax.swing.JMenuItem();
                tempItem.setText("Unregister SystemCall Plugin");
                tempItem.addActionListener((ae) -> {
                    System.out.println(plugin);
                    SystemCallPluginHandler.unRegisterSystemCallPlugin(plugin);
                    //new SystemCallPluginInfoFrame(plugin);
                    //generate some plugin info frame from the plugin
                });
                tempMenu.add(tempItem);

                ArrayList<Node<ActionListener>> temp = plugin.getGeneralListeners(); //adds general listeners to syscall menu
                if (temp != null) {
                    temp.forEach((t) -> generateJMenuFromNodeStructure(tempMenu, t));
                }

                registerSystemCallPluginsJMenu.add(tempMenu);
            }
            {
                ArrayList<Node<ActionListener>> temp = plugin.getFrameListeners();
                if (temp != null) {
                	javax.swing.JMenu tempMenu = new javax.swing.JMenu();
                    tempMenu.setText(plugin.DESCRIPTION.NAME.replaceAll("_", " "));
                    temp.forEach((t) -> generateJMenuFromNodeStructure(tempMenu, t));
                    systemCallFrameJMenu.add(tempMenu);
                }
            }
            {
                ArrayList<Node<ActionListener>> temp = plugin.getInternalExamples();
                if (temp != null) {
                	javax.swing.JMenu tempMenu = new javax.swing.JMenu();
                    tempMenu.setText(plugin.DESCRIPTION.NAME.replaceAll("_", " "));
                    temp.forEach((t) -> generateJMenuFromNodeStructure(tempMenu, t));
                    systemCallExampleJMenu.add(tempMenu);
                }
            }
        }
    }

    private static javax.swing.JMenu generateJMenuFromNodeStructure(javax.swing.JMenu menu, Node<ActionListener> node) {
        if (node == null) {
            return menu;
        }

        if (node.hasChildern()) { // if node has no children then add to menu as an item
        	javax.swing.JMenuItem temp = new javax.swing.JMenuItem();
            temp.setText(node.name);
            temp.addActionListener(node.getData());
            menu.add(temp);
            return menu;
        }

        javax.swing.JMenu temp = new javax.swing.JMenu();
        temp.setText(node.name);
        Objects.requireNonNull(node.getChildernAndDestroyParent()).forEach((t) -> generateJMenuFromNodeStructure(temp, t));
        menu.add(temp);
        return menu;
    }

    private static javax.swing.JMenu generateJMenuFromFile(File file, ActionListener al) {
        if (file.isDirectory()) {
        	javax.swing.JMenu jMenu = new javax.swing.JMenu();

            String name = file.getName();
            if (name.contains("‰")) {
                try {
                    name = name.split("‰")[2];
                } catch (Exception ignored) {

                }
            }

            jMenu.setText(name);
            jMenu.setName(file.getAbsolutePath());
            for (File f2 : Objects.requireNonNull(file.listFiles())) {
                if (f2.isDirectory()) {
                    jMenu.add(generateJMenuFromFile(f2, al));
                } else {
                    if (f2.getName().split("\\.")[1].equals("mxn")) {
                        continue;
                    }
                    String name2 = f2.getName().split("\\.")[0];
                    if (name2.contains("‰")) {
                        try {
                            name2 = name2.split("‰")[2];
                        } catch (Exception ignored) {

                        }
                    }
                    javax.swing.JMenuItem jMenuItem = new javax.swing.JMenuItem();
                    jMenuItem.setText(name2);
                    jMenuItem.setName(f2.getAbsolutePath());
                    jMenuItem.addActionListener(al);
                    jMenu.add(jMenuItem);
                }
            }
            return jMenu;
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("all")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        topPanel = new javax.swing.JPanel();
        topButtonBarPanel = new javax.swing.JPanel();
        assembleButton = new javax.swing.JButton();
        disassembleButton = new javax.swing.JButton();
        startButton = new javax.swing.JToggleButton();
        stopButton = new javax.swing.JButton();
        singleStepButton = new javax.swing.JButton();
        memoryButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        InstructionsRan = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lowerContentPanel = new javax.swing.JPanel();
        register_GUI1 = new MipsEmulatorState();
        aSM_GUI1 = new UserPaneTabbedPane();
        midButtonSliderPanel = new javax.swing.JPanel();
        enableBreak = new javax.swing.JCheckBox();
        delaySlider = new javax.swing.JSlider();
        delayLabel = new javax.swing.JLabel();
        bottomPanel = new javax.swing.JPanel();
        logFrame = new LogPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuButton = new javax.swing.JMenuItem();
        exampleMenu = new javax.swing.JMenu();
        newMenuButton = new javax.swing.JMenuItem();
        saveMenuButton = new javax.swing.JMenuItem();
        saveAsMenuButton = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        optionsMenu = new javax.swing.JMenu();
        checkForUpdates = new javax.swing.JMenuItem();
        enableGUIUpdatingWhileRunningButton = new javax.swing.JCheckBoxMenuItem();
        optionsButton = new javax.swing.JMenuItem();
        assemblerMenu = new javax.swing.JMenu();
        asciiChartButton = new javax.swing.JMenuItem();
        documentationButton = new javax.swing.JMenuItem();
        savePreProcessedFileButton = new javax.swing.JCheckBoxMenuItem();
        saveAssemblyInformationButton = new javax.swing.JCheckBoxMenuItem();
        processorMenu = new javax.swing.JMenu();
        saveMemoryButton = new javax.swing.JMenuItem();
        loadMemoryButton = new javax.swing.JMenuItem();
        breakProgramOnRTEButton = new javax.swing.JCheckBoxMenuItem();
        adaptiveMemoryMenuButton = new javax.swing.JCheckBoxMenuItem();
        systemCallPluginsJMenu = new javax.swing.JMenu();
        systemCallFrameJMenu = new javax.swing.JMenu();
        systemCallExampleJMenu = new javax.swing.JMenu();
        registerSystemCallPluginsJMenu = new javax.swing.JMenu();
        loadPluginJMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("MIPS");
        setBackground(new java.awt.Color(102, 102, 102));
        setMinimumSize(new java.awt.Dimension(800, 600));

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(745);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOpaque(false);

        topPanel.setMinimumSize(new java.awt.Dimension(400, 400));
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new java.awt.Dimension(500, 10000));

        topButtonBarPanel.setBackground(new java.awt.Color(102, 102, 102));
        topButtonBarPanel.setOpaque(false);

        assembleButton.setText("Assemble");
        assembleButton.setFocusable(false);

        disassembleButton.setText("Disassemble");
        disassembleButton.setToolTipText("Dissasembles the data currently in memory");
        disassembleButton.setFocusable(false);

        startButton.setText("Start");
        startButton.setFocusable(false);

        stopButton.setText("Stop");
        stopButton.setFocusable(false);

        singleStepButton.setText("Single Step");
        singleStepButton.setFocusable(false);

        memoryButton.setText("Memory");
        memoryButton.setFocusable(false);

        aboutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Icons/PNG/info.png"))); // NOI18N
        aboutButton.setBorderPainted(false);
        aboutButton.setContentAreaFilled(false);
        aboutButton.setFocusPainted(false);
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });

        InstructionsRan.setText("InstructionsRan");

        resetButton.setText("Reset");
        resetButton.setFocusable(false);
        resetButton.setOpaque(false);

        jLabel2.setText(MIPS.VERSION);

        javax.swing.GroupLayout topButtonBarPanelLayout = new javax.swing.GroupLayout(topButtonBarPanel);
        topButtonBarPanel.setLayout(topButtonBarPanelLayout);
        topButtonBarPanelLayout.setHorizontalGroup(
            topButtonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topButtonBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(assembleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singleStepButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disassembleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InstructionsRan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 449, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aboutButton)
                .addContainerGap())
        );
        topButtonBarPanelLayout.setVerticalGroup(
            topButtonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topButtonBarPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(topButtonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(aboutButton)
                    .addGroup(topButtonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(assembleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(singleStepButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(memoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(disassembleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(InstructionsRan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3))
        );

        lowerContentPanel.setMinimumSize(new java.awt.Dimension(0, 150));
        lowerContentPanel.setOpaque(false);

        register_GUI1.setMaximumSize(new java.awt.Dimension(600, 32797));
        register_GUI1.setMinimumSize(new java.awt.Dimension(0, 0));

        aSM_GUI1.setMinimumSize(new java.awt.Dimension(500, 56));

        javax.swing.GroupLayout lowerContentPanelLayout = new javax.swing.GroupLayout(lowerContentPanel);
        lowerContentPanel.setLayout(lowerContentPanelLayout);
        lowerContentPanelLayout.setHorizontalGroup(
            lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowerContentPanelLayout.createSequentialGroup()
                .addComponent(aSM_GUI1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(register_GUI1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                //.addComponent(instructionMemory_GUI1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        lowerContentPanelLayout.setVerticalGroup(
            lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            //.addComponent(instructionMemory_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(register_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
            .addComponent(aSM_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        midButtonSliderPanel.setOpaque(false);

//        linkedButton.setText("Linked File");
//        linkedButton.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                linkedButtonActionPerformed(evt);
//            }
//        });

//        aboutLinkedFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/info.png"))); // NOI18N
//        aboutLinkedFile.setBorderPainted(false);
//        aboutLinkedFile.setContentAreaFilled(false);
//        aboutLinkedFile.setFocusPainted(false);
//        aboutLinkedFile.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                aboutLinkedFileActionPerformed(evt);
//            }
//        });

        enableBreak.setSelected(true);
        enableBreak.setText("Enable BreakPoints");

        //delaySlider.setForeground(new java.awt.Color(0, 204, 153));
        delaySlider.setMaximum(1000);
        delaySlider.setValue(0);
        delaySlider.setFocusable(false);

        delayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        delayLabel.setText("Instruction Delay: " + delaySlider.getValue() + "ns");
        delaySlider.addChangeListener(cl ->{
        	long trueValue = (long) Math.pow(delaySlider.getValue(), 3);
        	delayLabel.setText("Instruction Delay: " + trueValue + "ns");
        	 Emulator.setDelay(trueValue);
        });

        javax.swing.GroupLayout midButtonSliderPanelLayout = new javax.swing.GroupLayout(midButtonSliderPanel);
        midButtonSliderPanel.setLayout(midButtonSliderPanelLayout);
        midButtonSliderPanelLayout.setHorizontalGroup(
            midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midButtonSliderPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enableBreak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                    .addGroup(midButtonSliderPanelLayout.createSequentialGroup()
//                        .addComponent(linkedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                        .addComponent(aboutLinkedFile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                ).addGap(63, 63, 63)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delaySlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(delayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(191, 191, 191))
        );
        midButtonSliderPanelLayout.setVerticalGroup(
            midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midButtonSliderPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    //.addComponent(linkedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    //.addComponent(aboutLinkedFile, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enableBreak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lowerContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(midButtonSliderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(topButtonBarPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addComponent(topButtonBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midButtonSliderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lowerContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(topPanel);

        bottomPanel.setMinimumSize(new java.awt.Dimension(200, 50));
        bottomPanel.setPreferredSize(new java.awt.Dimension(1214, 200));

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 1214, Short.MAX_VALUE)
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(bottomPanel);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1214, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 877, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 877, Short.MAX_VALUE))
        );

        menuBar.setBackground(new java.awt.Color(51, 51, 51));
        menuBar.setBorder(null);
        menuBar.setOpaque(false);

        fileMenu.setText("File");

        openMenuButton.setText("Open");
        openMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuButtonActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuButton);

        exampleMenu.setText("Examples");
        fileMenu.add(exampleMenu);

        newMenuButton.setText("New");
        newMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuButtonActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuButton);

        saveMenuButton.setText("Save");
        saveMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuButtonActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuButton);

        saveAsMenuButton.setText("SaveAs");
        saveAsMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuButtonActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuButton);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");
        menuBar.add(editMenu);

        optionsMenu.setText("Options");

        optionsButton.setText("Options");
        optionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsButtonActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsButton);

        checkForUpdates.setText("Update");
        checkForUpdates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkForUpdatesActionPerformed(evt);
            }
        });
        optionsMenu.add(checkForUpdates);

        enableGUIUpdatingWhileRunningButton.setSelected(true);
        enableGUIUpdatingWhileRunningButton.setText("Enable GUI Updating While Running");
        optionsMenu.add(enableGUIUpdatingWhileRunningButton);

        menuBar.add(optionsMenu);

        assemblerMenu.setText("Assembler");

        asciiChartButton.setText("Ascii chart");
        asciiChartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asciiChartButtonActionPerformed(evt);
            }
        });
        assemblerMenu.add(asciiChartButton);

        documentationButton.setText("Documentation");
        documentationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentationButtonActionPerformed(evt);
            }
        });
        assemblerMenu.add(documentationButton);

        savePreProcessedFileButton.setSelected(true);
        savePreProcessedFileButton.setText("Save PreProcessed File");
        assemblerMenu.add(savePreProcessedFileButton);

        saveAssemblyInformationButton.setSelected(true);
        saveAssemblyInformationButton.setText("Save Assembly Information");
        assemblerMenu.add(saveAssemblyInformationButton);

        menuBar.add(assemblerMenu);

        processorMenu.setText("Processor");

        breakProgramOnRTEButton.setSelected(true);
        breakProgramOnRTEButton.setText("Break Program On RunTime Error");
        processorMenu.add(breakProgramOnRTEButton);

        adaptiveMemoryMenuButton.setText("Adaptive Memory");
        processorMenu.add(adaptiveMemoryMenuButton);
        
        saveMemoryButton.setText("Save Memory to File");
        loadMemoryButton.setText("Load Memory from File");
        saveMemoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMemoryButtonActionPreformed(evt);
            }
        });
        loadMemoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMemoryButtonActionPreformed(evt);
            }
        });
        processorMenu.add(saveMemoryButton);
        processorMenu.add(loadMemoryButton);

        menuBar.add(processorMenu);

        systemCallPluginsJMenu.setText("SystemCall Plugins");

        systemCallFrameJMenu.setText("SystemCall Frames");
        systemCallPluginsJMenu.add(systemCallFrameJMenu);

        systemCallExampleJMenu.setText("SystemCall Examples");
        systemCallPluginsJMenu.add(systemCallExampleJMenu);

        registerSystemCallPluginsJMenu.setText("Registered SystemCall Plugins");
        systemCallPluginsJMenu.add(registerSystemCallPluginsJMenu);

        loadPluginJMenuItem.setText("Load Plugin");
        loadPluginJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPluginJMenuItemActionPerformed(evt);
            }
        });
        systemCallPluginsJMenu.add(loadPluginJMenuItem);

        menuBar.add(systemCallPluginsJMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
        DesktopBrowser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS/blob/master/README.md");
    }

    public void setControlsEnabled(boolean enabled) {
        assembleButton.setEnabled(enabled);
        startButton.setEnabled(enabled);
        stopButton.setEnabled(enabled);
        singleStepButton.setEnabled(enabled);
        resetButton.setEnabled(enabled);
        memoryButton.setEnabled(enabled);
        disassembleButton.setEnabled(enabled);
    }

    private void openMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuButtonActionPerformed
        JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());

        if (returnVal != JFileChooser.FILES_AND_DIRECTORIES) {
            File chosenFile = fc.getSelectedFile();
            //Editor.loadFileIntoEditor(chosenFile);
            new FormattedTextEditor(chosenFile);
        }
    }//GEN-LAST:event_openMenuButtonActionPerformed

    private void saveMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuButtonActionPerformed
        EditorHandler.saveLastFocused();
    }//GEN-LAST:event_saveMenuButtonActionPerformed

    private void saveAsMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuButtonActionPerformed
        EditorHandler.saveAsLastFocused();
    }//GEN-LAST:event_saveAsMenuButtonActionPerformed
    
    private void saveMemoryButtonActionPreformed(java.awt.event.ActionEvent evt) {
        bca.stopEmulator();
    	JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showSaveDialog(MainGUI.getFrame());
        if (returnVal == JFileChooser.FILES_ONLY) {
            FileUtils.saveByteArrayToFileSafe(EmulatorMemory.getMemory(), fc.getSelectedFile());
        }
    }
    
    private void loadMemoryButtonActionPreformed(java.awt.event.ActionEvent evt) {
        bca.onStopButton(evt);
    	JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());
        //if (returnVal == JFileChooser.FILES_ONLY) {
        File selected = fc.getSelectedFile();

        if(returnVal == JFileChooser.CANCEL_OPTION){
            return;
        }

        if(selected == null || !selected.exists()){
            LOGGER.log(Level.WARNING, "Cannot load memory chosen file does not exist");
            return;
        }
        if(selected.isDirectory()) {
            LOGGER.log(Level.WARNING, "Cannot load memory chosen file does not exist");
            return;
        }
        if(!selected.canWrite()){
            LOGGER.log(Level.WARNING, "Cannot load memory chosen file is read only");
            return;
        }
        byte[] data;
        try{
            data = Files.toByteArray(selected);
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Cannot load memory", e);
            return;
        }
        bca.setEmulatorMemory(data);
        bca.resetEmulator();
    }

    private void newMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuButtonActionPerformed
        new FormattedTextEditor();
    }

    private void asciiChartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asciiChartButtonActionPerformed
        new imageFrame("/Images/asciiChart.bmp");
    }

    private void checkForUpdatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkForUpdatesActionPerformed
        UpdateHandler.update();
    }//GEN-LAST:event_checkForUpdatesActionPerformed

    private void documentationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentationButtonActionPerformed
        try {
            DesktopBrowser.openLinkInBrowser(ResourceHandler.DOCUMENTATION_PATH + FileUtils.FILE_SEPARATOR + "index.html");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Cannot open desktop browser", ex);
        }
    }

    private void optionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsButtonActionPerformed
        new OptionsGUI();
    }

    private void loadPluginJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadPluginJMenuItemActionPerformed
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());
        if (returnVal != 0) {
            return;
        }
        File chosenFile = fc.getSelectedFile();
        throw new RuntimeException("This stopped being a thing a while ago");
        //BasePluginLoader.loadPlugin(chosenFile);
    }//GEN-LAST:event_loadPluginJMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel InstructionsRan;
    private static UserPaneTabbedPane aSM_GUI1;
    private static javax.swing.JButton aboutButton;
    private static javax.swing.JCheckBoxMenuItem adaptiveMemoryMenuButton;
    private static javax.swing.JMenuItem asciiChartButton;
    private static javax.swing.JPanel bottomPanel;
    private static javax.swing.JCheckBoxMenuItem breakProgramOnRTEButton;
    private static javax.swing.JMenuItem checkForUpdates;
    private static javax.swing.JButton assembleButton;
    private static javax.swing.JButton disassembleButton;
    private static javax.swing.JMenu assemblerMenu;
    private static javax.swing.JLabel delayLabel;
    private static javax.swing.JSlider delaySlider;
    private static javax.swing.JMenuItem documentationButton;
    private static javax.swing.JMenu editMenu;
    private static javax.swing.JCheckBox enableBreak;
    private static javax.swing.JCheckBoxMenuItem enableGUIUpdatingWhileRunningButton;
    private static javax.swing.JMenu exampleMenu;
    private static javax.swing.JMenu fileMenu;
    private static javax.swing.JLabel jLabel2;
    private static javax.swing.JSplitPane jSplitPane1;
    private static javax.swing.JMenuItem loadPluginJMenuItem;
    private static LogPanel logFrame;
    private static javax.swing.JPanel lowerContentPanel;
    private static javax.swing.JPanel mainPanel;
    private static javax.swing.JButton memoryButton;
    private static javax.swing.JMenuBar menuBar;
    private static javax.swing.JPanel midButtonSliderPanel;
    private static javax.swing.JMenuItem newMenuButton;
    private static javax.swing.JMenuItem openMenuButton;
    private static javax.swing.JMenuItem optionsButton;
    private static javax.swing.JMenu optionsMenu;
    private static javax.swing.JMenu registerSystemCallPluginsJMenu;
    private static MipsEmulatorState register_GUI1;
    private static javax.swing.JButton resetButton;
    private static javax.swing.JMenu processorMenu;
    private static javax.swing.JMenuItem saveMemoryButton;
    private static javax.swing.JMenuItem loadMemoryButton;
    private static javax.swing.JMenuItem saveAsMenuButton;
    private static javax.swing.JCheckBoxMenuItem saveAssemblyInformationButton;
    private static javax.swing.JMenuItem saveMenuButton;
    private static javax.swing.JCheckBoxMenuItem savePreProcessedFileButton;
    private static javax.swing.JButton singleStepButton;
    private static javax.swing.JToggleButton startButton;
    private static javax.swing.JButton stopButton;
    private static javax.swing.JMenu systemCallExampleJMenu;
    private static javax.swing.JMenu systemCallFrameJMenu;
    private static javax.swing.JMenu systemCallPluginsJMenu;
    private static javax.swing.JPanel topButtonBarPanel;
    private static javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
