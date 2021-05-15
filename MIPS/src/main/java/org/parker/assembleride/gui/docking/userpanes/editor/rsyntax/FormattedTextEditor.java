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
package org.parker.assembleride.gui.docking.userpanes.editor.rsyntax;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.parker.assembleride.core.SystemPreferences;
import org.parker.assembleride.gui.docking.userpanes.editor.TextEditor;
import org.parker.assembleride.util.FileUtils;
import org.parker.assembleride.util.SystemResources;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 *
 * @author parke
 */
public class FormattedTextEditor extends TextEditor {

    private static final Logger LOGGER = Logger.getLogger(FormattedTextEditor.class.getName());
    //private static final Preferences themePrefs = Preferences.ROOT_NODE.getNode("system/theme");

    private static final DefaultHighlighter.DefaultHighlightPainter errorHighlight = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,0,0,128));

    public FormattedTextEditor(File file) {
        super(file);
        initComponents();

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(isSaved()){
                    setSaved(false);
                }
            }
            @Override
            public void keyPressed(KeyEvent ke) {}
            @Override
            public void keyReleased(KeyEvent ke) {}
        });

        for (FocusListener fl : this.getFocusListeners()) {
            textArea.addFocusListener(fl);
        }

        this.textArea.setText(FileUtils.loadFileAsString(file));

    }

    public final void setTheme(String name) {
        try {
            InputStream in = new FileInputStream(SystemResources.EDITOR_THEMES + FileUtils.FILE_SEPARATOR + name + ".xml");
            Theme theme = Theme.load(in);
            theme.apply(textArea);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading Editor Theme " + name, e);
        }
    }

    public final void setAllFont(Font font) {
        this.textArea.setFont(font);
        scrollPane.setFont(font);
        scrollPane.getGutter().setLineNumberFont(font);
    }

    private final void initComponents() {
        textArea = new RSyntaxTextArea();
        scrollPane = new RTextScrollPane();
        scrollPane.setViewportView(textArea);
        scrollPane.setLineNumbersEnabled(true);

        String ext = "";
        if (currentFile != null && currentFile.exists()) {
            ext = FileUtils.getExtension(currentFile);
        }else{
            ext = "asm";
        }

        switch (ext) {
            case "asm":
                AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
                atmf.putMapping("MIPS", "org.parker.assembleride.gui.docking.userpanes.editor.rsyntax.MIPSTokenMaker");
                FoldParserManager.get().addFoldParserMapping("MIPS", new BasicAssemblyFoldParser());

                textArea.setSyntaxEditingStyle("MIPS");
                textArea.setCodeFoldingEnabled(true);
                break;
            default:
                try {
                    textArea.setSyntaxEditingStyle("text/" + ext);
                }catch (Exception e){
                    textArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_NONE);
                }
                textArea.setCodeFoldingEnabled(true);
                break;
        }

        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        SystemPreferences.currentEditorTheme.addLikedObserver(this, (o, arg) -> {
            if (textArea != null) {
                FormattedTextEditor.this.setTheme(SystemPreferences.currentEditorTheme.val());
                FormattedTextEditor.this.setAllFont(SystemPreferences.currentEditorFont.val().toFont());
            }
        });
        setTheme(SystemPreferences.currentEditorTheme.val());

        SystemPreferences.currentEditorFont.addLikedObserver(this, (o, arg) -> {
            if (textArea != null) {
                FormattedTextEditor.this.setAllFont(SystemPreferences.currentEditorFont.val().toFont());
            }
        });
        setAllFont(SystemPreferences.currentEditorFont.val().toFont());
        java.util.prefs.Preferences asd = java.util.prefs.Preferences.systemNodeForPackage(this.getClass());
    }

    @Override
    public void setEnabled(boolean val) {
        super.setEnabled(val);
        textArea.setEnabled(val);
        textArea.setEditable(val);
    }

    public RTextScrollPane scrollPane;
    public RSyntaxTextArea textArea;

    @Override
    public byte[] getDataAsBytes() {
        return textArea.getText().getBytes();
    }

    @Override
    public void setHighlightedLine(int lineNumber) {

        try {
            textArea.removeAllLineHighlights();
            textArea.addLineHighlight(lineNumber, new Color(200, 0, 0, 128));
            //textArea.getHighlighter().addHighlight();

            int height = scrollPane.getVisibleRect().height;
            int yPos = clamp(textArea.yForLine(lineNumber) - (height / 2), 0, scrollPane.getVerticalScrollBar().getMaximum() - height);
            scrollPane.getVerticalScrollBar().setValue(yPos);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
