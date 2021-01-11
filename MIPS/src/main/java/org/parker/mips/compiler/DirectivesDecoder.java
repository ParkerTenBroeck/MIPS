/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.compiler;

import org.parker.mips.compiler.data.UserLine;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.parker.mips.Log;

/**
 *
 * @author parke
 */
public class DirectivesDecoder {

//    public static boolean isDotData(String string) { //should not be used
//        if (string.contains(".ascii") || string.contains(".byte") || string.contains(".hword") || string.contains(".word") || string.contains(".space")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    
    public static byte[] getDirectivesData(UserLine line) {

        String string = line.line;

        if (string.contains(".space")) {

            return new byte[ASMCompiler.parseInt(string.split(" ")[1])];

        } else if (string.contains(".ascii")) {

            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(string.substring(6).trim());
            while (m.find()) {
                return m.group(1).getBytes();
            }
            ASMCompiler.DirectivesDecoderError("Invalid string", line.realLineNumber);
            return new byte[0];

        } else if (string.contains(".byte")) {

            try {

                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length];

                for (int i = 0; i < temp.length; i++) {
                    tempByte[i] = intTo1ByteArray(ASMCompiler.parseInt(temp[i].trim()))[0];
                }

                return tempByte;
            } catch (Exception e) {
                ASMCompiler.DirectivesDecoderError("Invalid byte:\n" + Log.getFullExceptionMessage(e) , line.realLineNumber);
                return new byte[0];
            }

        } else if (string.contains(".hword")) {

            try {
                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length * 2];

                for (int i = 0; i < temp.length; i++) {
                    byte[] tempTempByte = intTo2ByteArray(ASMCompiler.parseInt(temp[i].trim()));
                    tempByte[(i * 2)] = tempTempByte[0];
                    tempByte[(i * 2) + 1] = tempTempByte[1];

                }

                return tempByte;
            } catch (Exception e) {
                ASMCompiler.DirectivesDecoderError("Invalid half word\n" + Log.getFullExceptionMessage(e), line.realLineNumber);
                return new byte[0];
            }

        } else if (string.contains(".word")) {

            try {
                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length * 4];

                for (int i = 0; i < temp.length; i++) {
                    byte[] tempTempByte = intTo4ByteArray(ASMCompiler.parseInt(temp[i].trim()));
                    tempByte[(i * 4)] = tempTempByte[0];
                    tempByte[(i * 4) + 1] = tempTempByte[1];
                    tempByte[(i * 4) + 2] = tempTempByte[2];
                    tempByte[(i * 4) + 3] = tempTempByte[3];

                }

                return tempByte;
            } catch (Exception e) {
                ASMCompiler.DirectivesDecoderError("Invalid word:\n" + Log.getFullExceptionMessage(e), line.realLineNumber);
                return new byte[0];
            }

        }
        ASMCompiler.DirectivesDecoderError("invalid dotCode", line.realLineNumber);
        return new byte[0];
    }
    
        public static final byte[] intTo4ByteArray(int value) {
        return new byte[]{
            (byte) (value >>> 24),
            (byte) (value >>> 16),
            (byte) (value >>> 8),
            (byte) value};
    }

    public static final byte[] intTo2ByteArray(int value) {
        return new byte[]{
            (byte) (value >>> 8),
            (byte) value};
    }

    public static final byte[] intTo1ByteArray(int value) {
        return new byte[]{
            (byte) value};
    }
}
