/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI;

import org.parker.mips.Compiler.ASMCompiler;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.parker.mips.FileHandler;
import org.parker.mips.Log;
import org.parker.mips.MIPS;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPluginFrame;
import org.parker.mips.Processor.Memory;
import org.parker.mips.Processor.Processor;
import org.parker.mips.ResourceHandler;
import org.parker.mips.UpdateHandler;

/**
 *
 * @author parke
 */
public class Main_GUI extends javax.swing.JFrame {

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
        if (false) {
            return;
        }
        Main_GUI.autoUpdate = true;
        autoUpdateThread = new Thread() {
            public void run() {
                while (isRunning()) {
                    Main_GUI.refresh();
                    if (!Processor.isRunning()) {
                        Main_GUI.stopAutoUpdate();
                        Main_GUI.refresh();
                        Main_GUI.startButton.setSelected(false);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {

                    }
                }
            }
        };
        autoUpdateThread.setName("autoUpdate");
        autoUpdateThread.start();
    }

    private static synchronized void stopAutoUpdate() {
        Main_GUI.autoUpdate = false;
    }

    public static Component getFrame() {
        return mainPanel;
    }

    public static void refreshAll() {
        ASM_GUI.setTextAreaFromASMFile();
        Memory.setMemory(FileHandler.getLoadedMXNFile());
        InstructionMemory_GUI.refreshValues();
        refresh();
    }

    /**
     * Creates new form Main_GUI
     */
    public Main_GUI() {
        setLookAndFeel();
        initComponents();

        addCompileButtonListener((ae) -> {
            Processor.stop();
            Processor.reset();
            ASMCompiler.compile();
        });

        addStartButtonListener((ae) -> {
            if (startButton.isSelected()) {
                Processor.start();
                Main_GUI.startAutoUpdate();
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
                        System.exit(0);
                    }
                } else {
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
                    if (FileHandler.loadExampleFile(new File(((JMenuItem) evt.getSource()).getName()))) {
                        ASMCompiler.compile();
                    }
                }
            };

            for (Component comp : generateJMenuFromFile(new File(ResourceHandler.EXAMPLES_PATH), al).getMenuComponents()) {
                Main_GUI.exampleMenu.add((JComponent) comp);
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

    public static void addSysCallFrameToList(SystemCallPlugin plugin, SystemCallPluginFrame frame) {
        if (plugin == null || frame == null) {
            return;
        }
        JMenuItem temp = new JMenuItem();

        temp.setText(plugin.PLUGIN_NAME);
        temp.addActionListener((ae) -> {
            frame.setVisible(true);
        });

        systemCallFrameJMenu.add(temp);
    }

    private static JMenu generateJMenuFromFile(File file, ActionListener al) {
        if (file.isDirectory()) {
            JMenu jMenu = new JMenu();

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
                    JMenuItem jMenuItem = new JMenuItem();
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

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public static synchronized void refresh() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Register_GUI.updateVals();
                InstructionMemory_GUI.refresh();
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

        mainPanel = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJPanel1();
        bottomPanel = new javax.swing.JPanel();
        logFrame = new org.parker.mips.Log();
        topPanel = new javax.swing.JPanel();
        topButtonBarPanel = new javax.swing.JPanel();
        compileButton = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton();
        startButton = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJToggleButton();
        stopButton = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton();
        singleStepButton = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton();
        memoryButton = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton();
        aboutButton = new javax.swing.JButton();
        InstructionsRan = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJLabel();
        resetButton = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton();
        jLabel2 = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJLabel();
        lowerContentPanel = new javax.swing.JPanel();
        aSM_GUI1 = new org.parker.mips.GUI.ASM_GUI();
        register_GUI1 = new org.parker.mips.GUI.Register_GUI();
        instructionMemory_GUI1 = new org.parker.mips.GUI.InstructionMemory_GUI();
        midButtonSliderPanel = new javax.swing.JPanel();
        linkedButton = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJCheckBox();
        aboutLinkedFile = new javax.swing.JButton();
        enableBreak = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJCheckBox();
        delaySlider = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJSlider();
        delayLable = new org.parker.mips.GUI.ThemedJFrameComponents.ThemedJLabel();
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
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        compilerMenu = new javax.swing.JMenu();
        asciiChartButton = new javax.swing.JMenuItem();
        documentationButton = new javax.swing.JMenuItem();
        savePreProcessedFileButton = new javax.swing.JCheckBoxMenuItem();
        saveCompileInformationButton = new javax.swing.JCheckBoxMenuItem();
        runTimeMenu = new javax.swing.JMenu();
        breakProgramOnRTEButton = new javax.swing.JCheckBoxMenuItem();
        adaptiveMemoryMenuButton = new javax.swing.JCheckBoxMenuItem();
        jMenu1 = new javax.swing.JMenu();
        systemCallFrameJMenu = new javax.swing.JMenu();
        registeredSystemCallPluginsButton = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("MIPS");
        setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
        );

        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new java.awt.Dimension(500, 762));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        lowerContentPanel.setOpaque(false);

        javax.swing.GroupLayout lowerContentPanelLayout = new javax.swing.GroupLayout(lowerContentPanel);
        lowerContentPanel.setLayout(lowerContentPanelLayout);
        lowerContentPanelLayout.setHorizontalGroup(
            lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowerContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aSM_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(register_GUI1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instructionMemory_GUI1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        lowerContentPanelLayout.setVerticalGroup(
            lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lowerContentPanelLayout.createSequentialGroup()
                .addGroup(lowerContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(aSM_GUI1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(register_GUI1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instructionMemory_GUI1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(midButtonSliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(delaySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
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
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lowerContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(midButtonSliderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(topButtonBarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addComponent(topButtonBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midButtonSliderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lowerContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
            .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Show System Messages");
        optionsMenu.add(jCheckBoxMenuItem1);

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

        runTimeMenu.setText("Run Time Options");

        breakProgramOnRTEButton.setSelected(true);
        breakProgramOnRTEButton.setText("Break Program On RunTime Error");
        runTimeMenu.add(breakProgramOnRTEButton);

        adaptiveMemoryMenuButton.setText("Adaptive Memory");
        runTimeMenu.add(adaptiveMemoryMenuButton);

        menuBar.add(runTimeMenu);

        jMenu1.setText("SystemCall Plugins");

        systemCallFrameJMenu.setText("SystemCall Frames");
        jMenu1.add(systemCallFrameJMenu);

        registeredSystemCallPluginsButton.setText("Registered SystemCall Plugins");
        jMenu1.add(registeredSystemCallPluginsButton);

        menuBar.add(jMenu1);

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
        new Memory_GUI();
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
            DesktopBrowser.openLinkInBrowser(ResourceHandler.DOCUMENTATION_PATH + "\\index.html");
        } catch (Exception ex) {
            //Logger.getLogger(Main_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Browser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS/blob/master/MIPS%20documentation/MIPS%20Instructions-Traps-Registers.pdf");
    }//GEN-LAST:event_documentationButtonActionPerformed

    public static void addCompileButtonListener(ActionListener al) {
        Main_GUI.compileButton.addActionListener(al);
    }

    public static void addStartButtonListener(ActionListener al) {
        Main_GUI.startButton.addActionListener(al);
    }

    public static void addStopButtonListener(ActionListener al) {
        Main_GUI.stopButton.addActionListener(al);
    }

    public static void addSingleStepButtonListener(ActionListener al) {
        Main_GUI.singleStepButton.addActionListener(al);
    }

    public static void addResetButtonListener(ActionListener al) {
        Main_GUI.resetButton.addActionListener(al);
    }

    public static boolean isMemoryAdaptive() {
        return Main_GUI.adaptiveMemoryMenuButton.isSelected();
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
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJLabel InstructionsRan;
    private static org.parker.mips.GUI.ASM_GUI aSM_GUI1;
    private static javax.swing.JButton aboutButton;
    private static javax.swing.JButton aboutLinkedFile;
    private static javax.swing.JCheckBoxMenuItem adaptiveMemoryMenuButton;
    private static javax.swing.JMenuItem asciiChartButton;
    private static javax.swing.JPanel bottomPanel;
    private static javax.swing.JCheckBoxMenuItem breakProgramOnRTEButton;
    private static javax.swing.JMenuItem checkForUpdates;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton compileButton;
    private static javax.swing.JMenu compilerMenu;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJLabel delayLable;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJSlider delaySlider;
    private static javax.swing.JMenuItem documentationButton;
    private static javax.swing.JMenu editMenu;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJCheckBox enableBreak;
    private static javax.swing.JMenu exampleMenu;
    private static javax.swing.JMenu fileMenu;
    private static org.parker.mips.GUI.InstructionMemory_GUI instructionMemory_GUI1;
    private static javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJLabel jLabel2;
    private static javax.swing.JMenu jMenu1;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJCheckBox linkedButton;
    private static org.parker.mips.Log logFrame;
    private static javax.swing.JPanel lowerContentPanel;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJPanel1 mainPanel;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton memoryButton;
    private static javax.swing.JMenuBar menuBar;
    private static javax.swing.JPanel midButtonSliderPanel;
    private static javax.swing.JMenuItem newMenuButton;
    private static javax.swing.JMenuItem openMenuButton;
    private static javax.swing.JMenu optionsMenu;
    private static org.parker.mips.GUI.Register_GUI register_GUI1;
    private static javax.swing.JMenuItem registeredSystemCallPluginsButton;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton resetButton;
    private static javax.swing.JMenu runTimeMenu;
    private static javax.swing.JMenuItem saveAsMenuButton;
    private static javax.swing.JCheckBoxMenuItem saveCompileInformationButton;
    private static javax.swing.JMenuItem saveMenuButton;
    private static javax.swing.JCheckBoxMenuItem savePreProcessedFileButton;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton singleStepButton;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJToggleButton startButton;
    private static org.parker.mips.GUI.ThemedJFrameComponents.ThemedJButton stopButton;
    private static javax.swing.JMenu systemCallFrameJMenu;
    private static javax.swing.JPanel topButtonBarPanel;
    private static javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
