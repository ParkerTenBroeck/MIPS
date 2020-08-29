/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.RSyntax.ASMFoldParser;
import GUI.RSyntax.MIPSAbstractTokenMaker;
import GUI.lookandfeel.ModernScrollBarUI;
import GUI.lookandfeel.RoundedBorder;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultStyledDocument;
import mips.FileHandler;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rsyntaxtextarea.folding.YamlFoldParser;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author parke
 */
public class ASM_GUI extends javax.swing.JPanel {

    public static void clearText() {
        asmTextPane.setText("");
    }

    public static char[] getAllText() {
        return asmTextPane.getText().toCharArray();
    }

    static void setTextAreaFromASMFile() {
        setTextAreaFromList(FileHandler.getASMList());
    }

    static void setEnable(boolean enabled) {
        asmTextPane.setEditable(enabled);
    }

    /**
     * Creates new form AMS_GUI
     */
    public ASM_GUI() {
        initComponents();
        this.setBorder(new RoundedBorder(new Color(0, 0, 51), 0, 15));

        //asmScrollArea.getVerticalScrollBar().setUI(new ModernScrollBarUI(asmScrollArea));
        asmTextPane.setEditable(false);
        asmTextPane.setStyledDocument(new DefaultStyledDocument());

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("MIPS", "GUI.RSyntax.MIPSAbstractTokenMaker");

        FoldParserManager.get().addFoldParserMapping("MIPS", new ASMFoldParser());

        ((RSyntaxTextArea) this.jTextArea1).setSyntaxEditingStyle("MIPS");

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle("MIPS");
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);

        this.jScrollPane1 = sp;

        textArea.setBackground(new Color(0, 0, 51));
        textArea.setForeground(new Color(204,204,204));
        
        SyntaxScheme scheme = textArea.getSyntaxScheme();
        scheme.getStyle(Token.RESERVED_WORD).foreground = Color.white;
        scheme.getStyle(Token.MARKUP_PROCESSING_INSTRUCTION).foreground = Color.lightGray;
        scheme.getStyle(Token.DATA_TYPE).foreground = Color.blue;
        scheme.getStyle(Token.VARIABLE).foreground = Color.yellow;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = Color.orange;
        scheme.getStyle(Token.COMMENT_EOL).foreground = Color.green;

        scheme.getStyle(Token.ANNOTATION).foreground = Color.lightGray;
        scheme.getStyle(Token.IDENTIFIER).foreground = Color.cyan;
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.blue;
        
        

        textArea.revalidate();

        sp.setBackground(new Color(0, 0, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        );

        //RTextScrollPane sp = (RTextScrollPane)jScrollPane1;
        //((RSyntaxTextArea)this.jTextArea1).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
    }

    public static void setTextAreaFromList(List<String> list) {
        clearText();

        if (list == null) {
            return;
        }

        asmTextPane.setText(String.join("\n", list));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modernScrollPane1 = new GUI.lookandfeel.ModernScrollPane();
        asmTextPane = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new RSyntaxTextArea();
        ((RSyntaxTextArea)this.jTextArea1).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
        ((RSyntaxTextArea)this.jTextArea1).setCodeFoldingEnabled(true);

        asmTextPane.setEditable(false);
        asmTextPane.setBackground(new java.awt.Color(0, 0, 51));
        asmTextPane.setBorder(null);
        asmTextPane.setForeground(new java.awt.Color(204, 204, 204));
        asmTextPane.setCaretColor(new java.awt.Color(255, 255, 255));
        asmTextPane.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        asmTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                asmTextPaneKeyTyped(evt);
            }
        });
        modernScrollPane1.setViewportView(asmTextPane);

        setBackground(new java.awt.Color(102, 102, 0));
        setOpaque(false);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void asmTextPaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_asmTextPaneKeyTyped
        FileHandler.fileChange();
        colorText();
    }//GEN-LAST:event_asmTextPaneKeyTyped

    public static void colorText() {
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JTextPane asmTextPane;
    private static javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextArea jTextArea1;
    private static GUI.lookandfeel.ModernScrollPane modernScrollPane1;
    // End of variables declaration//GEN-END:variables
}
