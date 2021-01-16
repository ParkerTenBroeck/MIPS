package org.parker.mips.plugin;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PluginLogger extends Logger {
    private final String pluginName;

    /**
     * Creates a new PluginLogger that extracts the name from a plugin.
     *
     * @param context A reference to the plugin
     */
    public PluginLogger(PluginBase context) {
        super(context.getClass().getCanonicalName(), null);
        String prefix = context.DESCRIPTION.PREFIX;
        pluginName = prefix != null ? new StringBuilder().append("[").append(prefix + " " + context.DESCRIPTION.NAME).append("] ").toString() : "[" + context.DESCRIPTION.NAME + "] ";
        LogManager.getLogManager().addLogger(this);
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage(pluginName + logRecord.getMessage());
        super.log(logRecord);
    }

}
