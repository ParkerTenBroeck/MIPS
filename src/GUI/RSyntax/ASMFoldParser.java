/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.RSyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldType;

/**
 *
 * @author parke
 */
public class ASMFoldParser implements FoldParser {

    /**
     * Constructor.
     *
     */
    public ASMFoldParser() {
    }

    private static boolean isSpaces(Token t) {
        String lexeme = t.getLexeme();
        return lexeme.trim().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Fold> getFolds(RSyntaxTextArea textArea) {

        List<Fold> folds = new ArrayList<>();
        
        if(true)return folds;
        
        Stack<Integer> indentStack = new Stack<>();

        Fold currentFold = null;
        int lineCount = textArea.getLineCount();

        // closing needs to be done with offset of the previous line
        int lastOffset = 0;

        try {

            for (int line = 0; line < lineCount; line++) {

                Token t = textArea.getTokenListForLine(line);
                Token startLine = t;
                int offset = t.getOffset();

                // indent level is count of spaces + possible one for leading minus
                int indent = 0;
                
                if(t.getType() == Token.FUNCTION){
                    //while()
                }
//                if (t != null && t.isPaintable() && t.isSingleChar('-')) {
//                    indent++;
//                    t = t.getNextToken();
//                }

                

                while (!indentStack.empty()) {
                    int outer = indentStack.peek();
                    if (outer >= indent && currentFold != null) {
                        currentFold.setEndOffset(lastOffset);
                        Fold parentFold = currentFold.getParent();
                        // Don't add fold markers for single-line blocks
                        if (currentFold.isOnSingleLine()) {
                            removeFold(currentFold, folds);
                        }
                        currentFold = parentFold;
                        indentStack.pop();
                    } else {
                        break;
                    }
                }

                // scan until the end of the line
                while (t != null && t.isPaintable()) {
                    offset = t.getOffset();
                    t = t.getNextToken();
                }
                lastOffset = offset;

                if (currentFold == null) {
                    currentFold = new Fold(FoldType.CODE, textArea, startLine.getOffset());
                    folds.add(currentFold);
                } else {
                    currentFold = currentFold.createChild(FoldType.CODE, startLine.getOffset());
                }
                indentStack.push(indent);

            }

        } catch (BadLocationException ble) { // Should never happen
            ble.printStackTrace();
        }

        return folds;

    }

    /**
     * If this fold has a parent fold, this method removes it from its parent.
     * Otherwise, it's assumed to be the most recent (top-level) fold in the
     * <code>folds</code> list, and is removed from that.
     *
     * @param fold The fold to remove.
     * @param folds The list of top-level folds.
     */
    private static void removeFold(Fold fold, List<Fold> folds) {
        if (!fold.removeFromParent()) {
            folds.remove(folds.size() - 1);
        }
    }

}
