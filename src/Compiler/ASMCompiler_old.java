package Compiler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Compiler.*;
import static Compiler.DotCodeDecoder.getDotData;
import static Compiler.DotCodeDecoder.isDotData;
import static Compiler.StringToOpcode.stringToOpcode;
import GUI.Main_GUI;
import java.util.ArrayList;
import mips.FileWriteReader;
import static mips.processor.Memory.setMemory;

/**
 *
 * @author parke
 */
public class ASMCompiler_old {

    private static ArrayList<String> currentInstructions;
    private static byte[] memory;
    private static ArrayList<Object[]> memoryPointers;
    private static int index;

    public static ArrayList<String> getMemoryPointers() {

        if (memoryPointers == null) {
            return null;
        }

        ArrayList<String> temp = new ArrayList();

        for (Object[] pointer : memoryPointers) {
            temp.add((String) pointer[1]);
        }
        return temp;
    }

    public static void compile() {
        index = 0;
        FileWriteReader.saveASMFile();
        currentInstructions = null;
        currentInstructions = new ArrayList(FileWriteReader.getASMList());
        compileAndCleanInstructions();
        FileWriteReader.saveMXNFile();
        Main_GUI.refreshAll();
    }

    public static int getRelativeIndexOffset(String memoryPointer) {
        memoryPointer = memoryPointer.replaceAll(":", "");
        for (Object[] pointer : memoryPointers) {
            if (((String) pointer[1]).equals(memoryPointer)) {
                return ((int) pointer[0]) / 4 - (index + 4) / 4;
            }
        }
        error();
        return -1;
    }

    public static int getIndex(String memoryPointer) {
        memoryPointer = memoryPointer.replaceAll(":", "");
        for (Object[] pointer : memoryPointers) {
            if (((String) pointer[1]).equals(memoryPointer)) {
                return ((int) pointer[0]);
            }
        }
        error();
        return -1;
    }


    private static void findAndCleanMemoryPointers() {

        memoryPointers = new ArrayList();

        int index = 0;

        for (int i = 0; i < currentInstructions.size(); i++) {

            String ci = currentInstructions.get(i);

            if (ci == null) {

            } else if (ci.contains(":")) {
                memoryPointers.add(new Object[]{index, ci.replaceAll(":", "")});

                currentInstructions.remove(i);
                i--;

            } else if (ci.startsWith(".")) {
                if (isDotData(ci)) {

                    byte[] data = getDotData(ci);

                    index += data.length;
                }
                continue;

            } else {
                index += 4;
            }
        }
    }

    private static void cleanInstructionList() {
        for (int i = currentInstructions.size() - 1; i >= 0; i--) {

            String ci = currentInstructions.get(i);
            ci = ci.replaceAll("\t", "");
            ci = ci.replaceAll("\n", "");
            //ci = ci.replaceAll(Character.toString((char) 9), "");

            ci = ci.trim();

            if (!(ci.equals("") || ci == null)) {

                if (ci.contains(";")) {

                    if (ci.startsWith(";")) {
                        currentInstructions.remove(i);
                        continue;
                    } else {
                        ci = ci.split(";")[0];
                        ci = ci.trim();
                        if (ci.equals("")) {
                            currentInstructions.remove(i);
                            continue;
                        }
                    }
                }
                currentInstructions.set(i, ci);
            } else {
                currentInstructions.remove(i);
            }
        }
    }

    private static void compileAndCleanInstructions() {

        cleanInstructionList();
        findAndCleanMemoryPointers();
        compileInstructions();

    }

    public static void error() {

    }

    public static void compileInstructions() {

        for (String string : currentInstructions) {

            if (string.startsWith(".")) {
                if (isDotData(string)) {

                    byte[] data = getDotData(string);

                    for (int i = index; i < data.length + index; i++) {
                        memory[i] = data[i - index];
                    }
                    index += data.length;
                }
                continue;
            }
            try {
                int opcodeInt = stringToOpcode(string, index);
                byte[] opcode = intTo4ByteArray(opcodeInt);
                memory[index] = opcode[0];
                memory[index + 1] = opcode[1];
                memory[index + 2] = opcode[2];
                memory[index + 3] = opcode[3];
                index += 4;

            } catch (Exception e) {

            }
        }
        setMemory(memory);
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
