/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatPropertiesLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.StyleContext;
import org.parker.mips.OptionsHandler;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.util.StringUtils;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import org.parker.mips.ResourceHandler;

/**
 *
 * @author parke
 */
public class ThemeHandler {

	public static final String THEMES_PACKAGE = "/com/formdev/flatlaf/intellijthemes/themes/";

	private static boolean asd = false;

	public static void init() {
		if (asd) {
			return;
		}
		FlatInspector.install("ctrl shift alt X");

		OptionsHandler.currentGUIFont.addValueListener((e) -> {
			UIManager.put("defaultFont", OptionsHandler.currentGUIFont.val());
			smoothUpdateUI();
		});
		UIManager.put("defaultFont", OptionsHandler.currentGUIFont.val());// sets to current font

		OptionsHandler.currentGUITheme.addValueListener((e) -> {

		});

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

		setTheme(OptionsHandler.currentGUITheme.val());
		OptionsHandler.currentGUITheme.addValueListener(vl -> {
			setTheme(OptionsHandler.currentGUITheme.val());
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
		EventQueue.invokeLater(() -> {
			//FlatAnimatedLafChange.showSnapshot();
			FlatLaf.updateUI();
			//FlatAnimatedLafChange.hideSnapshotWithAnimation();
		});
	}

	public static Font changeFontSize(Font font, int newSize) {
		return StyleContext.getDefaultStyleContext().getFont(font.getFamily(), font.getStyle(), newSize);
	}

	public static Font changeFontFamily(Font font, String newFontFamily) {
		return StyleContext.getDefaultStyleContext().getFont(newFontFamily, font.getStyle(), font.getSize());
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

	private static void saveTheme(IJThemeInfo themeInfo) {
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
