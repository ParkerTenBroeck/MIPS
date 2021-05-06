package org.parker.mips.plugin.base;

import org.parker.mips.plugin.exceptions.InvalidDescriptionException;
import org.parker.mips.plugin.exceptions.InvalidPluginException;

import java.io.File;

public interface PluginLoader {
    Plugin loadPlugin(File file, String ymlPath) throws InvalidDescriptionException, InvalidPluginException;
}
