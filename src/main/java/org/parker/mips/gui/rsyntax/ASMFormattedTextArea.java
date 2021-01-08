/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.rsyntax;

import org.parker.mips.gui.theme.lookandfeel.ModernScrollBarUI;
import java.awt.Color;
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
import org.parker.mips.gui.theme.ThemeHandler;
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
        //super(textArea, true, new Color(201, 201, 201));

        this.setBorder(null);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
//
//        this.getVerticalScrollBar().setOpaque(false);
//        this.getVerticalScrollBar().setUI(new ModernScrollBarUI());
//
//        this.getHorizontalScrollBar().setOpaque(false);
//        this.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        this.setTheme(OptionsHandler.currentSyntaxTheme.value);

        //Theme theme = Theme.
        this.repaint();
    }

    public final void setTheme(String name) {
        try {
//<<<<<<< HEAD
            InputStream in = new FileInputStream(new File(ResourceHandler.SYNTAX_THEMES + FileHandler.FILE_SEPERATOR + name + ".xml"));
            Theme theme = Theme.load(in);
//=======
//            Theme theme = Theme.load(in, (Font) ThemeHandler.getThemeObjectFromThemeName(ThemeHandler.GENERAL_TEXT_FONT_PROPERTY_NAME));
//>>>>>>> master
            theme.apply(textArea);
        } catch (Exception e) {
            Log.logError("Error loading SyntaxText area Theme " + name + ".xml:\n" + Log.getFullExceptionMessage(e));
        }

        this.setBackground(textArea.getBackground());
    }

    public void setAllFontSize(int size) {
        Font temp = new Font(this.getFont().getName(), Font.PLAIN, size);
        this.setFont(temp);
        this.textArea.setFont(temp);
        this.getGutter().setLineNumberFont(temp);
    }

    public int getAllFontSize() {
        return this.getFont().getSize();
    }

}
