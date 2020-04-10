/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.DataClasses;

/**
 *
 * @author parke
 */
public class AbstractArgumentList {

    public String[] args;
    private char[] dividers;

    public AbstractArgumentList(String string) {
        abstractifyString(string, new char[]{' ', ',', '(', ')', '[', ']', '{', '}'});
    }

    public AbstractArgumentList(String string, char[] dividingSymbols) {
        abstractifyString(string, dividingSymbols);
    }

    private void abstractifyString(String string, char[] dividingSymbols) {

        int dvCount = 0;
        for (int s = 0; s < string.length(); s++) {
            for (int c = 0; c < dividingSymbols.length; c++) {
                if (string.charAt(s) == dividingSymbols[c]) {
                    dvCount++;
                }
            }
        }
        args = new String[dvCount + 1];
        dividers = new char[dvCount];

        int lastDivide = 0;
        dvCount = 0;
        for (int s = 0; s < string.length(); s++) {
            for (int c = 0; c < dividingSymbols.length; c++) {
                if (string.charAt(s) == dividingSymbols[c]) {
                    args[dvCount] = string.substring(lastDivide, s);
                    dividers[dvCount] = string.charAt(s);
                    lastDivide = s + 1;
                    dvCount++;
                }
            }
        }
        args[dividers.length] = string.substring(lastDivide, string.length());
    }

    /**
     *
     * @param s1
     * @param s2
     *
     * replaces any argument that is equal to s1 with s2
     */
    public void replaceAllFull(String s1, String s2) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(s1)) {
                args[i] = s2;
            }
        }
    }

    public String buildString() {
        String temp = "";
        for (int i = 0; i < dividers.length; i++) {
            temp += args[i] + dividers[i];
        }
        temp += args[dividers.length];
        return temp;
    }
}
