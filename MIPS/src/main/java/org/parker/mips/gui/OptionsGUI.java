/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import org.parker.mips.gui.theme.ThemeHandler;
import org.parker.mips.OptionsHandler;
import org.parker.mips.ResourceHandler;
import org.parker.mips.gui.theme.IJThemeInfo;

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

        initGUIThemeComponents();
        initEditorThemeComponents();

        this.setTitle("Options");
        try {
            this.setIconImage(new FlatSVGIcon("images/project.svg").getImage());
        } catch (Exception e) {

        }

        //Add icon
        //linking all of the components to the linked OPTIONS
        //General
        //logging
        OptionsHandler.logSystemMessages.LinkJButton(this.logSystemMessagesButton);
        OptionsHandler.logMessages.LinkJButton(this.logMessagesButton);
        OptionsHandler.logWarnings.LinkJButton(this.logWarningsButton);
        OptionsHandler.logErrors.LinkJButton(this.logErrorsButton);

        //GUI options
        OptionsHandler.enableGUIAutoUpdateWhileRunning.LinkJButton(this.enableAutoGUIUpdatesWhileRuning);
        OptionsHandler.GUIAutoUpdateRefreshTime.LinkJSlider(this.guiUpdateTimeSlider);

        //Compiler
        OptionsHandler.saveCleanedFile.LinkJButton(this.saveCleanedFileButton);
        OptionsHandler.savePreProcessedFile.LinkJButton(this.savePreProcessorFileButton);
        OptionsHandler.saveCompilationInfo.LinkJButton(this.saveCompilerInfoFileButton);

        //PreProcessor
        OptionsHandler.includeRegDef.LinkJButton(this.includeRegDefButton);
        OptionsHandler.includeSysCallDef.LinkJButton(this.includeSysCallDefButton);

        //Processor
        //Run Time
        OptionsHandler.breakOnRunTimeError.LinkJButton(this.breakOnRunTimeErrorButton);
        OptionsHandler.adaptiveMemory.LinkJButton(this.adaptiveMemoryButton);
        OptionsHandler.enableBreakPoints.LinkJButton(this.enableBreakPointsButton);

        //Non RunTime
        OptionsHandler.reloadMemoryOnReset.LinkJButton(this.reloadMemoryOnResetButton);

        //System Calls
        OptionsHandler.logSystemCallMessages.LinkJButton(this.logSystemCallMessagesButton);
        OptionsHandler.resetProcessorOnTrap0.LinkJButton(this.resetProcessorOnTrap0Button);

        //Others
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

        this.setVisible(true);
    }

    private void initGUIThemeComponents() {
        // add font families
        // get current font
        Font currentFont = OptionsHandler.currentGUIFont.val();
        String currentFamily = currentFont.getFamily();
        String currentSize = Integer.toString(currentFont.getSize());

        ArrayList<String> families = new ArrayList<>(Arrays.asList(
                "Arial", "Cantarell", "Comic Sans MS", "Courier New", "DejaVu Sans",
                "Dialog", "Liberation Sans", "Monospaced", "Noto Sans", "Roboto",
                "SansSerif", "Segoe UI", "Serif", "Tahoma", "Ubuntu", "Verdana"));
        if (!families.contains(currentFamily)) {
            families.add(currentFamily);
        }
        families.sort(String.CASE_INSENSITIVE_ORDER);

        guiFontList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = families.toArray(new String[0]);

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        guiFontList.setSelectedValue(currentFamily, false);

        guiFontList.addListSelectionListener((lse) -> {
            OptionsHandler.currentGUIFont.val(ThemeHandler.changeFontFamily(OptionsHandler.currentGUIFont.val(), guiFontList.getSelectedValue()));
        });
        OptionsHandler.currentGUIFont.addValueListener((e) -> {
            String current = OptionsHandler.currentGUIFont.val().getFontName();
            if (!current.equals(guiFontList.getSelectedValue())) {
                guiFontList.setSelectedValue(current, true);
            }
        });

        // add font sizes
        ArrayList<String> sizes = new ArrayList<>(Arrays.asList(
                "10", "12", "14", "16", "18", "20", "24", "28"));
        if (!sizes.contains(currentSize)) {
            sizes.add(currentSize);
        }
        sizes.sort(String.CASE_INSENSITIVE_ORDER);

        guiFontSizeList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = sizes.toArray(new String[0]);

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });

        guiFontSizeList.setSelectedValue(currentSize, false);

        guiFontSizeList.addListSelectionListener((lse) -> {
            String fontSize = guiFontSizeList.getSelectedValue();
            int val = Integer.parseInt(fontSize);
            OptionsHandler.currentGUIFont.val(ThemeHandler.changeFontSize(OptionsHandler.currentGUIFont.val(), val));
        });

        OptionsHandler.currentGUIFont.addValueListener((e) -> {
            String current = Integer.toString(OptionsHandler.currentGUIFont.val().getSize());
            if (!current.equals(guiFontList.getSelectedValue())) {
                guiFontList.setSelectedValue(current, true);
            }
        });

        {//fills the list with all pissible GUI themes
//            File file = new File(ResourceHandler.GUI_THEMES);
//            File[] files = file.listFiles();
//            String[] names = new String[files.length];
//            for (int i = 0; i < files.length; i++) {
//                names[i] = files[i].getName().split("\\.")[0];
//            }
//            this.guiThemeList.setModel(new DefaultComboBoxModel(names));
//            this.guiThemeList.setSelectedValue(OptionsHandler.currentGUITheme.val(), true);
//
//            this.guiThemeList.addListSelectionListener((ae) -> {
//                //System.err.println("asdasdasdasdasda");
//                String name = (String) guiThemeList.getSelectedValue();
//
//                if (!name.equals(OptionsHandler.currentGUITheme.val())) {
//                    OptionsHandler.currentGUITheme.val(name);
//                }
//            });

//            guiThemeList.setCellRenderer(new DefaultListCellRenderer() {
//                @Override
//                public Component getListCellRendererComponent(JList<?> list, Object value,
//                        int index, boolean isSelected, boolean cellHasFocus) {
//                    String title = categories.get(index);
//                    String name = ((IJThemeInfo) value).name;
//                    int sep = name.indexOf('/');
//                    if (sep >= 0) {
//                        name = name.substring(sep + 1).trim();
//                    }
//
//                    JComponent c = (JComponent) super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
//                    c.setToolTipText(buildToolTip((IJThemeInfo) value));
//                    if (title != null) {
//                        c.setBorder(new CompoundBorder(new ListCellTitledBorder(themesList, title), c.getBorder()));
//                    }
//                    return c;
//                }
//
//                private String buildToolTip(IJThemeInfo ti) {
//                    if (ti.themeFile != null) {
//                        return ti.themeFile.getPath();
//                    }
//                    if (ti.resourceName == null) {
//                        return ti.name;
//                    }
//
//                    return "Name: " + ti.name
//                            + "\nLicense: " + ti.license
//                            + "\nSource Code: " + ti.sourceCodeUrl;
//                }
//            });
        }

    }

    private void initEditorThemeComponents() {

        Font currentFont = OptionsHandler.currentEditorFont.val();
        String currentFamily = currentFont.getFamily();
        String currentSize = Integer.toString(currentFont.getSize());

        ArrayList<String> families = new ArrayList<>(Arrays.asList(
                "Arial", "Cantarell", "Comic Sans MS", "Courier New", "DejaVu Sans",
                "Dialog", "Liberation Sans", "Monospaced", "Noto Sans", "Roboto",
                "SansSerif", "Segoe UI", "Serif", "Tahoma", "Ubuntu", "Verdana"));
        if (!families.contains(currentFamily)) {
            families.add(currentFamily);
        }
        families.sort(String.CASE_INSENSITIVE_ORDER);

        editorFontList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = families.toArray(new String[0]);

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        editorFontList.setSelectedValue(currentFamily, false);

        editorFontList.addListSelectionListener((lse) -> {
            OptionsHandler.currentEditorFont.val(ThemeHandler.changeFontFamily(OptionsHandler.currentEditorFont.val(), editorFontList.getSelectedValue()));
        });

        OptionsHandler.currentEditorFont.addValueListener((e) -> {
            String current = OptionsHandler.currentEditorFont.val().getFontName();
            if (!current.equals(editorFontList.getSelectedValue())) {
                editorFontList.setSelectedValue(current, true);
            }
        });

        // add font sizes
        ArrayList<String> sizes = new ArrayList<>(Arrays.asList(
                "10", "12", "14", "16", "18", "20", "24", "28"));
        if (!sizes.contains(currentSize)) {
            sizes.add(currentSize);
        }
        sizes.sort(String.CASE_INSENSITIVE_ORDER);

        editorFontSizeList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = sizes.toArray(new String[0]);

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });

        editorFontSizeList.setSelectedValue(currentSize, false);

        editorFontSizeList.addListSelectionListener((lse) -> {
            String fontSize = editorFontSizeList.getSelectedValue();
            int val = Integer.parseInt(fontSize);
            OptionsHandler.currentEditorFont.val(ThemeHandler.changeFontSize(OptionsHandler.currentEditorFont.val(), val));

        });

        OptionsHandler.currentEditorFont.addValueListener((e) -> {
            String current = Integer.toString(OptionsHandler.currentEditorFont.val().getSize());
            if (!current.equals(editorFontList.getSelectedValue())) {
                editorFontList.setSelectedValue(current, true);
            }
        });

        {//loads list of available themes
            File file = new File(ResourceHandler.EDITOR_THEMES);
            File[] files = file.listFiles();
            if (files != null) {

            String[] names = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                names[i] = files[i].getName().split("\\.")[0];
            }
            this.editorThemeList.setModel(new DefaultComboBoxModel(names));
            this.editorThemeList.setSelectedValue(OptionsHandler.currentEditorTheme.val(), true);

            this.editorThemeList.addListSelectionListener((ae) -> {
                //System.err.println("asdasdasdasdasda");
                String name = (String) editorThemeList.getSelectedValue();

                if (!name.equals(OptionsHandler.currentEditorTheme.val())) {
                    OptionsHandler.currentEditorTheme.val(name);
                    //ASM_GUI.loadCurrentTheme();
                }
                //ThemeHandler.readThemeFromThemeName();
            });
            }
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

        themedJTabbedPane1 = new javax.swing.JTabbedPane();
        themedJPanel11 = new javax.swing.JPanel();
        themedJLabel2 = new javax.swing.JLabel();
        logSystemMessagesButton = new javax.swing.JCheckBox();
        logMessagesButton = new javax.swing.JCheckBox();
        logWarningsButton = new javax.swing.JCheckBox();
        logErrorsButton = new javax.swing.JCheckBox();
        themedJLabel3 = new javax.swing.JLabel();
        enableAutoGUIUpdatesWhileRuning = new javax.swing.JCheckBox();
        themedJLabel5 = new javax.swing.JLabel();
        saveCurrentOptionsButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        loadOptionsButton = new javax.swing.JButton();
        guiUpdateTimeSlider = new javax.swing.JSlider();
        themedJLabel8 = new javax.swing.JLabel();
        themedJPanel12 = new javax.swing.JPanel();
        breakOnRunTimeErrorButton = new javax.swing.JCheckBox();
        themedJLabel1 = new javax.swing.JLabel();
        adaptiveMemoryButton = new javax.swing.JCheckBox();
        enableBreakPointsButton = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        themedJLabel10 = new javax.swing.JLabel();
        reloadMemoryOnResetButton = new javax.swing.JCheckBox();
        themedJPanel13 = new javax.swing.JPanel();
        themedJLabel6 = new javax.swing.JLabel();
        savePreProcessorFileButton = new javax.swing.JCheckBox();
        saveCompilerInfoFileButton = new javax.swing.JCheckBox();
        saveCleanedFileButton = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        themedJLabel9 = new javax.swing.JLabel();
        includeRegDefButton = new javax.swing.JCheckBox();
        includeSysCallDefButton = new javax.swing.JCheckBox();
        themedJPanel15 = new javax.swing.JPanel();
        themedJLabel7 = new javax.swing.JLabel();
        logSystemCallMessagesButton = new javax.swing.JCheckBox();
        resetProcessorOnTrap0Button = new javax.swing.JCheckBox();
        themedJPanel14 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        themedJLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        guiThemeList = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        guiFontList = new javax.swing.JList<>();
        themedJLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        guiFontSizeList = new javax.swing.JList<>();
        themedJLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        editorThemeList = new javax.swing.JList<>();
        themedJLabel11 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        editorFontSizeList = new javax.swing.JList<>();
        themedJLabel14 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        editorFontList = new javax.swing.JList<>();
        themedJLabel15 = new javax.swing.JLabel();

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
        themedJLabel8.setText("Update Time: " + OptionsHandler.GUIAutoUpdateRefreshTime.val()+ " ms");
        OptionsHandler.GUIAutoUpdateRefreshTime.addValueListener(vl ->{
        	themedJLabel8.setText("Update Time: " + OptionsHandler.GUIAutoUpdateRefreshTime.val() + " ms");
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themedJLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadOptionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                    .addComponent(saveCurrentOptionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(125, Short.MAX_VALUE))
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
                .addContainerGap(269, Short.MAX_VALUE))
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
                    .addComponent(saveCompilerInfoFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    //.addComponent(linkedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
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
                //.addComponent(linkedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(244, Short.MAX_VALUE))
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
                .addContainerGap(423, Short.MAX_VALUE))
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
                .addContainerGap(294, Short.MAX_VALUE))
        );

        themedJTabbedPane1.addTab("SystemCalls", themedJPanel15);

        themedJLabel13.setText("Font Size");

        jScrollPane2.setViewportView(guiThemeList);

        guiFontList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "1", "2", "3", "4" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        guiFontList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(guiFontList);

        themedJLabel4.setText("Theme");

        guiFontSizeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(guiFontSizeList);

        themedJLabel12.setText("Font");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themedJLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themedJLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(themedJLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(themedJLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themedJLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themedJLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        jTabbedPane1.addTab("GUI", jPanel1);

        editorThemeList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(editorThemeList);

        themedJLabel11.setText("Theme");

        editorFontSizeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(editorFontSizeList);

        themedJLabel14.setText("Font");

        editorFontList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "1", "2", "3", "4" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        editorFontList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(editorFontList);

        themedJLabel15.setText("Font Size");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(themedJLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themedJLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(themedJLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(themedJLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(themedJLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(themedJLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Editor", jPanel2);

        javax.swing.GroupLayout themedJPanel14Layout = new javax.swing.GroupLayout(themedJPanel14);
        themedJPanel14.setLayout(themedJPanel14Layout);
        themedJPanel14Layout.setHorizontalGroup(
            themedJPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        themedJPanel14Layout.setVerticalGroup(
            themedJPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
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
    private javax.swing.JCheckBox adaptiveMemoryButton;
    private javax.swing.JCheckBox breakOnRunTimeErrorButton;
    private javax.swing.JList<String> editorFontList;
    private javax.swing.JList<String> editorFontSizeList;
    private javax.swing.JList<String> editorThemeList;
    private javax.swing.JCheckBox enableAutoGUIUpdatesWhileRuning;
    private javax.swing.JCheckBox enableBreakPointsButton;
    private javax.swing.JList<String> guiFontList;
    private javax.swing.JList<String> guiFontSizeList;
    private javax.swing.JList<IJThemeInfo> guiThemeList;
    private javax.swing.JSlider guiUpdateTimeSlider;
    private javax.swing.JCheckBox includeRegDefButton;
    private javax.swing.JCheckBox includeSysCallDefButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loadOptionsButton;
    private javax.swing.JCheckBox logErrorsButton;
    private javax.swing.JCheckBox logMessagesButton;
    private javax.swing.JCheckBox logSystemCallMessagesButton;
    private javax.swing.JCheckBox logSystemMessagesButton;
    private javax.swing.JCheckBox logWarningsButton;
    private javax.swing.JCheckBox reloadMemoryOnResetButton;
    private javax.swing.JCheckBox resetProcessorOnTrap0Button;
    private javax.swing.JCheckBox saveCleanedFileButton;
    private javax.swing.JCheckBox saveCompilerInfoFileButton;
    private javax.swing.JButton saveCurrentOptionsButton;
    private javax.swing.JCheckBox savePreProcessorFileButton;
    private javax.swing.JLabel themedJLabel1;
    private javax.swing.JLabel themedJLabel10;
    private javax.swing.JLabel themedJLabel11;
    private javax.swing.JLabel themedJLabel12;
    private javax.swing.JLabel themedJLabel13;
    private javax.swing.JLabel themedJLabel14;
    private javax.swing.JLabel themedJLabel15;
    private javax.swing.JLabel themedJLabel2;
    private javax.swing.JLabel themedJLabel3;
    private javax.swing.JLabel themedJLabel4;
    private javax.swing.JLabel themedJLabel5;
    private javax.swing.JLabel themedJLabel6;
    private javax.swing.JLabel themedJLabel7;
    private javax.swing.JLabel themedJLabel8;
    private javax.swing.JLabel themedJLabel9;
    private javax.swing.JPanel themedJPanel11;
    private javax.swing.JPanel themedJPanel12;
    private javax.swing.JPanel themedJPanel13;
    private javax.swing.JPanel themedJPanel14;
    private javax.swing.JPanel themedJPanel15;
    private javax.swing.JTabbedPane themedJTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
