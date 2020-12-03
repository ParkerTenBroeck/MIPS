/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.gui.rsyntax;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Fold> getFolds(RSyntaxTextArea textArea) {

        List<Fold> folds = new ArrayList<>();

        //if(true)return folds;
        Fold currentFold = null;
        int lineCount = textArea.getLineCount();

        // closing needs to be done with offset of the previous line
        int lastOffset = 0;

        try {

            for (int line = 0; line < lineCount; line++) {

                Token t = textArea.getTokenListForLine(line);
                int offset = t.getOffset();
                if (offset < 0) {
                    continue;
                }

                while (t != null) {

                    if (t.getType() == Token.ANNOTATION) {

                        if (currentFold == null) {
                            currentFold = new Fold(FoldType.CODE, textArea, offset);
                            folds.add(currentFold);
                        } else {
                            currentFold.setEndOffset(lastOffset);
                            currentFold = new Fold(FoldType.CODE, textArea, offset);
                            folds.add(currentFold);
                        }
                        break;
                    }
                    t = t.getNextToken();
                }

                lastOffset = offset;
            }

        } catch (BadLocationException ble) { // Should never happen
            ble.printStackTrace();
        }

        return folds;

    }
}
