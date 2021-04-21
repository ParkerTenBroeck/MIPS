package org.parker.mips.gui.userpanes.editor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

public abstract class TextEditor extends FileEditor {

    protected TextEditor(File file) {
        super(file);
        this.addFocusListener(new keySaveListener(this));
    }

    private class keySaveListener implements FocusListener {

        private final FileEditor parent;

        public keySaveListener(FileEditor editor) {
            parent = editor;
        }

        @Override
        public void focusGained(FocusEvent fe) {
            EditorHandler.setLastFocused(parent);
            updateTitle();
        }

        @Override
        public void focusLost(FocusEvent fe) {
        }
    }

    public abstract void setHighlightedLine(int lineNumber);
}
