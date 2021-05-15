package org.parker.assembleride.gui.docking.userpanes.editor;

import java.io.File;

public interface Editor {
    boolean save();
    boolean isSaved();
    File getFile();
}
