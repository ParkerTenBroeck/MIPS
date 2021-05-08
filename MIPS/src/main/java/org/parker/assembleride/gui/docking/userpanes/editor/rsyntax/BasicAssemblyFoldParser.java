/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.assembleride.gui.docking.userpanes.editor.rsyntax;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldType;

import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author parke
 */
public class BasicAssemblyFoldParser implements FoldParser {

    /**
     * Constructor.
     *
     */
    public BasicAssemblyFoldParser() {
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
                    }else if(t.getType() == Token.PREPROCESSOR){

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
