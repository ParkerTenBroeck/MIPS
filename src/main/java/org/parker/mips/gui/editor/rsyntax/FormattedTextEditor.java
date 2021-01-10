/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.editor.rsyntax;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.parker.mips.FileHandler;
import org.parker.mips.Log;
import org.parker.mips.OptionsHandler;
import org.parker.mips.ResourceHandler;
import org.parker.mips.gui.editor.Editor;

/**
 *
 * @author parke
 */
public class FormattedTextEditor extends Editor {

    public FormattedTextEditor(File file) {
        super(file);
        initComponents();

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                setSaved(false);
                updateDisplayTitle();
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
        
        if(file != null){
            this.textArea.setText(FileHandler.loadFileAsString(file));
        }
    }

    public FormattedTextEditor(String string) {
        this((File) null);
        this.textArea.setText(string);
    }

    public FormattedTextEditor() {
        this((File) null);
    }

    public final void setTheme(String name) {
        try {
            InputStream in = new FileInputStream(new File(ResourceHandler.EDITOR_THEMES + FileHandler.FILE_SEPERATOR + name + ".xml"));
            Theme theme = Theme.load(in);
            theme.apply(textArea);
        } catch (Exception e) {
            Log.logError("Error loading SyntaxText area Theme " + name + ".xml:\n" + Log.getFullExceptionMessage(e));
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

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("MIPS", "org.parker.mips.gui.editor.rsyntax.MIPSAbstractTokenMaker");
        FoldParserManager.get().addFoldParserMapping("MIPS", new MIPSFoldParser());

        textArea.setSyntaxEditingStyle("MIPS");
        textArea.setCodeFoldingEnabled(true);

        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        OptionsHandler.currentEditorTheme.addValueListener((e) -> {
            if (textArea != null) {
                setTheme(OptionsHandler.currentEditorTheme.val());
                setAllFont(OptionsHandler.currentEditorFont.val());
            }
        });
        setTheme(OptionsHandler.currentEditorTheme.val());

        OptionsHandler.currentEditorFont.addValueListener((e) -> {
            if (textArea != null) {
                setAllFont(OptionsHandler.currentEditorFont.val());
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
            File temp = createTempFile("untitles", ".asm");
            FileHandler.saveByteArrayToFile(textArea.getText().getBytes(), temp);
            return temp;
        }
    }
}
