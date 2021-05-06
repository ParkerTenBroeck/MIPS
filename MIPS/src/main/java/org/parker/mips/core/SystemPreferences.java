package org.parker.mips.core;

import org.parker.mips.preferences.Preference;
import org.parker.mips.preferences.Preferences;
import org.parker.mips.util.SerializableFont;

public class SystemPreferences {

    public static final Preferences systemPrefs = Preferences.ROOT_NODE.getNode("system");
    public static final Preferences loggerPrefs = systemPrefs.getNode("logger");
    public static final Preferences guiPrefs = systemPrefs.getNode("gui");
    public static final Preferences themePrefs = systemPrefs.getNode("theme");

    //logging
    public static final Preference<Boolean> showStackTrace = loggerPrefs.getRawPreference("showStackTrace", false);
    public static final Preference<Boolean> showCallerClass = loggerPrefs.getRawPreference("showCallerClass", false);
    public static final Preference<Boolean> showCallerMethod = loggerPrefs.getRawPreference("showCallerMethod",false);
    public static final Preference<Boolean> logSystemCallMessages = loggerPrefs.getRawPreference("logSystemCallMessages", true);
    public static final Preference<String> systemLogLevel = loggerPrefs.getRawPreference("systemLogLevel","INFO");
    public static final Preference<String> assemblerLogLevel = loggerPrefs.getRawPreference("assemblerLogLevel","ASSEMBLER_MESSAGE");
    public static final Preference<String> runtimeLogLevel = loggerPrefs.getRawPreference("runtimeLogLevel","RUN_TIME_MESSAGE");

    //gui
    public static final Preference<Boolean> enableGUIAutoUpdateWhileRunning = guiPrefs.getRawPreference("enableGUIAutoUpdateWhileRunning", true);
    public static final Preference<Integer> GUIAutoUpdateRefreshTime = guiPrefs.getRawPreference("GUIAutoUpdateRefreshTime", 100);
    public static final Preference<Boolean> showToolTips = guiPrefs.getRawPreference("showToolTips", true);

    //themes
    public static final Preference<SerializableFont> currentGUIFont = themePrefs.getRawPreference("currentGUIFont",new SerializableFont("Segoe UI", 0, 15));
    public static final Preference<String> currentGUITheme = themePrefs.getRawPreference("currentGUITheme","Flat Dark");
    public static final Preference<SerializableFont> currentEditorFont = themePrefs.getRawPreference("currentEditorFont",new SerializableFont("Segoe UI", 0, 15));
    public static final Preference<String> currentEditorTheme = themePrefs.getRawPreference("currentEditorTheme","Dark");
}
