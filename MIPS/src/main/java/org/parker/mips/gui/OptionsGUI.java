/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import org.parker.mips.OptionsHandler;
import org.parker.mips.ResourceHandler;
import org.parker.mips.gui.theme.IJThemeInfo;
import org.parker.mips.gui.theme.IJThemesManager;
import org.parker.mips.gui.theme.ThemeHandler;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.*;

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

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		initGUIThemeComponents();
		initEditorThemeComponents();

		this.setTitle("Options");
		try {
			this.setIconImage(new FlatSVGIcon("images/project.svg").getImage());
		} catch (Exception e) {

		}

		Object reference = this;
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				OptionsHandler.removeAllObserversLinkedToObject(reference);
			}
		};

		this.addWindowListener(exitListener);

		// Add icon
		// linking all of the components to the linked OPTIONS
		// General
		// logging
		OptionsHandler.showStackTrace.LinkJButton(this, this.showStackTraceJCheckBox);
		OptionsHandler.showCallerClass.LinkJButton(this, this.showCallerClassNameJCheckBox);
		OptionsHandler.showCallerMethod.LinkJButton(this, this.showCallerMethodNameJCheckBox);
		OptionsHandler.systemLogLevel.LinkJList(this, this.systemLogLevelJList);
		OptionsHandler.assemblerLogLevel.LinkJList(this, this.assemblerLogLevelJList);
		OptionsHandler.runtimeLogLevel.LinkJList(this, this.runtimeLogLevelJList);

		// GUI options
		OptionsHandler.enableGUIAutoUpdateWhileRunning.LinkJButton(this, this.enableAutoGUIUpdatesWhileRuning);
		OptionsHandler.GUIAutoUpdateRefreshTime.LinkJSlider(this, this.guiUpdateTimeSlider);

		// Assembler
		OptionsHandler.saveCleanedFile.LinkJButton(this, this.saveCleanedFileButton);
		OptionsHandler.savePreProcessedFile.LinkJButton(this, this.savePreProcessorFileButton);
		OptionsHandler.saveCompilationInfo.LinkJButton(this, this.saveAssemblerInfoFileButton);

		// PreProcessor
		OptionsHandler.includeRegDef.LinkJButton(this, this.includeRegDefButton);
		OptionsHandler.includeSysCallDef.LinkJButton(this, this.includeSysCallDefButton);

		// Processor
		// Run Time
		OptionsHandler.breakOnRunTimeError.LinkJButton(this, this.breakOnRunTimeErrorButton);
		OptionsHandler.adaptiveMemory.LinkJButton(this, this.adaptiveMemoryButton);
		OptionsHandler.enableBreakPoints.LinkJButton(this, this.enableBreakPointsButton);

		// Non RunTime
		OptionsHandler.reloadMemoryOnReset.LinkJButton(this, this.reloadMemoryOnResetButton);

		// System Calls
		OptionsHandler.logSystemCallMessages.LinkJButton(this, this.logSystemCallMessagesButton);
		OptionsHandler.resetProcessorOnTrap0.LinkJButton(this, this.resetProcessorOnTrap0Button);

		// Others
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

		String[] allSystemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		ArrayList<String> families = new ArrayList<>(Arrays.asList(allSystemFonts));
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
			OptionsHandler.currentGUIFont.val(
					ThemeHandler.changeFontFamily(OptionsHandler.currentGUIFont.val(), guiFontList.getSelectedValue()));
		});
		OptionsHandler.currentGUIFont.addLikedObserver(this, new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				String current = OptionsHandler.currentGUIFont.val().getFontName();
				if (!current.equals(guiFontList.getSelectedValue())) {
					guiFontList.setSelectedValue(current, true);
				}
			}
		});

		// add font sizes
		ArrayList<String> sizes = new ArrayList<>(Arrays.asList("10", "12", "14", "16", "18", "20", "24", "28"));
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

		OptionsHandler.currentGUIFont.addLikedObserver(this, (e, a) -> {
			String current = Integer.toString(OptionsHandler.currentGUIFont.val().getSize());
			if (!current.equals(guiFontList.getSelectedValue())) {
				guiFontList.setSelectedValue(current, true);
			}
		});

		{
			updateThemesList();

			guiThemeList.setSelectedValue(OptionsHandler.currentGUITheme.val(), true);

			OptionsHandler.currentGUITheme.addLikedObserver(this, (o,v) -> {
				if (!guiThemeList.getSelectedValue().equals(v))
					guiThemeList.setSelectedValue(v, true);
			});

			guiThemeList.addListSelectionListener((e) -> {
					if (e.getValueIsAdjusting()) {
						return;
					}
					IJThemeInfo val = guiThemeList.getSelectedValue();
					
					if (guiThemeList.getSelectedValue() == null) {
						return;
					}

					if (!val.equals(OptionsHandler.currentGUITheme.val())){
						OptionsHandler.currentGUITheme.val(val);
					}
				});

		}

	}

	private void updateThemesList() {
		ArrayList<IJThemeInfo> themes = new ArrayList();

		int filterLightDark = 0;
		boolean showLight = (filterLightDark != 2);
		boolean showDark = (filterLightDark != 1);

		// load theme infos
		IJThemesManager.loadBundledThemes();
		IJThemesManager.loadThemesFromDirectory();

		// sort themes by name
		Comparator<? super IJThemeInfo> comparator = (t1, t2) -> t1.name.compareToIgnoreCase(t2.name);
		IJThemesManager.bundledThemes.sort(comparator);
		IJThemesManager.moreThemes.sort(comparator);

		// remember selection (must be invoked before clearing themes field)
		IJThemeInfo oldSel = guiThemeList.getSelectedValue();

		themes.clear();
		// categories.clear();

		// add core themes at beginning
		// categories.put(themes.size(), "Core Themes");
		if (showLight)
			themes.add(new IJThemeInfo("Flat Light", null, false, null, null, null, null, null,
					FlatLightLaf.class.getName()));
		if (showDark)
			themes.add(new IJThemeInfo("Flat Dark", null, true, null, null, null, null, null,
					FlatDarkLaf.class.getName()));
		if (showLight)
			themes.add(new IJThemeInfo("Flat IntelliJ", null, false, null, null, null, null, null,
					FlatIntelliJLaf.class.getName()));
		if (showDark)
			themes.add(new IJThemeInfo("Flat Darcula", null, true, null, null, null, null, null,
					FlatDarculaLaf.class.getName()));

		// add themes from directory
		// categories.put( themes.size(), "Current Directory" );
		themes.addAll(IJThemesManager.moreThemes);

		// add uncategorized bundled themes
		// categories.put( themes.size(), "IntelliJ Themes" );
		for (IJThemeInfo ti : IJThemesManager.bundledThemes) {
			boolean show = (showLight && !ti.dark) || (showDark && ti.dark);
			if (show && !ti.name.contains("/"))
				themes.add(ti);
		}

		// add categorized bundled themes
		String lastCategory = null;
		for (IJThemeInfo ti : IJThemesManager.bundledThemes) {
			boolean show = (showLight && !ti.dark) || (showDark && ti.dark);
			int sep = ti.name.indexOf('/');
			if (!show || sep < 0)
				continue;

			String category = ti.name.substring(0, sep).trim();
			if (!Objects.equals(lastCategory, category)) {
				lastCategory = category;
				// categories.put( themes.size(), category );
			}

			themes.add(ti);
		}

		// fill themes list
		guiThemeList.setModel(new AbstractListModel<IJThemeInfo>() {
			@Override
			public int getSize() {
				return themes.size();
			}

			@Override
			public IJThemeInfo getElementAt(int index) {
				return themes.get(index);
			}
		});

		// restore selection
		if (oldSel != null) {
			for (int i = 0; i < themes.size(); i++) {
				IJThemeInfo theme = themes.get(i);
				if (oldSel.name.equals(theme.name) && Objects.equals(oldSel.resourceName, theme.resourceName)
						&& Objects.equals(oldSel.themeFile, theme.themeFile)
						&& Objects.equals(oldSel.lafClassName, theme.lafClassName)) {
					guiThemeList.setSelectedIndex(i);
					break;
				}
			}

			// select first theme if none selected
			if (guiThemeList.getSelectedIndex() < 0)
				guiThemeList.setSelectedIndex(0);
		}

		// scroll selection into visible area
		int sel = guiThemeList.getSelectedIndex();
		if (sel >= 0) {
			Rectangle bounds = guiThemeList.getCellBounds(sel, sel);
			if (bounds != null)
				guiThemeList.scrollRectToVisible(bounds);
		}
	}

	private void initEditorThemeComponents() {

		Font currentFont = OptionsHandler.currentEditorFont.val();
		String currentFamily = currentFont.getFamily();
		String currentSize = Integer.toString(currentFont.getSize());

		String[] allSystemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		ArrayList<String> families = new ArrayList<>(Arrays.asList(allSystemFonts));
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
			OptionsHandler.currentEditorFont.val(ThemeHandler.changeFontFamily(OptionsHandler.currentEditorFont.val(),
					editorFontList.getSelectedValue()));
		});

		OptionsHandler.currentEditorFont.addLikedObserver(this, (o,v) -> {
			String current = OptionsHandler.currentEditorFont.val().getFontName();
			if (!current.equals(editorFontList.getSelectedValue())) {
				editorFontList.setSelectedValue(current, true);
			}
		});

		// add font sizes
		ArrayList<String> sizes = new ArrayList<>(Arrays.asList("10", "12", "14", "16", "18", "20", "24", "28"));
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
			OptionsHandler.currentEditorFont
					.val(ThemeHandler.changeFontSize(OptionsHandler.currentEditorFont.val(), val));

		});

		OptionsHandler.currentEditorFont.addLikedObserver(this, (o,v) -> {
			String current = Integer.toString(OptionsHandler.currentEditorFont.val().getSize());
			if (!current.equals(editorFontList.getSelectedValue())) {
				editorFontList.setSelectedValue(current, true);
			}
		});

		{// loads list of available themes
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
					// System.err.println("asdasdasdasdasda");
					String name = (String) editorThemeList.getSelectedValue();

					if (!name.equals(OptionsHandler.currentEditorTheme.val())) {
						OptionsHandler.currentEditorTheme.val(name);
						// ASM_GUI.loadCurrentTheme();
					}
					// ThemeHandler.readThemeFromThemeName();
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
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		themedJTabbedPane1 = new javax.swing.JTabbedPane();
		themedJPanel11 = new javax.swing.JPanel();
		//themedJLabel2 = new javax.swing.JLabel();
		//logSystemMessagesButton = new javax.swing.JCheckBox();
		//logMessagesButton = new javax.swing.JCheckBox();
		//logWarningsButton = new javax.swing.JCheckBox();
		//logErrorsButton = new javax.swing.JCheckBox();
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
		saveAssemblerInfoFileButton = new javax.swing.JCheckBox();
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

		logPanel = new javax.swing.JPanel();
		systemLogLevelJList = new javax.swing.JList<String>();
		assemblerLogLevelJList = new javax.swing.JList<String>();
		runtimeLogLevelJList = new javax.swing.JList<String>();
		showStackTraceJCheckBox = new javax.swing.JCheckBox();
		showCallerMethodNameJCheckBox = new javax.swing.JCheckBox();
		showCallerClassNameJCheckBox = new javax.swing.JCheckBox();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		//themedJLabel2.setText("Log Options");

		//logSystemMessagesButton.setText("Log System Messages");

		//logMessagesButton.setText("Log Messages");

		//logWarningsButton.setText("Log Warnigs");

		//logErrorsButton.setText("Log Errors");

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
		themedJLabel8.setText("Update Time: " + OptionsHandler.GUIAutoUpdateRefreshTime.val() + " ms");
		OptionsHandler.GUIAutoUpdateRefreshTime.addLikedObserver(this, (o,v) -> {
			themedJLabel8.setText("Update Time: " + OptionsHandler.GUIAutoUpdateRefreshTime.val() + " ms");
		});

		javax.swing.GroupLayout themedJPanel11Layout = new javax.swing.GroupLayout(themedJPanel11);
		themedJPanel11.setLayout(themedJPanel11Layout);
		themedJPanel11Layout.setHorizontalGroup(themedJPanel11Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(themedJPanel11Layout.createSequentialGroup().addContainerGap()
						.addGroup(themedJPanel11Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								//.addComponent(themedJLabel2, javax.swing.GroupLayout.PREFERRED_SIZE,
								//		javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								//.addComponent(logSystemMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE,
								//		javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								//.addComponent(logErrorsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
								//		javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								//.addComponent(logWarningsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
								//		javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								//.addComponent(logMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE,
								//		javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(themedJLabel3, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(enableAutoGUIUpdatesWhileRuning, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(guiUpdateTimeSlider, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(themedJLabel8, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
						.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(themedJPanel11Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(themedJLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 153,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(loadOptionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 171,
										Short.MAX_VALUE)
								.addComponent(saveCurrentOptionsButton, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(146, 146, 146)));
		themedJPanel11Layout.setVerticalGroup(themedJPanel11Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(themedJPanel11Layout.createSequentialGroup().addContainerGap().addGroup(themedJPanel11Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(themedJLabel3, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(themedJLabel5, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						//.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(themedJPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(themedJPanel11Layout.createSequentialGroup()
										//.addComponent(logSystemMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										//		javax.swing.GroupLayout.DEFAULT_SIZE,
										//		javax.swing.GroupLayout.PREFERRED_SIZE)
										//.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										//.addComponent(logMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										//		javax.swing.GroupLayout.DEFAULT_SIZE,
										//		javax.swing.GroupLayout.PREFERRED_SIZE)
										//.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										//.addComponent(logWarningsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										//		javax.swing.GroupLayout.DEFAULT_SIZE,
										//		javax.swing.GroupLayout.PREFERRED_SIZE)
										//.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										//.addComponent(logErrorsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										//		javax.swing.GroupLayout.DEFAULT_SIZE,
										//		javax.swing.GroupLayout.PREFERRED_SIZE)
										//.addGap(18, 18, 18)
										//.addComponent(themedJLabel3, javax.swing.GroupLayout.PREFERRED_SIZE,
										//		javax.swing.GroupLayout.DEFAULT_SIZE,
										//		javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(enableAutoGUIUpdatesWhileRuning,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(themedJPanel11Layout.createSequentialGroup()
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(loadOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveCurrentOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(themedJLabel8, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(guiUpdateTimeSlider, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(126, Short.MAX_VALUE))
				.addComponent(jSeparator1));

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
		themedJPanel12Layout.setHorizontalGroup(themedJPanel12Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(themedJPanel12Layout.createSequentialGroup().addContainerGap()
						.addGroup(themedJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(themedJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(breakOnRunTimeErrorButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(adaptiveMemoryButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(enableBreakPointsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(121, 121, 121)
						.addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 15,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(themedJPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(themedJLabel10, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(reloadMemoryOnResetButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(125, Short.MAX_VALUE)));
		themedJPanel12Layout.setVerticalGroup(themedJPanel12Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator3)
				.addGroup(themedJPanel12Layout.createSequentialGroup().addContainerGap().addGroup(themedJPanel12Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(themedJPanel12Layout.createSequentialGroup()
								.addComponent(themedJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(breakOnRunTimeErrorButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(adaptiveMemoryButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
										enableBreakPointsButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(themedJPanel12Layout.createSequentialGroup()
								.addComponent(themedJLabel10, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(reloadMemoryOnResetButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(269, Short.MAX_VALUE)));

		themedJTabbedPane1.addTab("Processor", themedJPanel12);

		themedJLabel6.setText("Assembler Options");

		savePreProcessorFileButton.setText("Save PreProcessed File");

		saveAssemblerInfoFileButton.setText("Save Assembler Info File");

		saveCleanedFileButton.setText("Save Cleaned File");

		jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

		themedJLabel9.setText("PreProcessor Option");

		includeRegDefButton.setText("Include regdef");

		includeSysCallDefButton.setText("Include syscalldef");

		javax.swing.GroupLayout themedJPanel13Layout = new javax.swing.GroupLayout(themedJPanel13);
		themedJPanel13.setLayout(themedJPanel13Layout);
		themedJPanel13Layout.setHorizontalGroup(themedJPanel13Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, themedJPanel13Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(themedJPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(themedJLabel6, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(saveCleanedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(savePreProcessorFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(saveAssemblerInfoFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						// .addComponent(linkedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
						// javax.swing.GroupLayout.DEFAULT_SIZE,
						// javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
						.addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(themedJPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(themedJLabel9, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(includeRegDefButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(includeSysCallDefButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(144, 144, 144)));
		themedJPanel13Layout.setVerticalGroup(themedJPanel13Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
				.addGroup(themedJPanel13Layout.createSequentialGroup().addContainerGap().addGroup(themedJPanel13Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(themedJPanel13Layout.createSequentialGroup()
								.addComponent(themedJLabel6, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(saveCleanedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(savePreProcessorFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(saveAssemblerInfoFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(themedJPanel13Layout.createSequentialGroup()
								.addComponent(themedJLabel9, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(includeRegDefButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(includeSysCallDefButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						// .addComponent(linkedFileButton, javax.swing.GroupLayout.PREFERRED_SIZE,
						// javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(244, Short.MAX_VALUE)));

		themedJTabbedPane1.addTab("Assembler", themedJPanel13);

		themedJLabel7.setText("SystemCall Options");

		logSystemCallMessagesButton.setText("Log SystemCall Messages");

		resetProcessorOnTrap0Button.setText("Reset Processor On Trap 0");

		javax.swing.GroupLayout themedJPanel15Layout = new javax.swing.GroupLayout(themedJPanel15);
		themedJPanel15.setLayout(themedJPanel15Layout);
		themedJPanel15Layout.setHorizontalGroup(themedJPanel15Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(themedJPanel15Layout.createSequentialGroup().addContainerGap()
						.addGroup(themedJPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(resetProcessorOnTrap0Button, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(themedJLabel7, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(logSystemCallMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(423, Short.MAX_VALUE)));
		themedJPanel15Layout
				.setVerticalGroup(themedJPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(themedJPanel15Layout.createSequentialGroup().addContainerGap()
								.addComponent(themedJLabel7, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(logSystemCallMessagesButton, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(resetProcessorOnTrap0Button, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(294, Short.MAX_VALUE)));

		themedJTabbedPane1.addTab("SystemCalls", themedJPanel15);

		themedJLabel13.setText("Font Size");

		jScrollPane2.setViewportView(guiThemeList);

		guiFontList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = { "1", "2", "3", "4" };

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		guiFontList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jScrollPane3.setViewportView(guiFontList);

		themedJLabel4.setText("Theme");

		guiFontSizeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jScrollPane4.setViewportView(guiFontSizeList);

		themedJLabel12.setText("Font");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(themedJLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
								.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(themedJLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
								.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(themedJLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 150,Short.MAX_VALUE)
								.addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addContainerGap(0, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(themedJLabel4, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(themedJLabel12, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(themedJLabel13, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel1Layout.createSequentialGroup().addComponent(jScrollPane2).addGap(13, 13,
												13))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 297,
														Short.MAX_VALUE))
												.addContainerGap()))));

		jTabbedPane1.addTab("GUI", jPanel1);

		editorThemeList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane1.setViewportView(editorThemeList);

		themedJLabel11.setText("Theme");

		editorFontSizeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jScrollPane5.setViewportView(editorFontSizeList);

		themedJLabel14.setText("Font");

		editorFontList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = { "1", "2", "3", "4" };

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		editorFontList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jScrollPane6.setViewportView(editorFontList);

		themedJLabel15.setText("Font Size");

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
								.addComponent(themedJLabel11, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(themedJLabel14, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
								.addComponent(themedJLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(0, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(themedJLabel14, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(themedJLabel15, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jScrollPane6)))
						.addGroup(jPanel2Layout.createSequentialGroup()
								.addComponent(themedJLabel11, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
										jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))
						.addContainerGap()));

		jTabbedPane1.addTab("Editor", jPanel2);

		themedJTabbedPane1.addTab("Theme", themedJPanel14);

		{//logging panel

			javax.swing.JScrollPane bruhScroll1 = new javax.swing.JScrollPane();
			javax.swing.JLabel systemLevelsLogJLable = new javax.swing.JLabel();
			javax.swing.JLabel runtimeLevelsLogJLable = new javax.swing.JLabel();
			javax.swing.JScrollPane bruhScroll2 = new javax.swing.JScrollPane();
			javax.swing.JLabel assemblerLevelsLogJLable = new javax.swing.JLabel();
			javax.swing.JScrollPane bruhScroll3 = new javax.swing.JScrollPane();

			systemLogLevelJList.setModel(new javax.swing.AbstractListModel<String>() {
				String[] strings = { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST" };
				public int getSize() { return strings.length; }
				public String getElementAt(int i) { return strings[i]; }
			});
			bruhScroll1.setViewportView(systemLogLevelJList);

			systemLevelsLogJLable.setText("System Levels");

			runtimeLevelsLogJLable.setText("RunTime Levels");

			runtimeLogLevelJList.setModel(new javax.swing.AbstractListModel<String>() {
				String[] strings = { "OFF", "RUN_TIME_ERROR", "RUN_TIME_WARNING", "RUN_TIME_MESSAGE" };
				public int getSize() { return strings.length; }
				public String getElementAt(int i) { return strings[i]; }
			});
			bruhScroll2.setViewportView(runtimeLogLevelJList);

			assemblerLevelsLogJLable.setText("Assembler Levels");

			assemblerLogLevelJList.setModel(new javax.swing.AbstractListModel<String>() {
				String[] strings = { "OFF", "ASSEMBLER_ERROR", "ASSEMBLER_WARNING", "ASSEMBLER_MESSAGE" };
				public int getSize() { return strings.length; }
				public String getElementAt(int i) { return strings[i]; }
			});
			bruhScroll3.setViewportView(assemblerLogLevelJList);

			showCallerMethodNameJCheckBox.setText("Show Caller Method Name");

			showStackTraceJCheckBox.setText("Show Stack Trace On Error");

			showCallerClassNameJCheckBox.setText("Show Caller Class Name");

			javax.swing.GroupLayout loggerLayout = new javax.swing.GroupLayout(logPanel);
			logPanel.setLayout(loggerLayout);
			loggerLayout.setHorizontalGroup(
					loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(loggerLayout.createSequentialGroup()
									.addContainerGap()
									.addGroup(loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
											.addComponent(bruhScroll1)
											.addComponent(systemLevelsLogJLable, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
											.addComponent(bruhScroll2)
											.addComponent(runtimeLevelsLogJLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(bruhScroll3, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
											.addComponent(assemblerLevelsLogJLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									.addGroup(loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
											.addComponent(showCallerMethodNameJCheckBox)
											.addComponent(showCallerClassNameJCheckBox)
											.addComponent(showStackTraceJCheckBox))
									.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			loggerLayout.setVerticalGroup(
					loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(loggerLayout.createSequentialGroup()
									.addContainerGap()
									.addGroup(loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(systemLevelsLogJLable)
											.addComponent(assemblerLevelsLogJLable))
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
											.addComponent(bruhScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
											.addGroup(loggerLayout.createSequentialGroup()
													.addGroup(loggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
															.addGroup(loggerLayout.createSequentialGroup()
																	.addComponent(showCallerMethodNameJCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
																	.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(showCallerClassNameJCheckBox)
																	.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																	.addComponent(showStackTraceJCheckBox))
															.addComponent(bruhScroll3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
													.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(runtimeLevelsLogJLable)
													.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(bruhScroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
									.addContainerGap())
			);

			themedJTabbedPane1.addTab("Logging", logPanel);
		}

		javax.swing.GroupLayout themedJPanel14Layout = new javax.swing.GroupLayout(themedJPanel14);
		themedJPanel14.setLayout(themedJPanel14Layout);
		themedJPanel14Layout.setHorizontalGroup(themedJPanel14Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1));
		themedJPanel14Layout.setVerticalGroup(themedJPanel14Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(themedJTabbedPane1,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(themedJTabbedPane1,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */

		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>

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
	//private javax.swing.JCheckBox logErrorsButton;
	//private javax.swing.JCheckBox logMessagesButton;
	private javax.swing.JCheckBox logSystemCallMessagesButton;
	//private javax.swing.JCheckBox logSystemMessagesButton;
	//private javax.swing.JCheckBox logWarningsButton;
	private javax.swing.JCheckBox reloadMemoryOnResetButton;
	private javax.swing.JCheckBox resetProcessorOnTrap0Button;
	private javax.swing.JCheckBox saveCleanedFileButton;
	private javax.swing.JCheckBox saveAssemblerInfoFileButton;
	private javax.swing.JButton saveCurrentOptionsButton;
	private javax.swing.JCheckBox savePreProcessorFileButton;
	private javax.swing.JLabel themedJLabel1;
	private javax.swing.JLabel themedJLabel10;
	private javax.swing.JLabel themedJLabel11;
	private javax.swing.JLabel themedJLabel12;
	private javax.swing.JLabel themedJLabel13;
	private javax.swing.JLabel themedJLabel14;
	private javax.swing.JLabel themedJLabel15;
	//private javax.swing.JLabel themedJLabel2;
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
	//log options tab
	private javax.swing.JPanel logPanel;
	private javax.swing.JList<String> systemLogLevelJList;
	private javax.swing.JList<String> assemblerLogLevelJList;
	private javax.swing.JList<String> runtimeLogLevelJList;
	private javax.swing.JCheckBox showStackTraceJCheckBox;
	private javax.swing.JCheckBox showCallerMethodNameJCheckBox;
	private javax.swing.JCheckBox showCallerClassNameJCheckBox;
	// End of variables declaration//GEN-END:variables

}

class ListCellTitledBorder implements Border {

	private final JList<?> list;
	private final String title;

	ListCellTitledBorder(JList<?> list, String title) {
		this.list = list;
		this.title = title;
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		int height = c.getFontMetrics(list.getFont()).getHeight();
		return new Insets(height, 0, 0, 0);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		FontMetrics fm = c.getFontMetrics(list.getFont());
		int titleWidth = fm.stringWidth(title);
		int titleHeight = fm.getHeight();

// fill background
		g.setColor(list.getBackground());
		g.fillRect(x, y, width, titleHeight);

		int gap = UIScale.scale(4);

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			FlatUIUtils.setRenderingHints(g2);

			g2.setColor(UIManager.getColor("Label.disabledForeground"));

			// paint separator lines
			int sepWidth = (width - titleWidth) / 2 - gap - gap;
			if (sepWidth > 0) {
				int sy = y + Math.round(titleHeight / 2f);
				float sepHeight = UIScale.scale((float) 1);

				g2.fill(new Rectangle2D.Float(x + gap, sy, sepWidth, sepHeight));
				g2.fill(new Rectangle2D.Float(x + width - gap - sepWidth, sy, sepWidth, sepHeight));
			}

			// draw title
			int xt = x + ((width - titleWidth) / 2);
			int yt = y + fm.getAscent();

			FlatUIUtils.drawString(list, g2, title, xt, yt);
		} finally {
			g2.dispose();
		}
	}
}
