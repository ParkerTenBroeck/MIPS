/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.editor.rsyntax;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.parker.mips.FileUtils;
import org.parker.mips.OptionsHandler;
import org.parker.mips.ResourceHandler;
import org.parker.mips.gui.editor.Editor;

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
public class FormattedTextEditor extends Editor {

    private static final Logger LOGGER = Logger.getLogger(FormattedTextEditor.class.getName());

    private static final DefaultHighlighter.DefaultHighlightPainter errorHighlight = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,0,0,128));

    public FormattedTextEditor(File file, String name) {
        super(file, name);
        initComponents();

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                setSaved(false);
                updateDisplayTitle();

                try {
                    for(int i = 0; i < 20; i ++) {
                        textArea.addLineHighlight(i, textArea.getCurrentLineHighlightColor());
                    }
                    textArea.getHighlighter().addHighlight(20,100,errorHighlight);

                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });

        for (FocusListener fl : this.getFocusListeners()) {
            textArea.addFocusListener(fl);
        }

        if (file != null) {
            this.textArea.setText(FileUtils.loadFileAsString(file));
        }
    }

    public FormattedTextEditor(String textBody) {
        this( (File)null, "");
        this.textArea.setText(textBody);
    }

    public FormattedTextEditor(String textBody, String name){
        this( (File)null, name);
        this.textArea.setText(textBody);
    }

    public FormattedTextEditor() {
        this( (File)null, "");
    }

    public final void setTheme(String name) {
        try {
            InputStream in = new FileInputStream(ResourceHandler.EDITOR_THEMES + FileUtils.FILE_SEPARATOR + name + ".xml");
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
        }

        switch (ext) {
            default:
                AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
                atmf.putMapping("MIPS", "org.parker.mips.gui.editor.rsyntax.MIPSAbstractTokenMaker");
                FoldParserManager.get().addFoldParserMapping("MIPS", new MIPSFoldParser());

                textArea.setSyntaxEditingStyle("MIPS");
                textArea.setCodeFoldingEnabled(true);
                break;
        }

        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        OptionsHandler.currentEditorTheme.addLikedObserver(this, (o, arg) -> {
            if (textArea != null) {
                FormattedTextEditor.this.setTheme(OptionsHandler.currentEditorTheme.val());
                FormattedTextEditor.this.setAllFont(OptionsHandler.currentEditorFont.val());
            }
        });
        setTheme(OptionsHandler.currentEditorTheme.val());

        OptionsHandler.currentEditorFont.addLikedObserver(this, (o, arg) -> {
            if (textArea != null) {
                FormattedTextEditor.this.setAllFont(OptionsHandler.currentEditorFont.val());
            }
        });
        setAllFont(OptionsHandler.currentEditorFont.val());
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
    public File getFalseFile() {
        if (currentFile != null) {
            return currentFile;
        } else {
            File temp = createTempFile(getName(), ".asm");
            FileUtils.saveByteArrayToFileSafe(textArea.getText().getBytes(), temp);
            return temp;
        }
    }

    @Override
    public void closeS() {
        OptionsHandler.removeAllObserversLinkedToObject(this);
    }
}
