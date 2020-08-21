/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Compiler.ASMCompiler;
import java.awt.Component;
import java.awt.Menu;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import mips.FileWriteReader;
import mips.Log;
import mips.processor.Processor;

/**
 *
 * @author parke
 */
public class Main_GUI extends javax.swing.JFrame {

    private static Thread autoUpdateThread;
    private static boolean autoUpdate;
    private static UserIO userIO = new UserIO();
    private static final Screen screen = new Screen();

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
        Main_GUI.autoUpdate = true;
        autoUpdateThread = new Thread() {
            public void run() {
                while (isRunning()) {
                    Main_GUI.refresh();
                    try {
                        Thread.sleep(100);
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

    public static void stop() {
        startButton.setSelected(false);
        stopAutoUpdate();
        Processor.stop();
    }

    public static Component getFrame() {
        return mainPanel;
    }

    public static void refreshAll() {
        ASM_GUI.setTextAreaFromASMFile();
        InstructionMemory_GUI.refreshValues();
        refresh();
    }

    public static void showScreen() {
        if (!screen.isVisible()) {
            screen.setVisible(true);
        }
    }

    /**
     * Creates new form Main_GUI
     */
    public Main_GUI() {
        setLookAndFeel();
        initComponents();

        makeTextAreaAutoScroll(virtualConsolLog);
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                if (!FileWriteReader.isASMFileSaved()) {
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

        ActionListener al = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileWriteReader.saveASMFile();
                Processor.stop();
                Processor.reset();
                FileWriteReader.loadExampleFile(new File(((JMenuItem) evt.getSource()).getName()));
                ASMCompiler.compile();
            }
        };

        try {
            for (Component comp : generateJMenuFromFile(new File(getClass().getResource("/Examples").toURI()), al).getMenuComponents()) {
                Main_GUI.exampleMenu.add((JComponent) comp);
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(Main_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        new dragAndDrop(mainPanel);
        this.setVisible(true);
        Thread.currentThread().setName("GUI");
        refresh();
    }

    private static JMenu generateJMenuFromFile(File file, ActionListener al) {
        if (file.isDirectory()) {
            JMenu jMenu = new JMenu();
            jMenu.setText(file.getName());
            jMenu.setName(file.getAbsolutePath());
            for (File f2 : file.listFiles()) {
                if (f2.isDirectory()) {
                    jMenu.add(generateJMenuFromFile(f2, al));
                } else {
                    JMenuItem jMenuItem = new JMenuItem();
                    jMenuItem.setText(f2.getName());
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

    public static void openUserIO() {
        if (!userIO.isVisible()) {
            userIO.setVisible(true);
            //userIO.requestFocus();
        }

    }

    public static void infoBox(String titleBar, String infoMessage) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
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

        mainPanel = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        buttonBarPanel = new javax.swing.JPanel();
        compileButton = new javax.swing.JButton();
        startButton = new javax.swing.JToggleButton();
        stopButton = new javax.swing.JButton();
        singleStepButton = new javax.swing.JButton();
        memoryButton = new javax.swing.JButton();
        userIOButton = new javax.swing.JButton();
        screenButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        InstructionsRan = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        asmGui = new GUI.ASM_GUI();
        registers = new GUI.Register_GUI();
        instructionMemory_GUI = new GUI.InstructionMemory_GUI();
        jPanel3 = new javax.swing.JPanel();
        linkedButton = new javax.swing.JCheckBox();
        aboutLinkedFile = new javax.swing.JButton();
        delaySlider = new javax.swing.JSlider();
        delayLable = new javax.swing.JLabel();
        enableBreak = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        virtualConsolLog = new javax.swing.JTextPane();
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
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        savePreProcessedFileButton = new javax.swing.JCheckBoxMenuItem();
        saveCompileInformationButton = new javax.swing.JCheckBoxMenuItem();
        breakProgramOnRTEButton = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("MIPS");

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel2.setPreferredSize(new java.awt.Dimension(500, 762));

        compileButton.setText("Compile");
        compileButton.setFocusable(false);
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileButtonActionPerformed(evt);
            }
        });

        startButton.setText("Start");
        startButton.setFocusable(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.setFocusable(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        singleStepButton.setText("Single Step");
        singleStepButton.setFocusable(false);
        singleStepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleStepButtonActionPerformed(evt);
            }
        });

        memoryButton.setText("Memory");
        memoryButton.setFocusable(false);
        memoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memoryButtonActionPerformed(evt);
            }
        });

        userIOButton.setText("User IO");
        userIOButton.setFocusable(false);
        userIOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userIOButtonActionPerformed(evt);
            }
        });

        screenButton.setText("Screen");
        screenButton.setFocusable(false);
        screenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                screenButtonActionPerformed(evt);
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
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonBarPanelLayout = new javax.swing.GroupLayout(buttonBarPanel);
        buttonBarPanel.setLayout(buttonBarPanelLayout);
        buttonBarPanelLayout.setHorizontalGroup(
            buttonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(compileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singleStepButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memoryButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userIOButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(screenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InstructionsRan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aboutButton))
        );
        buttonBarPanelLayout.setVerticalGroup(
            buttonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonBarPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aboutButton)
                    .addGroup(buttonBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(compileButton)
                        .addComponent(startButton)
                        .addComponent(stopButton)
                        .addComponent(singleStepButton)
                        .addComponent(memoryButton)
                        .addComponent(userIOButton)
                        .addComponent(screenButton)
                        .addComponent(resetButton)
                        .addComponent(InstructionsRan))))
        );

        registers.setMaximumSize(new java.awt.Dimension(200, 597));

        instructionMemory_GUI.setMaximumSize(new java.awt.Dimension(238, 267));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(asmGui, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instructionMemory_GUI, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(asmGui, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(registers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(instructionMemory_GUI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        linkedButton.setSelected(true);
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

        enableBreak.setSelected(true);
        enableBreak.setText("Enable BreakPoints");
        enableBreak.setFocusable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(linkedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aboutLinkedFile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(delayLable, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(delaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(123, 123, 123)
                .addComponent(enableBreak))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enableBreak)
                    .addComponent(delayLable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aboutLinkedFile, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(linkedButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonBarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(buttonBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jSplitPane2.setTopComponent(jPanel2);

        virtualConsolLog.setEditable(false);
        virtualConsolLog.setBackground(new java.awt.Color(153, 153, 153));
        virtualConsolLog.setContentType("HTML/plain"); // NOI18N
        virtualConsolLog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(virtualConsolLog);

        jSplitPane2.setBottomComponent(jScrollPane1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1061, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 937, Short.MAX_VALUE)
        );

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

        checkForUpdates.setText("Check for updates");
        checkForUpdates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkForUpdatesActionPerformed(evt);
            }
        });
        optionsMenu.add(checkForUpdates);

        menuBar.add(optionsMenu);

        jMenu1.setText("Compiler");

        jMenuItem1.setText("Ascii chart");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Documentation");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        savePreProcessedFileButton.setSelected(true);
        savePreProcessedFileButton.setText("Save PreProcessed File");
        jMenu1.add(savePreProcessedFileButton);

        saveCompileInformationButton.setSelected(true);
        saveCompileInformationButton.setText("Save CompileInformation");
        saveCompileInformationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCompileInformationButtonActionPerformed(evt);
            }
        });
        jMenu1.add(saveCompileInformationButton);

        breakProgramOnRTEButton.setSelected(true);
        breakProgramOnRTEButton.setText("Break Program On RunTime Error");
        jMenu1.add(breakProgramOnRTEButton);

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

    private void singleStepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleStepButtonActionPerformed
        if (!startButton.isSelected()) {
            
            Processor.runSingleStep();
        }
        refresh();
    }//GEN-LAST:event_singleStepButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if (startButton.isSelected()) {
            Processor.start();
            Main_GUI.startAutoUpdate();
        } else {
            stop();
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        stop();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void delaySliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delaySliderMouseReleased
        Processor.setDelay((int) Math.pow(delaySlider.getValue(), 3));
    }//GEN-LAST:event_delaySliderMouseReleased

    private void screenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_screenButtonActionPerformed
        screen.setVisible(!screen.isVisible());
    }//GEN-LAST:event_screenButtonActionPerformed

    private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
        Browser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS/blob/master/README.md");
    }//GEN-LAST:event_aboutButtonActionPerformed

    private void userIOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userIOButtonActionPerformed
        openUserIO();
    }//GEN-LAST:event_userIOButtonActionPerformed

    private void openMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuButtonActionPerformed
        FileWriteReader.openFilePopup();
    }//GEN-LAST:event_openMenuButtonActionPerformed

    private void compileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileButtonActionPerformed
        Processor.stop();
        Processor.reset();
        ASMCompiler.compile();
    }//GEN-LAST:event_compileButtonActionPerformed

    private void memoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoryButtonActionPerformed
        new Memory_GUI();
    }//GEN-LAST:event_memoryButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        stop();
        Processor.reset();
        UserIO.clearOutput();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void saveMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuButtonActionPerformed
        FileWriteReader.saveASMFile();
    }//GEN-LAST:event_saveMenuButtonActionPerformed

    private void saveAsMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuButtonActionPerformed
        FileWriteReader.saveAsASMFile();
    }//GEN-LAST:event_saveAsMenuButtonActionPerformed

    private void newMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuButtonActionPerformed
        FileWriteReader.newFile();
    }//GEN-LAST:event_newMenuButtonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new imageFrame("/images/asciiChart.bmp");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void aboutLinkedFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutLinkedFileActionPerformed
        Log.logWarning("Linked Files can only be read by this program and are loaded from the file every time compiled. This is usful is another text editor is being used");
        infoBox("Message", "Linked Files can only be read by this program \n and are loaded from the file every time compiled. \n This is usful is another text editor is being used");
    }//GEN-LAST:event_aboutLinkedFileActionPerformed

    private void linkedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkedButtonActionPerformed
        ASM_GUI.setEnable(!linkedButton.isSelected());
    }//GEN-LAST:event_linkedButtonActionPerformed

    private void checkForUpdatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkForUpdatesActionPerformed
        Browser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS");
    }//GEN-LAST:event_checkForUpdatesActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Browser.openLinkInBrowser("https://github.com/ParkerTenBroeck/MIPS/blob/master/MIPS%20documentation/MIPS%20Instructions-Traps-Registers.pdf");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void saveCompileInformationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCompileInformationButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveCompileInformationButtonActionPerformed

    public static boolean savePreProcessedFile() {
        return savePreProcessedFileButton.isSelected();
    }

    public static boolean saveCompilationInfo() {
        return saveCompileInformationButton.isSelected();
    }

    public static void appendMessageToVirtualConsoleLog(String message, SimpleAttributeSet att) {
        Document doc = virtualConsolLog.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), message + "\n", att);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public static boolean breakOnRunTimeError() {
        return breakProgramOnRTEButton.isSelected();
    }

    public static void makeTextAreaAutoScroll(JTextPane textArea) {

        // Get the text area's scroll pane :
        final JScrollPane scrollPane = (JScrollPane) (textArea.getParent().getParent());

        // Disable the auto scroll :
        ((DefaultCaret) textArea.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        // Add a listener to the vertical scroll bar :
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            private int _val = 0;
            private int _ext = 0;
            private int _max = 0;

            private final BoundedRangeModel _model = scrollPane.getVerticalScrollBar().getModel();

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                // Get the new max :
                int newMax = _model.getMaximum();

                // If the new max has changed and if we were scrolled to bottom :
                if (newMax != _max && (_val + _ext == _max)) {

                    // Scroll to bottom :
                    _model.setValue(_model.getMaximum() - _model.getExtent());
                }

                // Save the new values :
                _val = _model.getValue();
                _ext = _model.getExtent();
                _max = _model.getMaximum();
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel InstructionsRan;
    private static javax.swing.JButton aboutButton;
    private static javax.swing.JButton aboutLinkedFile;
    private static GUI.ASM_GUI asmGui;
    private static javax.swing.JCheckBoxMenuItem breakProgramOnRTEButton;
    private static javax.swing.JPanel buttonBarPanel;
    private static javax.swing.JMenuItem checkForUpdates;
    private static javax.swing.JButton compileButton;
    private static javax.swing.JLabel delayLable;
    private static javax.swing.JSlider delaySlider;
    private static javax.swing.JMenu editMenu;
    private static javax.swing.JCheckBox enableBreak;
    private static javax.swing.JMenu exampleMenu;
    private static javax.swing.JMenu fileMenu;
    private static GUI.InstructionMemory_GUI instructionMemory_GUI;
    private static javax.swing.JMenu jMenu1;
    private static javax.swing.JMenuItem jMenuItem1;
    private static javax.swing.JMenuItem jMenuItem2;
    private static javax.swing.JPanel jPanel2;
    private static javax.swing.JPanel jPanel3;
    private static javax.swing.JPanel jPanel4;
    private static javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JSplitPane jSplitPane2;
    private static javax.swing.JCheckBox linkedButton;
    private static javax.swing.JPanel mainPanel;
    private static javax.swing.JButton memoryButton;
    private static javax.swing.JMenuBar menuBar;
    private static javax.swing.JMenuItem newMenuButton;
    private static javax.swing.JMenuItem openMenuButton;
    private static javax.swing.JMenu optionsMenu;
    private static GUI.Register_GUI registers;
    private static javax.swing.JButton resetButton;
    private static javax.swing.JMenuItem saveAsMenuButton;
    private static javax.swing.JCheckBoxMenuItem saveCompileInformationButton;
    private static javax.swing.JMenuItem saveMenuButton;
    private static javax.swing.JCheckBoxMenuItem savePreProcessedFileButton;
    private static javax.swing.JButton screenButton;
    private static javax.swing.JButton singleStepButton;
    private static javax.swing.JToggleButton startButton;
    private static javax.swing.JButton stopButton;
    private static javax.swing.JButton userIOButton;
    private static javax.swing.JTextPane virtualConsolLog;
    // End of variables declaration//GEN-END:variables
}
