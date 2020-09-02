/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.RSyntax;

import GUI.lookandfeel.ModernScrollBarUI;
import java.awt.Color;
import java.awt.Font;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.Gutter;
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
        atmf.putMapping("MIPS", "GUI.RSyntax.MIPSAbstractTokenMaker");
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

//        Gutter gutter = this.getGutter();
        
//        gutter.setLineNumberColor(new Color(201, 201, 201));
//        gutter.setBackground(new Color(20, 20, 60));

//        textArea.setBackground(new Color(0, 0, 51));
//        textArea.setForeground(new Color(204, 204, 204));
//        textArea.setCaretColor(Color.yellow);
//        textArea.setCurrentLineHighlightColor(new Color(40, 40, 102));
//        textArea.setSelectionColor(new Color(102, 50, 122));

//        SyntaxScheme scheme = textArea.getSyntaxScheme();
//        scheme.getStyle(Token.RESERVED_WORD).foreground = Color.white;
//        scheme.getStyle(Token.MARKUP_PROCESSING_INSTRUCTION).foreground = Color.lightGray;
//        scheme.getStyle(Token.DATA_TYPE).foreground = Color.MAGENTA;
//        scheme.getStyle(Token.VARIABLE).foreground = Color.yellow;
//        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = Color.orange;
//        scheme.getStyle(Token.COMMENT_EOL).foreground = Color.green;
//
//        scheme.getStyle(Token.ANNOTATION).foreground = Color.lightGray;
//        scheme.getStyle(Token.IDENTIFIER).foreground = Color.cyan;
//        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.blue;
//        scheme.getStyle(Token.LITERAL_NUMBER_HEXADECIMAL).foreground = Color.pink;
//        scheme.getStyle(Token.RESERVED_WORD_2).foreground = Color.darkGray;
//        scheme.getStyle(Token.MARKUP_TAG_ATTRIBUTE).foreground = Color.GREEN;
//*/
       
        
        try{
        Theme theme = Theme.load(getClass().getResourceAsStream("/Themes/RSyntaxArea/default.xml"));
        theme.apply(textArea);
        }catch(Exception e){
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
