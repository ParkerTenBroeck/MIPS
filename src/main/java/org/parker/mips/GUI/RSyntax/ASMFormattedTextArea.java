/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.GUI.RSyntax;

import org.parker.mips.GUI.lookandfeel.ModernScrollBarUI;
import java.awt.Color;
import java.awt.Font;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.RTextScrollPane;

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
        atmf.putMapping("MIPS", "org.parker.mips.GUI.RSyntax.MIPSAbstractTokenMaker");
        FoldParserManager.get().addFoldParserMapping("MIPS", new ASMFoldParser());

        textArea.setSyntaxEditingStyle("MIPS");
        textArea.setCodeFoldingEnabled(true);
        //super(textArea, true, new Color(201, 201, 201));

        this.setBorder(null);

        this.getVerticalScrollBar().setOpaque(false);
        this.getVerticalScrollBar().setUI(new ModernScrollBarUI(this, new Color(50, 50, 50)));

        this.getHorizontalScrollBar().setOpaque(false);
        this.getHorizontalScrollBar().setUI(new ModernScrollBarUI(this, new Color(50, 50, 50)));

        this.setBackground(new Color(0, 0, 51));
        this.setForeground(new Color(40, 40, 100));

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/Themes/RSyntaxArea/default.xml"));
            theme.apply(textArea);
        } catch (Exception e) {
            System.out.println("help " + e.getMessage());
        }
        //Theme theme = Theme.
        textArea.repaint();
        textArea.revalidate();

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
