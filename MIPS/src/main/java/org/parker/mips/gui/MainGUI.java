/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.parker.mips.*;
import org.parker.mips.assembler.Assembler;
import org.parker.mips.gui.userpanes.editor.Editor;
import org.parker.mips.gui.userpanes.editor.EditorHandler;
import org.parker.mips.gui.userpanes.hexeditor.MemoryEditorUserPane;
import org.parker.mips.gui.userpanes.editor.rsyntax.FormattedTextEditor;
import org.parker.mips.gui.theme.ThemeHandler;
import org.parker.mips.log.LogFrame;
import org.parker.mips.plugin.PluginLoader;
import org.parker.mips.plugin.syscall.SystemCallPlugin;
import org.parker.mips.plugin.syscall.SystemCallPlugin.Node;
import org.parker.mips.plugin.syscall.SystemCallPluginHandler;
import org.parker.mips.preferences.Preference;
import org.parker.mips.preferences.Preferences;
import org.parker.mips.emulator.Memory;
import org.parker.mips.emulator.Emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class MainGUI extends javax.swing.JFrame {

    private static Thread autoUpdateThread;
    private static boolean auteUpdateRunning;
    private static MainGUI instance;

    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());

    private static final Preferences systemPrefs = Preferences.ROOT_NODE.getNode("system");

    public static synchronized boolean isRunning() {
        return auteUpdateRunning;
    }

    public static synchronized boolean canBreak() {
        return enableBreak.isSelected();
    }

    private static synchronized void startAutoUpdate() {
        MainGUI.startButton.setSelected(true);
        MainGUI.auteUpdateRunning = true;
        if (!(Boolean)systemPrefs.getNode("gui").getPreference("enableGUIAutoUpdateWhileRunning", true)) {
            return;
        }
        autoUpdateThread = new Thread() {
            public void run() {

                Preference<Boolean> autoUpdate = systemPrefs.getNode("gui").getRawPreference("enableGUIAutoUpdateWhileRunning", true);
                Preference<Integer> refreshTime = systemPrefs.getNode("gui").getRawPreference("GUIAutoUpdateRefreshTime", 100);

                while (auteUpdateRunning && autoUpdate.val()) {
                    MainGUI.refresh();
                    try {
                        Thread.sleep(refreshTime.val());
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
        MainGUI.auteUpdateRunning = false;
        MainGUI.refresh();
    }

    public static Component getFrame() {
        return mainPanel;
    }

    public static void refreshAll() {
        Memory.reloadMemory();
        refresh();
    }

    /**
     * Creates new form Main_GUI
     */
    public MainGUI() {
        initComponents();

        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("images/logo4.png");
            ImageIcon icon = new ImageIcon(url);
            this.setIconImage(icon.getImage());

            aboutButton.setIcon(new FlatSVGIcon("images/informationDialog.svg", (int) (aboutButton.getWidth() / 1.5), (int) (aboutButton.getHeight() / 1.5)));
            //aboutLinkedFile.setIcon(new FlatSVGIcon("images/informationDialog.svg", (int) (aboutLinkedFile.getWidth() / 1.5), (int) (aboutLinkedFile.getHeight() / 1.5)));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load Icon", e);
        }

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        systemPrefs.getNode("emulator/runtime").getRawPreference("enableBreakPoints",true).LinkJButton(this, enableBreak);
        //OptionsHandler.linkedFile.LinkJButton(this, linkedButton);

        systemPrefs.getNode("assembler").getRawPreference("saveCompilationInfo",false).LinkJButton(this, saveCompileInformationButton);
        systemPrefs.getNode("assembler").getRawPreference("savePreProcessedFile",false).LinkJButton(this, savePreProcessedFileButton);

        systemPrefs.getNode("emulator/runtime").getRawPreference("breakOnRunTimeError",true).LinkJButton(this, breakProgramOnRTEButton);
        systemPrefs.getNode("emulator/runtime").getRawPreference("adaptiveMemory",false).LinkJButton(this, adaptiveMemoryMenuButton);

        systemPrefs.getNode("gui").getRawPreference("enableGUIAutoUpdateWhileRunning", true).LinkJButton(this,enableGUIUpdatingWhileRunningButton);
        //OptionsHandler.logSystemMessages.LinkJButton(this, logSystemMessagesButton);
        //OptionsHandler.logMessages.LinkJButton(this, logMessagesButton);
        //OptionsHandler.logWarnings.LinkJButton(this, logWarningsButton);
        //OptionsHandler.logErrors.LinkJButton(this, logErrorsButton);

        addCompileButtonListener((ae) -> {
            LOGGER.log(Level.FINER, "Compile Button Action Preformed");
            Emulator.reset();
            EditorHandler.saveAll();
            Assembler.assembleDefault();
        });

        addStartButtonListener((ae) -> {
            LOGGER.log(Level.FINER, "Start Processor Button Action Preformed");
            if (startButton.isSelected()) {
                Emulator.start();
                MainGUI.startAutoUpdate();
            } else {
                Emulator.stop();
            }
        });

        addStopButtonListener((ae) -> {
            LOGGER.log(Level.FINER, "Stop Processor Button Action Preformed");
            Emulator.stop();
        });

        addSingleStepButtonListener((ae) -> {
            LOGGER.log(Level.FINER, "Single Step Processor Button Action Preformed");
            if (!startButton.isSelected()) {
                Emulator.runSingleStep();
            }
            refresh();
        });

        addResetButtonListener((ae) -> {
            LOGGER.log(Level.FINER, "Reset Processor Button Action Preformed");
            Emulator.reset();
        });

        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
        LOGGER.log(Level.FINER, "Main Window Exit Action Preformed");

                if (!EditorHandler.isAllSaved()) {
                    int confirm = createWarningQuestion("Exit Confirmation", "You have unsaved work would you like to save before continuing?");

                    if (confirm == JOptionPane.CANCEL_OPTION) {

                    }
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (!EditorHandler.saveAll()) {
                            return;
                        }
                        Preferences.savePreferencesToDefaultFile();
                        System.exit(0);
                    }
                    if (confirm == JOptionPane.NO_OPTION) {
                        Preferences.savePreferencesToDefaultFile();
                        System.exit(0);
                    }
                } else {
                    Preferences.savePreferencesToDefaultFile();
                    System.exit(0);
                }

            }
        };
        this.addWindowListener(exitListener);

        //Generates examples
        try {

            ActionListener al = new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    Emulator.stop();
                    Emulator.reset();
                    //if (FileHandler.loadExampleFile(new File(((ThemedJMenuItem) evt.getSource()).getName()))) {
                    //new FormattedTextEditor();
                    File file = new File(((javax.swing.JMenuItem) evt.getSource()).getName());
                    Editor.createEditor(FileUtils.loadFileAsByteArraySafe(file), FileUtils.removeExtension(file.getName()), FormattedTextEditor.class);
                    Assembler.assembleDefault();
                    //}
                }
            };

            for (Component comp : generateJMenuFromFile(new File(ResourceHandler.EXAMPLES_PATH), al).getMenuComponents()) {
                MainGUI.exampleMenu.add((JComponent) comp);
            }

        } catch (Exception ex) {
        }

        new dragAndDrop(mainPanel);
        Thread.currentThread().setName("GUI");
        refresh();

        if (instance != null) {
            instance.dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));
        }
        ThemeHandler.updateUI();
        this.setVisible(true);
        instance = this;
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
                    temp.forEach((t) -> {
                        generateJMenuFromNodeStructure(tempMenu, t);
                    });
                }

                registerSystemCallPluginsJMenu.add(tempMenu);
            }
            {
                ArrayList<Node<ActionListener>> temp = plugin.getFrameListeners();
                if (temp != null) {
                	javax.swing.JMenu tempMenu = new javax.swing.JMenu();
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
                	javax.swing.JMenu tempMenu = new javax.swing.JMenu();
                    tempMenu.setText(plugin.DESCRIPTION.NAME.replaceAll("_", " "));
                    temp.forEach((t) -> {
                        generateJMenuFromNodeStructure(tempMenu, t);
                    });
                    systemCallExampleJMenu.add(tempMenu);
                }
            }

        }
    }

    private static javax.swing.JMenu generateJMenuFromNodeStructure(javax.swing.JMenu menu, Node<ActionListener> node) {
        if (node == null) {
            return menu;
        }

        if (node.hasChildern()) { // if node has no childerent then add to menu as an item
        	javax.swing.JMenuItem temp = new javax.swing.JMenuItem();
            temp.setText(node.name);
            temp.addActionListener((ActionListener) node.getData());
            menu.add(temp);
            return menu;
        }

        javax.swing.JMenu temp = new javax.swing.JMenu();
        temp.setText(node.name);
        node.getChildernAndDestroyParent().forEach((t) -> {
            generateJMenuFromNodeStructure(temp, t);
        });
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
                    javax.swing.JMenuItem jMenuItem = new javax.swing.JMenuItem();
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
        javax.swing.SwingUtilities.invokeLater(() -> {
            register_GUI1.updateVals();
            //InstructionMemoryGUI.refresh();
            InstructionsRan.setText(Long.toString(Emulator.getInstructionsRan()));
            UserPaneTabbedPane.updateOpenUserPanes();
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        topPanel = new javax.swing.JPanel();
        topButtonBarPanel = new javax.swing.JPanel();
        compileButton = new javax.swing.JButton();
        startButton = new javax.swing.JToggleButton();
        stopButton = new javax.swing.JButton();
        singleStepButton = new javax.swing.JButton();
        memoryButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        InstructionsRan = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lowerContentPanel = new javax.swing.JPanel();
        //instructionMemory_GUI1 = new org.parker.mips.gui.InstructionMemoryGUI();
        register_GUI1 = new org.parker.mips.gui.RegisterGUI();
        aSM_GUI1 = new UserPaneTabbedPane();
        midButtonSliderPanel = new javax.swing.JPanel();
        //linkedButton = new javax.swing.JCheckBox();
        //aboutLinkedFile = new javax.swing.JButton();
        enableBreak = new javax.swing.JCheckBox();
        delaySlider = new javax.swing.JSlider();
        delayLable = new javax.swing.JLabel();
        bottomPanel = new javax.swing.JPanel();
        logFrame = new LogFrame();
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
        //logSystemMessagesButton = new javax.swing.JCheckBoxMenuItem();
        //logMessagesButton = new javax.swing.JCheckBoxMenuItem();
        //logWarningsButton = new javax.swing.JCheckBoxMenuItem();
        //logErrorsButton = new javax.swing.JCheckBoxMenuItem();
        optionsButton = new javax.swing.JMenuItem();
        assemblerMenu = new javax.swing.JMenu();
        asciiChartButton = new javax.swing.JMenuItem();
        documentationButton = new javax.swing.JMenuItem();
        savePreProcessedFileButton = new javax.swing.JCheckBoxMenuItem();
        saveCompileInformationButton = new javax.swing.JCheckBoxMenuItem();
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

        delaySlider.setForeground(new java.awt.Color(0, 204, 153));
        delaySlider.setMaximum(1000);
        delaySlider.setValue(0);
        delaySlider.setFocusable(false);

        delayLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        delayLable.setText("Instruction Delay: " + delaySlider.getValue() + "ns");
        delaySlider.addChangeListener(cl ->{
        	long trueValue = (long) Math.pow(delaySlider.getValue(), 3);
        	delayLable.setText("Instruction Delay: " + trueValue + "ns");	
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
                    .addComponent(delayLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(191, 191, 191))
        );
        midButtonSliderPanelLayout.setVerticalGroup(
            midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midButtonSliderPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    //.addComponent(linkedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    //.addComponent(aboutLinkedFile, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        saveCompileInformationButton.setSelected(true);
        saveCompileInformationButton.setText("Save CompileInformation");
        assemblerMenu.add(saveCompileInformationButton);

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
                saveMenuButtonActionPerformed(evt);
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
    }//GEN-LAST:event_aboutButtonActionPerformed

    private void openMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuButtonActionPerformed
        JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());

        if (returnVal != JFileChooser.FILES_AND_DIRECTORIES) {
            File chosenFile = fc.getSelectedFile();
            Editor.loadFileIntoEditor(chosenFile);
        }
    }//GEN-LAST:event_openMenuButtonActionPerformed

    private void memoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoryButtonActionPerformed
        //new MemoryGUI();

        UserPaneTabbedPane.addEditor(new MemoryEditorUserPane());
    }//GEN-LAST:event_memoryButtonActionPerformed

    private void saveMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuButtonActionPerformed
        EditorHandler.saveLastFocused();
    }//GEN-LAST:event_saveMenuButtonActionPerformed

    private void saveAsMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuButtonActionPerformed
        EditorHandler.saveAsLastFocused();
    }//GEN-LAST:event_saveAsMenuButtonActionPerformed
    
    private void saveMemoryButtonActionPreformed(java.awt.event.ActionEvent evt) {
        Emulator.stop();
    	JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());
        if (returnVal == fc.FILES_ONLY) {
            FileUtils.saveByteArrayToFileSafe(Memory.getMemory(), fc.getSelectedFile());
        }
    }
    
    private void loadMemoryButtonActionPreformed(java.awt.event.ActionEvent evt) {
    	JFileChooser fc = new JFileChooser(ResourceHandler.DEFAULT_PROJECTS_PATH);
        int returnVal = fc.showOpenDialog(MainGUI.getFrame());
        if (returnVal == fc.FILES_ONLY) {
            Memory.setMemory(FileUtils.loadFileAsByteArraySafe(fc.getSelectedFile()));
        }
        Emulator.stop();
        refreshAll();
    }

    private void newMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuButtonActionPerformed
        //new FormattedTextEditor();
        Editor.createEditor();
    }//GEN-LAST:event_newMenuButtonActionPerformed

    private void asciiChartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asciiChartButtonActionPerformed
        new imageFrame("/images/asciiChart.bmp");
    }//GEN-LAST:event_asciiChartButtonActionPerformed

    private void aboutLinkedFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutLinkedFileActionPerformed
        LOGGER.log(Level.INFO, "Linked Files can only be read by this program and are loaded from the file every time compiled. This is usful is another text editor is being used");
        //LogFrame.logCustomMessage(, true, false, true, Color.BLUE, null);
        //infoBox("Message", "Linked Files can only be read by this program \n and are loaded from the file every time compiled. \n This is usful is another text editor is being used");
    }//GEN-LAST:event_aboutLinkedFileActionPerformed

    private void linkedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkedButtonActionPerformed
        //ASM_GUI.setEnable(!linkedButton.isSelected());
    }//GEN-LAST:event_linkedButtonActionPerformed

    private void checkForUpdatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkForUpdatesActionPerformed
        UpdateHandler.update();
    }//GEN-LAST:event_checkForUpdatesActionPerformed

    private void documentationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentationButtonActionPerformed
        try {
            DesktopBrowser.openLinkInBrowser(ResourceHandler.DOCUMENTATION_PATH + FileUtils.FILE_SEPARATOR + "index.html");
        } catch (Exception ex) {
        }
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

    //messages (ok)
    public static void createPlaneMessage(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    public static void createPlaneInfo(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    public static void createWarningMessage(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, null, null);
    }

    public static void createErrorMessage(String title, String message) {
        createCustomOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, null, null, null);
    }

    //choices (yes, no)
    public static int createPlaneChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    public static int createInfoChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    public static int createWarningChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
    }

    public static int createErrorChoice(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
    }

    //Question (yes, no, cancel)
    public static int createPlaneQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    public static int createInfoQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    public static int createWarningQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
    }

    public static int createErrorQuestion(String title, String message) {
        return createCustomOptionDialog(title, message, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
    }

    //custom
    public static int createCustomOptionDialog(String title, String message, int i, int ii) {
        return createCustomOptionDialog(title, message, i, ii, null, null, null);
    }

    public static int createCustomOptionDialog(String title, String message, int i, int ii, Icon icon) {
        return createCustomOptionDialog(title, message, i, ii, icon, null, null);
    }

    public static int createCustomOptionDialog(String title, String message, int i, int ii, Icon icon, Object[] objects, Object object) {
        return JOptionPane.showOptionDialog(instance, message, title, i, ii, icon, objects, object);
    }

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel InstructionsRan;
    private static UserPaneTabbedPane aSM_GUI1;
    private static javax.swing.JButton aboutButton;
    private static javax.swing.JCheckBoxMenuItem adaptiveMemoryMenuButton;
    private static javax.swing.JMenuItem asciiChartButton;
    private static javax.swing.JPanel bottomPanel;
    private static javax.swing.JCheckBoxMenuItem breakProgramOnRTEButton;
    private static javax.swing.JMenuItem checkForUpdates;
    private static javax.swing.JButton compileButton;
    private static javax.swing.JMenu assemblerMenu;
    private static javax.swing.JLabel delayLable;
    private static javax.swing.JSlider delaySlider;
    private static javax.swing.JMenuItem documentationButton;
    private static javax.swing.JMenu editMenu;
    private static javax.swing.JCheckBox enableBreak;
    private static javax.swing.JCheckBoxMenuItem enableGUIUpdatingWhileRunningButton;
    private static javax.swing.JMenu exampleMenu;
    private static javax.swing.JMenu fileMenu;
    //private static org.parker.mips.gui.InstructionMemoryGUI instructionMemory_GUI1;
    private static javax.swing.JLabel jLabel2;
    private static javax.swing.JSplitPane jSplitPane1;
    //private static javax.swing.JCheckBox linkedButton;
    private static javax.swing.JMenuItem loadPluginJMenuItem;
    //private static javax.swing.JCheckBoxMenuItem logErrorsButton;
    private static LogFrame logFrame;
    //private static javax.swing.JCheckBoxMenuItem logMessagesButton;
    //private static javax.swing.JCheckBoxMenuItem logSystemMessagesButton;
    //private static javax.swing.JCheckBoxMenuItem logWarningsButton;
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
    private static org.parker.mips.gui.RegisterGUI register_GUI1;
    private static javax.swing.JButton resetButton;
    private static javax.swing.JMenu processorMenu;
    private static javax.swing.JMenuItem saveMemoryButton;
    private static javax.swing.JMenuItem loadMemoryButton;
    private static javax.swing.JMenuItem saveAsMenuButton;
    private static javax.swing.JCheckBoxMenuItem saveCompileInformationButton;
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
