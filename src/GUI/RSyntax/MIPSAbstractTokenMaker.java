/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.RSyntax;

import Compiler.PreProcessor;
import Compiler.PreProcessorStatements.Statement;
import Compiler.StringToOpcode;
import javax.swing.text.Segment;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

/**
 *
 * @author parke
 */
public class MIPSAbstractTokenMaker extends AbstractTokenMaker {

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap();

        for (String instruction : StringToOpcode.INSTRUCTIONS) {
            tokenMap.put(instruction, Token.RESERVED_WORD);
        }
        for (Statement s : PreProcessor.validStatements) {
            tokenMap.put("#" + s.STATEMENT_NAME, Token.MARKUP_PROCESSING_INSTRUCTION);
        }

        tokenMap.put(".byte", Token.DATA_TYPE);
        tokenMap.put(".hword", Token.DATA_TYPE);
        tokenMap.put(".word", Token.DATA_TYPE);
        tokenMap.put(".ascii", Token.DATA_TYPE);
        tokenMap.put(".space", Token.DATA_TYPE);

        tokenMap.put(".org", Token.RESERVED_WORD_2);

        tokenMap.put("$", Token.VARIABLE);

        tokenMap.put(":", Token.ANNOTATION);

        tokenMap.put(",", Token.SEPARATOR);
        tokenMap.put("(", Token.SEPARATOR);
        tokenMap.put(")", Token.SEPARATOR);

        //tokenMap.put(",", Token.SEPARATOR);
        //tokenMap.put("(", Token.SEPARATOR);
        //tokenMap.put(")", Token.SEPARATOR);
        //tokenMap.put("", 0);
        return tokenMap;
    }

    @Override
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
        // This assumes all keywords, etc. were parsed as "identifiers."
        if (tokenType == Token.IDENTIFIER) {
            int value = wordsToHighlight.get(segment, start, end);
            if (value != -1) {
                tokenType = value;
            }
        }
        super.addToken(segment, start, end, tokenType, startOffset);
    }

    /**
     * Returns a list of tokens representing the given text.
     *
     * @param text The text to break into tokens.
     * @param startTokenType The token with which to start tokenizing.
     * @param startOffset The offset at which the line of tokens begins.
     * @return A linked list of tokens representing <code>text</code>.
     */
    @Override
    public Token getTokenList(Segment text, int startTokenType, int startOffset) {

        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        // Token starting offsets are always of the form:
        // 'startOffset + (currentTokenStart-offset)', but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart'.
        int newStartOffset = startOffset - offset;

        int currentTokenStart = offset;
        int currentTokenType = startTokenType;

        for (int i = offset; i < end; i++) {

            char c = array[i];
            //System.out.println(currentToken);
            switch (currentTokenType) {

                case Token.NULL:
                    //System.out.println(currentToken);
                    currentTokenStart = i;   // Starting a new token here.

                    switch (c) {

                        case ' ':
                        case '\t':
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case ';':
                        //case '#':
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case '$':
                            currentTokenType = Token.VARIABLE;
                            break;

                        default:
                            if (RSyntaxUtilities.isDigit(c)) {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            } else if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
                                if (previousToken == null) {
                                    currentTokenType = Token.IDENTIFIER;
                                } else {
                                    currentTokenType = Token.IDENTIFIER;
                                }

                                //currentTokenType = Token.MARKUP_PROCESSING_INSTRUCTION;
                                break;
                            }

                            // Anything not currently handled - mark as an identifier
                            currentTokenType = Token.IDENTIFIER;
                            break;

                    } // End of switch (c).

                    break;

                case Token.WHITESPACE:

                    switch (c) {

                        case ' ':
                        case '\t':
                            break;   // Still whitespace.

                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case ';':
                        //case '#':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case '$':
                            currentTokenType = Token.VARIABLE;
                            break;

                        case ',':
                        case ')':
                        case '(':
                            currentTokenType = Token.SEPARATOR;
                            break;

                        default:   // Add the whitespace token and start anew.

                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;

                            if (RSyntaxUtilities.isDigit(c) | c == '-') {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            } else if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
                                currentTokenType = Token.MARKUP_TAG_ATTRIBUTE;
                                break;
                            }

                            // Anything not currently handled - mark as identifier
                            currentTokenType = Token.IDENTIFIER;

                    } // End of switch (c).

                    break;

                case Token.MARKUP_TAG_ATTRIBUTE:
                    switch (c) {
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart, i - 1, Token.MARKUP_TAG_ATTRIBUTE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.MARKUP_TAG_ATTRIBUTE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case ',':
                        case '(':
                        case ')':
                            addToken(text, currentTokenStart, i - 1, Token.MARKUP_TAG_ATTRIBUTE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.SEPARATOR;
                            break;

                        case ';':
                        //case '#':
                            addToken(text, currentTokenStart, i - 1, Token.MARKUP_TAG_ATTRIBUTE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;
                    }
                    break;

                default: // Should never happen
                case Token.IDENTIFIER:
//System.out.println("ss");
                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case ';':
                        //case '#':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case ',':
                        case '(':
                        case ')':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.SEPARATOR;
                            break;

                        case ':':
                            currentTokenType = Token.ANNOTATION;
                            break;

                        default:
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c == '/' || c == '_') {
                                break;   // Still an identifier of some type.
                            }
                        // Otherwise, we're still an identifier (?).

                    } // End of switch (c).

                    break;

                case Token.LITERAL_NUMBER_DECIMAL_INT:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case ',':
                        case '(':
                        case ')':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.SEPARATOR;
                            break;

                        case ';':
                        //case '#':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case 'b':
                        case 'x':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_HEXADECIMAL, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_NUMBER_HEXADECIMAL;
                            break;

                        default:

                            if (RSyntaxUtilities.isDigit(c) | c == '-') {
                                break;   // Still a literal number.
                            }

                            // Otherwise, remember this was a number and start over.
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;

                    } // End of switch (c).

                    break;

                case Token.LITERAL_NUMBER_HEXADECIMAL:
                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_HEXADECIMAL, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_HEXADECIMAL, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;

                        case ';':
                        //case '#':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_HEXADECIMAL, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case ',':
                        case '(':
                        case ')':
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_HEXADECIMAL, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.SEPARATOR;
                            break;

                        default:

                            if (RSyntaxUtilities.isDigit(c) | RSyntaxUtilities.isLetter(c) | c == '-') {
                                break;   // Still a literal number.
                            }

                            // Otherwise, remember this was a number and start over.
                            addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_HEXADECIMAL, newStartOffset + currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;

                    } // End of switch (c).
                    break;

                case Token.VARIABLE:

                    switch (c) {

                        case ';':
                        //case '#':
                            addToken(text, currentTokenStart, i - 1, Token.VARIABLE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        default:
                            if (RSyntaxUtilities.isDigit(c) || RSyntaxUtilities.isLetter(c)) {
                                currentTokenType = Token.VARIABLE;
                                break;
                            } else {
                                addToken(text, currentTokenStart, i - 1, Token.VARIABLE, newStartOffset + currentTokenStart);
                                i--;
                                currentTokenType = Token.NULL;

                            }
                    }

                    break;

                case Token.ANNOTATION:
                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart, i - 1, Token.ANNOTATION, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case ';':
                        //case '#':
                            addToken(text, currentTokenStart, i - 1, Token.ANNOTATION, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        case ':':
                            if (previousToken == null) {
                                addToken(text, currentTokenStart, i - 1, Token.ANNOTATION, newStartOffset + currentTokenStart);
                                i--;
                                currentTokenType = Token.NULL;
                                break;
                            } else if (previousToken.getType() == Token.NULL) {
                                previousToken.setType(Token.ANNOTATION);
                                addToken(text, currentTokenStart, i - 1, Token.ANNOTATION, newStartOffset + currentTokenStart);
                                i--;
                                currentTokenType = Token.NULL;
                                break;
                            }
                            //addToken(text, currentTokenStart, i - 1, Token.ANNOTATION, newStartOffset + currentTokenStart);
                            //i--;
                            //currentTokenType = Token.NULL;
                            break;

                        default:
                            //addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            //i--;
                            //currentTokenType = Token.NULL;
                            if (RSyntaxUtilities.isLetter(c)) {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }

                    }
                    break;

                case Token.SEPARATOR:
                    switch (c) {
                        case ',':
                        case '(':
                        case ')':
                            addToken(text, currentTokenStart, i - 1, Token.SEPARATOR, newStartOffset + currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;
                            break;
                        default:
                            addToken(text, currentTokenStart, i - 1, Token.SEPARATOR, newStartOffset + currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;
                    }
                    break;

                case Token.COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = Token.NULL;
                    break;

                case Token.LITERAL_STRING_DOUBLE_QUOTE:
                    if (c == '"') {
                        addToken(text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset + currentTokenStart);
                        currentTokenType = Token.NULL;
                    }
                    break;

            } // End of switch (currentTokenType).

        } // End of for (int i=offset; i<end; i++).

        switch (currentTokenType) {

            // Remember what token type to begin the next line with.
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
                addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
                break;

            // Do nothing if everything was okay.
            case Token.NULL:
                addNullToken();
                break;

            // All other token types don't continue to the next line...
            default:
                addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
                addNullToken();

        }

        // Return the first token in our linked list.
        return firstToken;

    }
}
