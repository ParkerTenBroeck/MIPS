/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.assembler_old;

import org.parker.mips.util.FileUtils;
import org.parker.mips.assembler_old.data.UserLine;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author parke
 */
public class DirectivesDecoder {

    private  static final Logger LOGGER = Logger.getLogger(DirectivesDecoder.class.getName());

    public static byte[] getDirectivesData(UserLine line) throws DirectiveException {

        String string = line.line;

        if (string.contains(".space")) {

            return new byte[Assembler.parseInt(string.split(" ")[1])];

        } else if (string.contains(".ascii")) {

            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(string.substring(6).trim());
            while (m.find()) {
                return m.group(1).getBytes();
            }
            throw new DirectiveException("Invalid String", line);

        } else if (string.contains(".byte")) {

            if(string.contains("\"")){
                try{
                    String filePath = string.substring(string.indexOf("\"") + 1, string.lastIndexOf("\""));

                    LOGGER.log(Level.WARNING, filePath);

                    byte[] data = FileUtils.loadFileAsByteArraySafe(new File(filePath));

                    LOGGER.log(Level.WARNING, "Done loading file: " + filePath);
                    return data;
                }catch(Exception e){
                    throw new DirectiveException("Invalid file descriptor", line, e);
                }
            }

            try {

                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length];

                for (int i = 0; i < temp.length; i++) {
                    tempByte[i] = intTo1ByteArray(Assembler.parseInt(temp[i].trim()))[0];
                }

                return tempByte;
            } catch (Exception e) {
                throw new DirectiveException("Invalid byte", line, e);
            }

        } else if (string.contains(".hword")) {

            try {
                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length * 2];

                for (int i = 0; i < temp.length; i++) {
                    byte[] tempTempByte = intTo2ByteArray(Assembler.parseInt(temp[i].trim()));
                    tempByte[(i * 2)] = tempTempByte[0];
                    tempByte[(i * 2) + 1] = tempTempByte[1];

                }

                return tempByte;
            } catch (Exception e) {
                throw new DirectiveException("Invalid half word", line, e);
            }

        } else if (string.contains(".word")) {

            try {
                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length * 4];

                for (int i = 0; i < temp.length; i++) {
                    byte[] tempTempByte = intTo4ByteArray(Assembler.parseInt(temp[i].trim()));
                    tempByte[(i * 4)] = tempTempByte[0];
                    tempByte[(i * 4) + 1] = tempTempByte[1];
                    tempByte[(i * 4) + 2] = tempTempByte[2];
                    tempByte[(i * 4) + 3] = tempTempByte[3];

                }

                return tempByte;
            } catch (Exception e) {
                throw new DirectiveException("Invalid word", line, e);
            }

        }
        throw new DirectiveException("Invalid Directive", line);
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
