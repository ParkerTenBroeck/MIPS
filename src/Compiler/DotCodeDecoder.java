/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

import static Compiler.ASMCompiler.intTo1ByteArray;
import static Compiler.ASMCompiler.intTo2ByteArray;
import static Compiler.ASMCompiler.intTo4ByteArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author parke
 */
public class DotCodeDecoder {

    public static int getOrg(String string) {

        if (!string.contains(".org")) {
            return -1;
        }

        string = string.trim();
        try {
            return Integer.parseInt(string.split(" ")[1]);
        } catch (Exception e) {
            ASMCompiler.error("Invalid origin");
            return -1;
        }
    }

    public static boolean isDotData(String string) {
        if (string.contains(".ascii") || string.contains(".byte") || string.contains(".hword") || string.contains(".word") || string.contains(".space")) {
            return true;
        } else {
            return false;
        }
    }

    static byte[] getDotData(String string) {

        if (string.contains(".space")) {

            return new byte[Integer.parseInt(string.split(" ")[1])];

        } else if (string.contains(".ascii")) {

            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(string.substring(6).trim());
            while (m.find()) {
                return m.group(1).getBytes();
            }
            ASMCompiler.error("Invalid string");
            return new byte[0];

        } else if (string.contains(".byte")) {

            try {

                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length];

                for (int i = 0; i < temp.length; i++) {
                    tempByte[i] = intTo1ByteArray(Integer.parseInt(temp[i].trim()))[0];
                }

                return tempByte;
            } catch (Exception e) {
                ASMCompiler.error("Invalid byte");
                return new byte[0];
            }

        } else if (string.contains(".hword")) {

            try {
                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length * 2];

                for (int i = 0; i < temp.length; i++) {
                    byte[] tempTempByte = intTo2ByteArray(Integer.parseInt(temp[i].trim()));
                    tempByte[(i * 2)] = tempTempByte[0];
                    tempByte[(i * 2) + 1] = tempTempByte[1];

                }

                return tempByte;
            } catch (Exception e) {
                ASMCompiler.error("Invalid half word");
                return new byte[0];
            }

        } else if (string.contains(".word")) {

            try {
                String[] temp = string.substring(string.indexOf(" ")).split(",");

                byte[] tempByte = new byte[temp.length * 4];

                for (int i = 0; i < temp.length; i++) {
                    byte[] tempTempByte = intTo4ByteArray(Integer.parseInt(temp[i].trim()));
                    tempByte[(i * 4)] = tempTempByte[0];
                    tempByte[(i * 4) + 1] = tempTempByte[1];
                    tempByte[(i * 4) + 2] = tempTempByte[2];
                    tempByte[(i * 4) + 3] = tempTempByte[3];

                }

                return tempByte;
            } catch (Exception e) {
                ASMCompiler.error("Invalid word");
                return new byte[0];
            }

        }
        ASMCompiler.error("invalid dotCode");
        return new byte[0];
    }
}
