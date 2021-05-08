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
package org.parker.assembleride.gui.docking.userpanes.editor;

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
