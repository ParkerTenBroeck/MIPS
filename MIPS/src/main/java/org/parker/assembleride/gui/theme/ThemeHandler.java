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
package org.parker.assembleride.gui.theme;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatPropertiesLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.util.StringUtils;
import com.formdev.flatlaf.util.SystemInfo;
import org.parker.assembleride.core.SystemPreferences;
import org.parker.assembleride.util.ResourceHandler;
import org.parker.assembleride.util.SerializableFont;
import org.parker.assembleride.preferences.Preference;

import javax.swing.*;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class ThemeHandler {

	public static final String THEMES_PACKAGE = "/com/formdev/flatlaf/intellijthemes/themes/";

	private static boolean asd = false;

	//private static final Preferences themePrefs = Preferences.ROOT_NODE.getNode("system/theme");
	private static final Logger LOGGER = Logger.getLogger(ThemeHandler.class.toString());

	public static void init() {
		if (asd) {
			return;
		}
		FlatInspector.install("ctrl shift alt X");

		SystemPreferences.currentGUIFont.addObserver((o, v) -> {
			UIManager.put("defaultFont", ((SerializableFont)v).toFont());
			smoothUpdateUI();
		});
		UIManager.put("defaultFont", SystemPreferences.currentGUIFont.val().toFont());// sets to current font

//		OptionsHandler.currentGUITheme.addObserver((o,v) -> {
//
//		});

		UIManager.put("ScrollBar.thumbArc", 2);
		UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		UIManager.put("Button.arc", 0);
		UIManager.put("Component.arc", 0);
		UIManager.put("CheckBox.arc", 0);
		UIManager.put("ProgressBar.arc", 0);
		UIManager.put("CheckBox.icon.style", "filled");
		if (SystemInfo.isMacOS && System.getProperty("apple.laf.useScreenMenuBar") == null) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		Preference<String> currentGUITheme = SystemPreferences.themePrefs.getRawPreference("currentGUITheme", "Flat Dark");

		setTheme(currentGUITheme.val());
		currentGUITheme.addObserver((o,v) -> {
			setTheme(currentGUITheme.val());
		});

		asd = true;
	}

	public static void smoothUpdateUI() {

		EventQueue.invokeLater(() -> {
			FlatAnimatedLafChange.showSnapshot();
			FlatLaf.updateUI();
			FlatAnimatedLafChange.hideSnapshotWithAnimation();
		});
	}
	
	public static void updateUI() {
		EventQueue.invokeLater(FlatLaf::updateUI);
	}

	public static Font changeFontSize(Font font, int newSize) {
		return StyleContext.getDefaultStyleContext().getFont(font.getFamily(), font.getStyle(), newSize);
	}

	public static Font changeFontFamily(Font font, String newFontFamily) {
		return StyleContext.getDefaultStyleContext().getFont(newFontFamily, font.getStyle(), font.getSize());
	}

	private static void setTheme(String themeName){
		IJThemeInfo theme = IJThemesManager.getTheme(themeName);
		if(theme != null) {
			setTheme(theme);
		}else{
			LOGGER.log(Level.SEVERE, "GUI Theme: " + themeName + " does not exist");
		}
	}

	private static void setTheme(IJThemeInfo themeInfo) {
		if (themeInfo == null) {
			return;
		}

		EventQueue.invokeLater(() -> {
			// change look and feel
			if (themeInfo.lafClassName != null) {
				if (themeInfo.lafClassName.equals(UIManager.getLookAndFeel().getClass().getName())) {
					return;
				}

				FlatAnimatedLafChange.showSnapshot();

				try {
					UIManager.setLookAndFeel(themeInfo.lafClassName);
				} catch (Exception ex) {
					// ex.printStackTrace();
					// showInformationDialog( "Failed to create '" + themeInfo.lafClassName + "'.",
					// ex );
				}
			} else if (themeInfo.themeFile != null) {
				FlatAnimatedLafChange.showSnapshot();

				try {
					if (themeInfo.themeFile.getName().endsWith(".properties")) {
						FlatLaf.install(new FlatPropertiesLaf(themeInfo.name, themeInfo.themeFile));
					} else {
						FlatLaf.install(IntelliJTheme.createLaf(new FileInputStream(themeInfo.themeFile)));
					}

					// DemoPrefs.getState().put( DemoPrefs.KEY_LAF_THEME, DemoPrefs.FILE_PREFIX +
					// themeInfo.themeFile );
				} catch (Exception ex) {
					// ex.printStackTrace();
					// showInformationDialog( "Failed to load '" + themeInfo.themeFile + "'.", ex );
				}
			} else {
				FlatAnimatedLafChange.showSnapshot();

				IntelliJTheme.install(ThemeHandler.class.getResourceAsStream(THEMES_PACKAGE + themeInfo.resourceName));
				// DemoPrefs.getState().put( DemoPrefs.KEY_LAF_THEME, DemoPrefs.RESOURCE_PREFIX
				// + themeInfo.resourceName );
			}

			// update all components
			FlatLaf.updateUI();
			FlatAnimatedLafChange.hideSnapshotWithAnimation();
		});
	}

	public static void saveTheme(IJThemeInfo themeInfo) {
		if (themeInfo == null || themeInfo.resourceName == null) {
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(ResourceHandler.GUI_THEMES, themeInfo.resourceName));
		if (fileChooser.showSaveDialog(SwingUtilities.windowForComponent(null)) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();

		// save theme
		try {
			Files.copy(ThemeHandler.class.getResourceAsStream(THEMES_PACKAGE + themeInfo.resourceName), file.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			// showInformationDialog("Failed to save theme to '" + file + "'.", ex);
			return;
		}

		// save license
		if (themeInfo.licenseFile != null) {
			try {
				File licenseFile = new File(file.getParentFile(),
						StringUtils.removeTrailing(file.getName(), ".theme.json")
								+ themeInfo.licenseFile.substring(themeInfo.licenseFile.indexOf('.')));
				Files.copy(ThemeHandler.class.getResourceAsStream(THEMES_PACKAGE + themeInfo.licenseFile),
						licenseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				// showInformationDialog("Failed to save theme license to '" + file + "'.", ex);
				return;
			}
		}
	}
}