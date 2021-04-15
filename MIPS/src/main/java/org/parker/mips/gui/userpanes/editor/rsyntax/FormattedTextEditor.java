/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.userpanes.editor.rsyntax;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.parker.mips.util.FileUtils;
import org.parker.mips.util.ResourceHandler;
import org.parker.mips.gui.userpanes.editor.Editor;
import org.parker.mips.util.SerializableFont;
import org.parker.mips.preferences.Preference;
import org.parker.mips.preferences.Preferences;

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
    private static final Preferences themePrefs = Preferences.ROOT_NODE.getNode("system/theme");

    private static final DefaultHighlighter.DefaultHighlightPainter errorHighlight = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,0,0,128));

    protected FormattedTextEditor(File file) {
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

        if (file != null) {
            this.textArea.setText(FileUtils.loadFileAsString(file));
        }

        this.updateTitle();
    }

    public FormattedTextEditor(byte[] textBody){
        this((File)null);
        this.textArea.setText(new String(textBody));
    }

    protected FormattedTextEditor() {
        this((File)null);
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
        }else{
            ext = "asm";
        }

        switch (ext) {
            case "asm":
                AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
                atmf.putMapping("MIPS", "org.parker.mips.gui.userpanes.editor.rsyntax.MIPSAbstractTokenMaker");
                FoldParserManager.get().addFoldParserMapping("MIPS", new MIPSFoldParser());

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

        Preference<String> currentEditorTheme = themePrefs.getRawPreference("currentEditorTheme", "Dark");
        Preference<SerializableFont> currentEditorFont = themePrefs.getRawPreference("currentEditorFont", new SerializableFont("Segoe UI", 0, 15));


        currentEditorTheme.addLikedObserver(this, (o, arg) -> {
            if (textArea != null) {
                FormattedTextEditor.this.setTheme(currentEditorTheme.val());
                FormattedTextEditor.this.setAllFont(currentEditorFont.val().toFont());
            }
        });
        setTheme(currentEditorTheme.val());

        currentEditorFont.addLikedObserver(this, (o, arg) -> {
            if (textArea != null) {
                FormattedTextEditor.this.setAllFont(currentEditorFont.val().toFont());
            }
        });
        setAllFont(currentEditorFont.val().toFont());
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
}
