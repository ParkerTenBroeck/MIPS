/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import org.parker.mips.gui.theme.ThemeHandler;
import org.parker.mips.OptionsHandler;
import org.parker.mips.ResourceHandler;

/**
 *
 * @author parke
 */
public class OptionsGUI extends javax.swing.JFrame {

    /**
     * Creates new form OptionsGUI
     */
    public OptionsGUI() {
        initComponents();
        this.setVisible(true);
        this.setTitle("Options");

        //linking all of the components to the linked OPTIONS
        //General
        //logging
        this.logSystemMessagesButton.setSelected(OptionsHandler.logSystemMessages);
        this.logMessagesButton.setSelected(OptionsHandler.logMessages);
        this.logWarningsButton.setSelected(OptionsHandler.logWarnings);
        this.logErrorsButton.setSelected(OptionsHandler.logErrors);

        //GUI options
        this.enableAutoGUIUpdatesWhileRuning.setSelected(OptionsHandler.enableGUIAutoUpdateWhileRunning);
        this.guiUpdateTimeSlider.setValue(OptionsHandler.GUIAutoUpdateRefreshTime);

        //Compiler
        this.saveCleanedFileButton.setSelected(OptionsHandler.saveCleanedFile);
        this.savePreProcessorFileButton.setSelected(OptionsHandler.savePreProcessedFile);
        this.saveCompilerInfoFileButton.setSelected(OptionsHandler.saveCompilationInfo);
        this.linkedFileButton.setSelected(OptionsHandler.linkedFile);

        //PreProcessor
        this.includeRegDefButton.setSelected(OptionsHandler.includeRegDef);
        this.includeSysCallDefButton.setSelected(OptionsHandler.includeSysCallDef);

        //Processor
        //Run Time
        this.breakOnRunTimeErrorButton.setSelected(OptionsHandler.breakOnRunTimeError);
        this.adaptiveMemoryButton.setSelected(OptionsHandler.adaptiveMemory);
        this.enableBreakPointsButton.setSelected(OptionsHandler.enableBreakPoints);

        //Non RunTime
        this.reloadMemoryOnResetButton.setSelected(OptionsHandler.reloadMemoryOnReset);

        //System Calls
        this.logSystemCallMessagesButton.setSelected(OptionsHandler.logSystemCallMessages);
        this.resetProcessorOnTrap0Button.setSelected(OptionsHandler.resetProcessorOnTrap0);

        this.loadOptionsButton.addActionListener((ae) -> {
            JFileChooser fc = ResourceHandler.createFileChooser(ResourceHandler.USER_SAVED_CONFIG_PATH);
            int val = ResourceHandler.openFileChooser(fc);

            if (JFileChooser.APPROVE_OPTION == val) {
                OptionsHandler.readOptionsFromCustomFile(fc.getSelectedFile());
            }
        });

        this.saveCurrentOptionsButton.addActionListener((ae) -> {
            JFileChooser fc = ResourceHandler.createFileChooser(ResourceHandler.USER_SAVED_CONFIG_PATH);
            int val = ResourceHandler.openFileChooser(fc);

            if (JFileChooser.APPROVE_OPTION == val) {
                OptionsHandler.saveOptionsToCustomFile(fc.getSelectedFile());
            }
        });

        {
            File file = new File(ResourceHandler.GUI_THEMES);
            File[] files = file.listFiles();
            String[] names = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                names[i] = files[i].getName().split("\\.")[0];
            }
            this.guiThemeComboBox.setModel(new DefaultComboBoxModel(names));
            this.guiThemeComboBox.setSelectedItem(OptionsHandler.currentGUITheme.value);
            
            this.guiThemeComboBox.addActionListener((ae) -> {
                //System.err.println("asdasdasdasdasda");
                String name = (String) guiThemeComboBox.getSelectedItem();

                if (!name.equals(OptionsHandler.currentGUITheme.value)) {
                    OptionsHandler.currentGUITheme.value = name;
                    ThemeHandler.loadCurrentTheme();
                }
                //ThemeHandler.readThemeFromThemeName();
            });
        }

        {
            File file = new File(ResourceHandler.SYNTAX_THEMES);
            File[] files = file.listFiles();
            String[] names = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                names[i] = files[i].getName().split("\\.")[0];
            }
            this.syntaxThemeComboBox.setModel(new DefaultComboBoxModel(names));
            this.syntaxThemeComboBox.setSelectedItem(OptionsHandler.currentSyntaxTheme.value);
            
            this.syntaxThemeComboBox.addActionListener((ae) -> {
                //System.err.println("asdasdasdasdasda");
                String name = (String) syntaxThemeComboBox.getSelectedItem();

                if (!name.equals(OptionsHandler.currentSyntaxTheme.value)) {
                    OptionsHandler.currentSyntaxTheme.value = name;
                    ASM_GUI.loadCurrentTheme();
                }
                //ThemeHandler.readThemeFromThemeName();
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        themedJTabbedPane1 = new org.parker.mips.gui.theme.components.ThemedJTabbedPane();
        themedJPanel11 = new org.parker.mips.gui.theme.components.ThemedJPanel1();
        themedJLabel2 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        logSystemMessagesButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        logMessagesButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        logWarningsButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        logErrorsButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        themedJLabel3 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        enableAutoGUIUpdatesWhileRuning = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        themedJLabel5 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        saveCurrentOptionsButton = new org.parker.mips.gui.theme.components.ThemedJButton();
        jSeparator1 = new javax.swing.JSeparator();
        loadOptionsButton = new org.parker.mips.gui.theme.components.ThemedJButton();
        guiUpdateTimeSlider = new org.parker.mips.gui.theme.components.ThemedJSlider();
        themedJLabel8 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        themedJPanel12 = new org.parker.mips.gui.theme.components.ThemedJPanel1();
        breakOnRunTimeErrorButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        themedJLabel1 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        adaptiveMemoryButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        enableBreakPointsButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        themedJLabel10 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        reloadMemoryOnResetButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        themedJPanel13 = new org.parker.mips.gui.theme.components.ThemedJPanel1();
        themedJLabel6 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        savePreProcessorFileButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        saveCompilerInfoFileButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        saveCleanedFileButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        themedJLabel9 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        includeRegDefButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        includeSysCallDefButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        linkedFileButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        themedJPanel15 = new org.parker.mips.gui.theme.components.ThemedJPanel1();
        themedJLabel7 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        logSystemCallMessagesButton = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        resetProcessorOnTrap0Button = new org.parker.mips.gui.theme.components.ThemedJCheckBox();
        themedJPanel14 = new org.parker.mips.gui.theme.components.ThemedJPanel1();
        themedJLabel4 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        jSeparator4 = new javax.swing.JSeparator();
        themedJLabel11 = new org.parker.mips.gui.theme.components.ThemedJLabel();
        syntaxThemeComboBox = new org.parker.mips.gui.theme.components.ThemedJComboBox();
        guiThemeComboBox = new org.parker.mips.gui.theme.components.ThemedJComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        themedJTabbedPane1.setBackground(new java.awt.Color(51, 51, 51));

        themedJLabel2.setText("Log Options");

        logSystemMessagesButton.setText("Log System Messages");

        logMessagesButton.setText("Log Messages");

        logWarningsButton.setText("Log Warnigs");

        logErrorsButton.setText("Log Errors");

        themedJLabel3.setText("GUI Options");

        enableAutoGUIUpdatesWhileRuning.setText("Enable Auto GUI Updates While Runing");

        themedJLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        themedJLabel5.setText("Options");

        saveCurrentOptionsButton.setText("Save Current Options");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        loadOptionsButton.setText("Load Options");

        guiUpdateTimeSlider.setMaximum(500);
        guiUpdateTimeSlider.setMinimum(1);
        guiUpdateTimeSlider.setValue(100);

        themedJLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        themedJLabel8.setText("Update Time 1-500");

        javax.swing.GroupLayout themedJPanel11Layout = new javax.swing.GroupLayout(themedJPanel11);
        themedJPanel11.setLayout(themedJPanel11Layout);
        themedJPanel11Layout.setHorizontalGroup(
            themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themedJPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themedJLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logSystemMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logErrorsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logWarningsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themedJLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enableAutoGUIUpdatesWhileRuning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guiUpdateTimeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(themedJLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themedJLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveCurrentOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(146, 146, 146))
        );
        themedJPanel11Layout.setVerticalGroup(
            themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themedJPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themedJLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themedJLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(themedJPanel11Layout.createSequentialGroup()
                        .addComponent(logSystemMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logWarningsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logErrorsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(themedJLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enableAutoGUIUpdatesWhileRuning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themedJPanel11Layout.createSequentialGroup()
                        .addComponent(loadOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveCurrentOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(themedJLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guiUpdateTimeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );

        themedJTabbedPane1.addTab("General", themedJPanel11);

        breakOnRunTimeErrorButton.setText("Break On RunTime Error");

        themedJLabel1.setText("RunTime Options");

        adaptiveMemoryButton.setText("Adaptive Memory");

        enableBreakPointsButton.setText("Enable BreakPoints");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        themedJLabel10.setText("General Options");

        reloadMemoryOnResetButton.setText("Reload Memory on Reset");

        javax.swing.GroupLayout themedJPanel12Layout = new javax.swing.GroupLayout(themedJPanel12);
        themedJPanel12.setLayout(themedJPanel12Layout);
        themedJPanel12Layout.setHorizontalGroup(
            themedJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themedJPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themedJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(breakOnRunTimeErrorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(adaptiveMemoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enableBreakPointsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(121, 121, 121)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themedJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themedJLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reloadMemoryOnResetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        themedJPanel12Layout.setVerticalGroup(
            themedJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addGroup(themedJPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(themedJPanel12Layout.createSequentialGroup()
                        .addComponent(themedJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(breakOnRunTimeErrorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adaptiveMemoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enableBreakPointsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themedJPanel12Layout.createSequentialGroup()
                        .addComponent(themedJLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reloadMemoryOnResetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(255, Short.MAX_VALUE))
        );

        themedJTabbedPane1.addTab("Processor", themedJPanel12);

        themedJLabel6.setText("Compiler Options");

        savePreProcessorFileButton.setText("Save PreProcessed File");

        saveCompilerInfoFileButton.setText("Save Compiler Info File");

        saveCleanedFileButton.setText("Save Cleaned File");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        themedJLabel9.setText("PreProcessor Option");

        includeRegDefButton.setText("Include regdef");

        includeSysCallDefButton.setText("Include syscalldef");

        linkedFileButton.setText("Use Linked File");

        javax.swing.GroupLayout themedJPanel13Layout = new javax.swing.GroupLayout(themedJPanel13);
        themedJPanel13.setLayout(themedJPanel13Layout);
        themedJPanel13Layout.setHorizontalGroup(
            themedJPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, themedJPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themedJLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveCleanedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(savePreProcessorFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveCompilerInfoFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(linkedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themedJPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themedJLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(includeRegDefButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(includeSysCallDefButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(144, 144, 144))
        );
        themedJPanel13Layout.setVerticalGroup(
            themedJPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(themedJPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(themedJPanel13Layout.createSequentialGroup()
                        .addComponent(themedJLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveCleanedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(savePreProcessorFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveCompilerInfoFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themedJPanel13Layout.createSequentialGroup()
                        .addComponent(themedJLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(includeRegDefButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(includeSysCallDefButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(linkedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(230, Short.MAX_VALUE))
        );

        themedJTabbedPane1.addTab("Compiler", themedJPanel13);

        themedJLabel7.setText("SystemCall Options");

        logSystemCallMessagesButton.setText("Log SystemCall Messages");

        resetProcessorOnTrap0Button.setText("Reset Processor On Trap 0");

        javax.swing.GroupLayout themedJPanel15Layout = new javax.swing.GroupLayout(themedJPanel15);
        themedJPanel15.setLayout(themedJPanel15Layout);
        themedJPanel15Layout.setHorizontalGroup(
            themedJPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themedJPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resetProcessorOnTrap0Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themedJLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logSystemCallMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(420, Short.MAX_VALUE))
        );
        themedJPanel15Layout.setVerticalGroup(
            themedJPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themedJPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(themedJLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logSystemCallMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetProcessorOnTrap0Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(280, Short.MAX_VALUE))
        );

        themedJTabbedPane1.addTab("SystemCalls", themedJPanel15);

        themedJLabel4.setText("GUI Themes");

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        themedJLabel11.setText("Syntax Theme");

        javax.swing.GroupLayout themedJPanel14Layout = new javax.swing.GroupLayout(themedJPanel14);
        themedJPanel14.setLayout(themedJPanel14Layout);
        themedJPanel14Layout.setHorizontalGroup(
            themedJPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themedJPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themedJLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guiThemeComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(224, 224, 224)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themedJPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themedJLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(syntaxThemeComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        themedJPanel14Layout.setVerticalGroup(
            themedJPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(themedJPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themedJPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(themedJPanel14Layout.createSequentialGroup()
                        .addComponent(themedJLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guiThemeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themedJPanel14Layout.createSequentialGroup()
                        .addComponent(themedJLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(syntaxThemeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(318, Short.MAX_VALUE))
        );

        themedJTabbedPane1.addTab("Theme", themedJPanel14);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(themedJTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(themedJTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OptionsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OptionsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OptionsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OptionsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OptionsGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.parker.mips.gui.theme.components.ThemedJCheckBox adaptiveMemoryButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox breakOnRunTimeErrorButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox enableAutoGUIUpdatesWhileRuning;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox enableBreakPointsButton;
    private org.parker.mips.gui.theme.components.ThemedJComboBox guiThemeComboBox;
    private org.parker.mips.gui.theme.components.ThemedJSlider guiUpdateTimeSlider;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox includeRegDefButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox includeSysCallDefButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox linkedFileButton;
    private org.parker.mips.gui.theme.components.ThemedJButton loadOptionsButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox logErrorsButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox logMessagesButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox logSystemCallMessagesButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox logSystemMessagesButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox logWarningsButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox reloadMemoryOnResetButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox resetProcessorOnTrap0Button;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox saveCleanedFileButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox saveCompilerInfoFileButton;
    private org.parker.mips.gui.theme.components.ThemedJButton saveCurrentOptionsButton;
    private org.parker.mips.gui.theme.components.ThemedJCheckBox savePreProcessorFileButton;
    private org.parker.mips.gui.theme.components.ThemedJComboBox syntaxThemeComboBox;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel1;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel10;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel11;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel2;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel3;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel4;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel5;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel6;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel7;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel8;
    private org.parker.mips.gui.theme.components.ThemedJLabel themedJLabel9;
    private org.parker.mips.gui.theme.components.ThemedJPanel1 themedJPanel11;
    private org.parker.mips.gui.theme.components.ThemedJPanel1 themedJPanel12;
    private org.parker.mips.gui.theme.components.ThemedJPanel1 themedJPanel13;
    private org.parker.mips.gui.theme.components.ThemedJPanel1 themedJPanel14;
    private org.parker.mips.gui.theme.components.ThemedJPanel1 themedJPanel15;
    private org.parker.mips.gui.theme.components.ThemedJTabbedPane themedJTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
