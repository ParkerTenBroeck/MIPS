/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import org.parker.mips.compiler.ASMCompiler;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.parker.mips.gui.theme.components.ThemedJMenu;
import org.parker.mips.gui.theme.components.ThemedJMenuItem;
import javax.swing.JOptionPane;
import org.parker.mips.FileHandler;
import org.parker.mips.Log;
import org.parker.mips.MIPS;
import org.parker.mips.plugin.syscall.SystemCallPlugin;
import org.parker.mips.processor.Memory;
import org.parker.mips.processor.Processor;
import org.parker.mips.ResourceHandler;
import org.parker.mips.UpdateHandler;
import org.parker.mips.OptionsHandler;
import org.parker.mips.plugin.syscall.SystemCallPluginHandler;
import org.parker.mips.plugin.PluginLoader;
import org.parker.mips.plugin.syscall.SystemCallPlugin.Node;

/**
 *
 * @author parke
 */
public class MainGUI extends javax.swing.JFrame {

    private static Thread autoUpdateThread;
    private static boolean autoUpdate;

    public static synchronized boolean isRunning() {
        return autoUpdate;
    }

    public static boolean isLinked() {
        return linkedButton.isSelected();
    }

    public static synchronized boolean canBreak() {
        return enableBreak.isSelected();
    }

    private static synchronized void startAutoUpdate() {
        MainGUI.startButton.setSelected(true);
        MainGUI.autoUpdate = true;
        if (!OptionsHandler.enableGUIAutoUpdateWhileRunning.value) {
            return;
        }
        autoUpdateThread = new Thread() {
            public void run() {
                while (autoUpdate && OptionsHandler.enableGUIAutoUpdateWhileRunning.value) {
                    MainGUI.refresh();
                    try {
                        Thread.sleep(OptionsHandler.GUIAutoUpdateRefreshTime.value);
                    } catch (Exception e) {

                    }
                }
            }
        };
        autoUpdateThread.setName("autoUpdate");
        autoUpdateThread.start();
    }

    public static synchronized void stopAutoUpdate() {
        MainGUI.startButton.setSelected(false);
        //MainGUI.startButton.repaint();
        //System.out.println(startButton.isSelected());
        MainGUI.autoUpdate = false;
        MainGUI.refresh();
    }

    public static Component getFrame() {
        return mainPanel;
    }

    public static void refreshAll() {
        ASM_GUI.setTextAreaFromASMFile();
        Memory.setMemory(FileHandler.getLoadedMXNFile());
        InstructionMemoryGUI.refreshValues();
        refresh();
    }

    /**
     * Creates new form Main_GUI
     */
    public MainGUI() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        enableBreak.setSelected(OptionsHandler.enableBreakPoints);
        linkedButton.setSelected(OptionsHandler.linkedFile);

        saveCompileInformationButton.setSelected(OptionsHandler.saveCompilationInfo);
        savePreProcessedFileButton.setSelected(OptionsHandler.savePreProcessedFile);

        breakProgramOnRTEButton.setSelected(OptionsHandler.breakOnRunTimeError);
        adaptiveMemoryMenuButton.setSelected(OptionsHandler.adaptiveMemory);

        enableGUIUpdatingWhileRunningButton.setSelected(OptionsHandler.enableGUIAutoUpdateWhileRunning);
        logSystemMessagesButton.setSelected(OptionsHandler.logSystemMessages);
        logMessagesButton.setSelected(OptionsHandler.logMessages);
        logWarningsButton.setSelected(OptionsHandler.logWarnings);
        logErrorsButton.setSelected(OptionsHandler.logErrors);

        addCompileButtonListener((ae) -> {
            Processor.stop();
            Processor.reset();
            ASMCompiler.compile();
        });

        addStartButtonListener((ae) -> {
            if (startButton.isSelected()) {
                Processor.start();
                MainGUI.startAutoUpdate();
            } else {
                Processor.stop();
            }
        });

        addStopButtonListener((ae) -> {
            Processor.stop();
        });

        addSingleStepButtonListener((ae) -> {
            if (!startButton.isSelected()) {
                Processor.runSingleStep();
            }
            refresh();
        });

        addResetButtonListener((ae) -> {
            Processor.stop();
            Processor.reset();
        });

        //new ModernSliderUI(this.delaySlider);
        //makeTextAreaAutoScroll(virtualConsolLog);
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                if (!FileHandler.isASMFileSaved()) {
                    int confirm = JOptionPane.showOptionDialog(
                            null, "you have unsaved work are you sure you want to exit?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (confirm == 0) {
                        OptionsHandler.saveOptionsToDefaultFile();
                        System.exit(0);
                    }
                } else {
                    OptionsHandler.saveOptionsToDefaultFile();
                    System.exit(0);
                }

            }
        };
        this.addWindowListener(exitListener);

        try {

            ActionListener al = new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Processor.stop();
                    Processor.reset();
                    if (FileHandler.loadExampleFile(new File(((ThemedJMenuItem) evt.getSource()).getName()))) {
                        ASMCompiler.compile();
                    }
                }
            };

            for (Component comp : generateJMenuFromFile(new File(ResourceHandler.EXAMPLES_PATH), al).getMenuComponents()) {
                MainGUI.exampleMenu.add((JComponent) comp);
            }

        } catch (Exception ex) {
            //appendMessageToVirtualConsoleLog(ex.toString(), null);
            //Logger.getLogger(Main_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        new dragAndDrop(mainPanel);
        this.setVisible(true);
        Thread.currentThread().setName("GUI");
        refresh();
    }

//    public static void addPluginFrameOpeningEventsToMainGUISystemCallPluginLists(SystemCallPlugin plugin, NamedFrameOpeningEvent[] foe) {
//        if (plugin == null || foe == null) {
//            return;
//        }
//        
//        ThemedJMenu tempMen = new ThemedJMenu();
//        
//        tempMen.setText(plugin.PLUGIN_NAME.replaceAll("_", " "));
//
//        for(int i = 0; i < foe.length; i ++){
//            ThemedJMenuItem tempIte = new ThemedJMenuItem();
//            tempIte.setText(foe[i].FRAME_NAME);
//            tempIte.addActionListener(foe[i].AL);
//            tempMen.add(tempIte);
//        }
//
//        systemCallFrameJMenu.add(tempMen);
//    }
    public static void reloadSystemCallPluginLists() {
        systemCallFrameJMenu.removeAll();
        registerSystemCallPluginsJMenu.removeAll();

        ArrayList<SystemCallPlugin> plugins = SystemCallPluginHandler.getRegisteredSystemCalls();
        for (SystemCallPlugin plugin : plugins) {

            {
                ThemedJMenu tempMenu = new ThemedJMenu();
                tempMenu.setText(plugin.DESCRIPTION.NAME);
                ThemedJMenuItem tempItem = new ThemedJMenuItem();
                tempItem.setText("Open SystemCall Plugin Info Frame");
                tempItem.addActionListener((ae) -> {
                    new SystemCallPluginInfoFrame(plugin);
                    //generate some plugin info frame from the plugin
                });
                tempMenu.add(tempItem);

                tempItem = new ThemedJMenuItem();
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
                    temp.forEach((t) -> {
                        generateJMenuFromNodeStructure(tempMenu, t);
                    });
                }

                registerSystemCallPluginsJMenu.add(tempMenu);
            }
            {
                ArrayList<Node<ActionListener>> temp = plugin.getFrameListeners();
                if (temp != null) {
                    ThemedJMenu tempMenu = new ThemedJMenu();
                    tempMenu.setText(plugin.DESCRIPTION.NAME.replaceAll("_", " "));
                    temp.forEach((t) -> {
                        generateJMenuFromNodeStructure(tempMenu, t);
                    });
                    systemCallFrameJMenu.add(tempMenu);
                }
            }
            {
                ArrayList<Node<ActionListener>> temp = plugin.getInternalExamples();
                if (temp != null) {
                    ThemedJMenu tempMenu = new ThemedJMenu();
                    tempMenu.setText(plugin.DESCRIPTION.NAME.replaceAll("_", " "));
                    temp.forEach((t) -> {
                        generateJMenuFromNodeStructure(tempMenu, t);
                    });
                    systemCallExampleJMenu.add(tempMenu);
                }
            }

        }
    }

    private static ThemedJMenu generateJMenuFromNodeStructure(ThemedJMenu menu, Node<ActionListener> node) {
        if (node == null) {
            return menu;
        }

        if (node.hasChildern()) { // if node has no childerent then add to menu as an item
            ThemedJMenuItem temp = new ThemedJMenuItem();
            temp.setText(node.name);
            temp.addActionListener((ActionListener) node.getData());
            menu.add(temp);
            return menu;
        }

        ThemedJMenu temp = new ThemedJMenu();
        temp.setText(node.name);
        node.getChildernAndDestroyParent().forEach((t) -> {
            generateJMenuFromNodeStructure(temp, t);
        });
        menu.add(temp);
        return menu;
    }

//    public static void addSystemCallPluginToPluginLists(){
//        
//    }
    private static ThemedJMenu generateJMenuFromFile(File file, ActionListener al) {
        if (file.isDirectory()) {
            ThemedJMenu jMenu = new ThemedJMenu();

            String name = file.getName();
            if (name.contains("‰")) {
                try {
                    name = name.split("‰")[2];
                } catch (Exception e) {

                }
            }

            jMenu.setText(name);
            jMenu.setName(file.getAbsolutePath());
            for (File f2 : file.listFiles()) {
                if (f2.isDirectory()) {
                    jMenu.add(generateJMenuFromFile(f2, al));
                } else {
                    if (f2.getName().split("\\.")[1].equals("mxn")) {
                        continue;
                    }
                    String namef2 = f2.getName().split("\\.")[0];
                    if (namef2.contains("‰")) {
                        try {
                            namef2 = namef2.split("‰")[2];
                        } catch (Exception e) {

                        }
                    }
                    ThemedJMenuItem jMenuItem = new ThemedJMenuItem();
                    jMenuItem.setText(namef2);
                    jMenuItem.setName(f2.getAbsolutePath());
                    jMenuItem.addActionListener(al);
                    jMenu.add(jMenuItem);
                }
            }
            return jMenu;
        }
        return null;
    }

    public static void refresh() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegisterGUI.updateVals();
                InstructionMemoryGUI.refresh();
                InstructionsRan.setText(Long.toString(Processor.getInstructionsRan()));
            }
        });
    }

    public static int confirmBox(String titleBar, String infoMessage) {
        return JOptionPane.showConfirmDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new org.parker.mips.gui.theme.components.ThemedJPanel1();
        jSplitPane1 = new javax.swing.JSplitPane();
        topPanel = new javax.swing.JPanel();
        topButtonBarPanel = new javax.swing.JPanel();
        compileButton = new org.parker.mips.gui.theme.components.ThemedJButton();
        startButton = new org.parker.mips.gui.theme.components.ThemedJToggleButton();
        stopButton = new org.parker.mips.gui.theme.components.ThemedJButton();
        singleStepButton = new org.parker.mips.gui.theme.components.ThemedJButton();
        memoryButton = new org.parker.mips.gui.theme.components.ThemedJButton();
        aboutButton = new javax.swing.JButton();
        InstructionsRan = new org.parker.mips.gui.theme.components.ThemedJLabel();
        resetButton = new org.parker.mips.gui.theme.components.ThemedJButton();
        jLabel2 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        lowerContentPanel = new javax.swing.JPanel();
        instructionMemory_GUI1 = new org.parker.mips.gui.InstructionMemoryGUI();
        register_GUI1 = new org.parker.mips.gui.RegisterGUI();
        aSM_GUI1 = new org.parker.mips.gui.ASM_GUI();
        midButtonSliderPanel = new javax.swing.JPanel();
        linkedButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        aboutLinkedFile = new javax.swing.JButton();
        enableBreak = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        delaySlider = new org.parker.mips.gui.theme.components.ThemedJSlider();
        delayLable = new org.parker.mips.gui.theme.components.ThemedJLabel();
        bottomPanel = new javax.swing.JPanel();
        logFrame = new org.parker.mips.Log();
        menuBar = new org.parker.mips.gui.theme.components.ThemedJMenuBar();
        fileMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        openMenuButton = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        exampleMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        newMenuButton = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        saveMenuButton = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        saveAsMenuButton = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        editMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        optionsMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        checkForUpdates = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        enableGUIUpdatingWhileRunningButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        logSystemMessagesButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        logMessagesButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        logWarningsButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        logErrorsButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        optionsButton = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        compilerMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        asciiChartButton = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        documentationButton = new org.parker.mips.gui.theme.components.ThemedJMenuItem();
        savePreProcessedFileButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        saveCompileInformationButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        runTimeMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        breakProgramOnRTEButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        adaptiveMemoryMenuButton = new org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem();
        systemCallPluginsJMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        systemCallFrameJMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        systemCallExampleJMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        registerSystemCallPluginsJMenu = new org.parker.mips.gui.theme.components.ThemedJMenu();
        loadPluginJMenuItem = new org.parker.mips.gui.theme.components.ThemedJMenuItem();

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

        compileButton.setText("Compile");
        compileButton.setFocusable(false);

        startButton.setText("Start");
        startButton.setFocusable(false);

        stopButton.setText("Stop");
        stopButton.setFocusable(false);

        singleStepButton.setText("Single Step");
        singleStepButton.setFocusable(false);

        memoryButton.setText("Memory");
        memoryButton.setFocusable(false);
        memoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memoryButtonActionPerformed(evt);
            }
        });

        aboutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/info.png"))); // NOI18N
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
                .addComponent(compileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singleStepButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(compileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(singleStepButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(memoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instructionMemory_GUI1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        lowerContentPanelLayout.setVerticalGroup(
            lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(instructionMemory_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(register_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
            .addComponent(aSM_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        midButtonSliderPanel.setOpaque(false);

        linkedButton.setText("Linked File");
        linkedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkedButtonActionPerformed(evt);
            }
        });

        aboutLinkedFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/info.png"))); // NOI18N
        aboutLinkedFile.setBorderPainted(false);
        aboutLinkedFile.setContentAreaFilled(false);
        aboutLinkedFile.setFocusPainted(false);
        aboutLinkedFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutLinkedFileActionPerformed(evt);
            }
        });

        enableBreak.setSelected(true);
        enableBreak.setText("Enable BreakPoints");

        delaySlider.setForeground(new java.awt.Color(0, 204, 153));
        delaySlider.setMaximum(1000);
        delaySlider.setValue(0);
        delaySlider.setFocusable(false);
        delaySlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                delaySliderMouseReleased(evt);
            }
        });

        delayLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        delayLable.setText("Delay");

        javax.swing.GroupLayout midButtonSliderPanelLayout = new javax.swing.GroupLayout(midButtonSliderPanel);
        midButtonSliderPanel.setLayout(midButtonSliderPanelLayout);
        midButtonSliderPanelLayout.setHorizontalGroup(
            midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midButtonSliderPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enableBreak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(midButtonSliderPanelLayout.createSequentialGroup()
                        .addComponent(linkedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aboutLinkedFile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(63, 63, 63)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delaySlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(delayLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(191, 191, 191))
        );
        midButtonSliderPanelLayout.setVerticalGroup(
            midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midButtonSliderPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(linkedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aboutLinkedFile, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delayLable, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        logSystemMessagesButton.setSelected(true);
        logSystemMessagesButton.setText("Log System Messages");
        optionsMenu.add(logSystemMessagesButton);

        logMessagesButton.setSelected(true);
        logMessagesButton.setText("Log Messages");
        optionsMenu.add(logMessagesButton);

        logWarningsButton.setSelected(true);
        logWarningsButton.setText("Log Warnings");
        logWarningsButton.setToolTipText("");
        optionsMenu.add(logWarningsButton);

        logErrorsButton.setSelected(true);
        logErrorsButton.setText("Log Errors");
        optionsMenu.add(logErrorsButton);

        optionsButton.setText("Options");
        optionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsButtonActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsButton);

        menuBar.add(optionsMenu);

        compilerMenu.setText("Compiler");

        asciiChartButton.setText("Ascii chart");
        asciiChartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asciiChartButtonActionPerformed(evt);
            }
        });
        compilerMenu.add(asciiChartButton);

        documentationButton.setText("Documentation");
        documentationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentationButtonActionPerformed(evt);
            }
        });
        compilerMenu.add(documentationButton);

        savePreProcessedFileButton.setSelected(true);
        savePreProcessedFileButton.setText("Save PreProcessed File");
        compilerMenu.add(savePreProcessedFileButton);

        saveCompileInformationButton.setSelected(true);
        saveCompileInformationButton.setText("Save CompileInformation");
        compilerMenu.add(saveCompileInformationButton);

        menuBar.add(compilerMenu);

        runTimeMenu.setText("RunTime");

        breakProgramOnRTEButton.setSelected(true);
        breakProgramOnRTEButton.setText("Break Program On RunTime Error");
        runTimeMenu.add(breakProgramOnRTEButton);

        adaptiveMemoryMenuButton.setText("Adaptive Memory");
        runTimeMenu.add(adaptiveMemoryMenuButton);

        menuBar.add(runTimeMenu);

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

    private void delaySliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delaySliderMouseReleased
        Processor.setDelay((int) Math.pow(delaySlider.getValue(), 3));
    }//GEN-LAST:event_delaySliderMouseReleased

    private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
        DesktopBrowser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS/blob/master/README.md");
    }//GEN-LAST:event_aboutButtonActionPerformed

    private void openMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuButtonActionPerformed
        if (FileHandler.openUserSelectedFile()) {
            refreshAll();
        }
    }//GEN-LAST:event_openMenuButtonActionPerformed

    private void memoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoryButtonActionPerformed
        new MemoryGUI();
    }//GEN-LAST:event_memoryButtonActionPerformed

    private void saveMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuButtonActionPerformed
        FileHandler.saveASMFileFromUserTextArea();
    }//GEN-LAST:event_saveMenuButtonActionPerformed

    private void saveAsMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuButtonActionPerformed
        FileHandler.saveAsASMFileFromUserTextArea();
    }//GEN-LAST:event_saveAsMenuButtonActionPerformed

    private void newMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuButtonActionPerformed
        if (FileHandler.newFile(true)) {
            refreshAll();
        }
    }//GEN-LAST:event_newMenuButtonActionPerformed

    private void asciiChartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asciiChartButtonActionPerformed
        new imageFrame("/images/asciiChart.bmp");
    }//GEN-LAST:event_asciiChartButtonActionPerformed

    private void aboutLinkedFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutLinkedFileActionPerformed
        Log.logCustomMessage("Linked Files can only be read by this program and are loaded from the file every time compiled. This is usful is another text editor is being used", true, false, true, Color.BLUE, null);
        //infoBox("Message", "Linked Files can only be read by this program \n and are loaded from the file every time compiled. \n This is usful is another text editor is being used");
    }//GEN-LAST:event_aboutLinkedFileActionPerformed

    private void linkedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkedButtonActionPerformed
        ASM_GUI.setEnable(!linkedButton.isSelected());
    }//GEN-LAST:event_linkedButtonActionPerformed

    private void checkForUpdatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkForUpdatesActionPerformed
        UpdateHandler.update();
//Browser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS");
    }//GEN-LAST:event_checkForUpdatesActionPerformed

    private void documentationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentationButtonActionPerformed

        //new HTMLFrame();
        try {
            DesktopBrowser.openLinkInBrowser(ResourceHandler.DOCUMENTATION_PATH + FileHandler.FILE_SEPERATOR + "index.html");
        } catch (Exception ex) {
            //Logger.getLogger(Main_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Browser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS/blob/master/MIPS%20documentation/MIPS%20Instructions-Traps-Registers.pdf");
    }//GEN-LAST:event_documentationButtonActionPerformed

    private void optionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsButtonActionPerformed
        new OptionsGUI();
    }//GEN-LAST:event_optionsButtonActionPerformed

    private void loadPluginJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadPluginJMenuItemActionPerformed
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());

        if (returnVal != 0) {
            return;
        }

        File chosenFile = fc.getSelectedFile();

        PluginLoader.loadPlugin(chosenFile);

    }//GEN-LAST:event_loadPluginJMenuItemActionPerformed

    public static void addCompileButtonListener(ActionListener al) {
        MainGUI.compileButton.addActionListener(al);
    }

    public static void addStartButtonListener(ActionListener al) {
        MainGUI.startButton.addActionListener(al);
    }

    public static void addStopButtonListener(ActionListener al) {
        MainGUI.stopButton.addActionListener(al);
    }

    public static void addSingleStepButtonListener(ActionListener al) {
        MainGUI.singleStepButton.addActionListener(al);
    }

    public static void addResetButtonListener(ActionListener al) {
        MainGUI.resetButton.addActionListener(al);
    }

    public static boolean isMemoryAdaptive() {
        return MainGUI.adaptiveMemoryMenuButton.isSelected();
    }

    public static boolean savePreProcessedFile() {
        return savePreProcessedFileButton.isSelected();
    }

    public static boolean saveCompilationInfo() {
        return saveCompileInformationButton.isSelected();
    }

    public static boolean breakOnRunTimeError() {
        return breakProgramOnRTEButton.isSelected();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static org.parker.mips.gui.theme.components.ThemedJLabel InstructionsRan;
    private static org.parker.mips.gui.ASM_GUI aSM_GUI1;
    private static javax.swing.JButton aboutButton;
    private static javax.swing.JButton aboutLinkedFile;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem adaptiveMemoryMenuButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem asciiChartButton;
    private static javax.swing.JPanel bottomPanel;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem breakProgramOnRTEButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem checkForUpdates;
    private static org.parker.mips.gui.theme.components.ThemedJButton compileButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenu compilerMenu;
    private static org.parker.mips.gui.theme.components.ThemedJLabel delayLable;
    private static org.parker.mips.gui.theme.components.ThemedJSlider delaySlider;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem documentationButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenu editMenu;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBox enableBreak;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem enableGUIUpdatingWhileRunningButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenu exampleMenu;
    private static org.parker.mips.gui.theme.components.ThemedJMenu fileMenu;
    private static org.parker.mips.gui.InstructionMemoryGUI instructionMemory_GUI1;
    private static org.parker.mips.gui.theme.components.ThemedJLabel jLabel2;
    private static javax.swing.JSplitPane jSplitPane1;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBox linkedButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem loadPluginJMenuItem;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem logErrorsButton;
    private static org.parker.mips.Log logFrame;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem logMessagesButton;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem logSystemMessagesButton;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem logWarningsButton;
    private static javax.swing.JPanel lowerContentPanel;
    private static org.parker.mips.gui.theme.components.ThemedJPanel1 mainPanel;
    private static org.parker.mips.gui.theme.components.ThemedJButton memoryButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenuBar menuBar;
    private static javax.swing.JPanel midButtonSliderPanel;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem newMenuButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem openMenuButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem optionsButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenu optionsMenu;
    private static org.parker.mips.gui.theme.components.ThemedJMenu registerSystemCallPluginsJMenu;
    private static org.parker.mips.gui.RegisterGUI register_GUI1;
    private static org.parker.mips.gui.theme.components.ThemedJButton resetButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenu runTimeMenu;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem saveAsMenuButton;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem saveCompileInformationButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenuItem saveMenuButton;
    private static org.parker.mips.gui.theme.components.ThemedJCheckBoxMenuItem savePreProcessedFileButton;
    private static org.parker.mips.gui.theme.components.ThemedJButton singleStepButton;
    private static org.parker.mips.gui.theme.components.ThemedJToggleButton startButton;
    private static org.parker.mips.gui.theme.components.ThemedJButton stopButton;
    private static org.parker.mips.gui.theme.components.ThemedJMenu systemCallExampleJMenu;
    private static org.parker.mips.gui.theme.components.ThemedJMenu systemCallFrameJMenu;
    private static org.parker.mips.gui.theme.components.ThemedJMenu systemCallPluginsJMenu;
    private static javax.swing.JPanel topButtonBarPanel;
    private static javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
