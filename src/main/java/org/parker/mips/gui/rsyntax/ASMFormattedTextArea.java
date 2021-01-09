/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.rsyntax;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

/**
 *
 * @author parke
 */
public class ASMFormattedTextArea extends RTextScrollPane {

    public final RSyntaxTextArea textArea;

    public ASMFormattedTextArea() {

        textArea = new RSyntaxTextArea();
        this.setViewportView(textArea);
        this.setLineNumbersEnabled(true);

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("MIPS", "org.parker.mips.gui.rsyntax.MIPSAbstractTokenMaker");
        FoldParserManager.get().addFoldParserMapping("MIPS", new ASMFoldParser());

        textArea.setSyntaxEditingStyle("MIPS");
        textArea.setCodeFoldingEnabled(true);

        this.setBorder(null);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setTheme(OptionsHandler.currentEditorTheme.val());

        //Theme theme = Theme.
        //setAllFontSize(18);
        this.repaint();
    }

    public final void setTheme(String name) {
        try {
            //Font cF = this.getFont();

            InputStream in = new FileInputStream(new File(ResourceHandler.EDITOR_THEMES + FileHandler.FILE_SEPERATOR + name + ".xml"));
            Theme theme = Theme.load(in);
            theme.apply(textArea);

            //setAllFontSize(18);
            //this.setFont(this.getFont());
        } catch (Exception e) {
            Log.logError("Error loading SyntaxText area Theme " + name + ".xml:\n" + Log.getFullExceptionMessage(e));
        }
    }

    public void setAllFont(Font font) {
        this.setFont(font);
        this.textArea.setFont(font);
        this.getGutter().setLineNumberFont(font);
    }
//    @Override
//    public void setFont(Font font) {
//        font = new Font(font.getName(), Font.PLAIN, 18);
//        super.setFont(font);
//        if (this.textArea != null) {
//            this.textArea.setFont(font);
//        }
//        if (this.getGutter() != null) {
//            this.getGutter().setLineNumberFont(font);
//        }
//    }

//    public int getAllFontSize() {
//        return this.getFont().getSize();
//    }
}
