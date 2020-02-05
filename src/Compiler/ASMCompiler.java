/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

import static Compiler.DotCodeDecoder.getDotData;
import static Compiler.DotCodeDecoder.isDotData;
import static Compiler.StringToOpcode.stringToOpcode;
import GUI.Main_GUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mips.FileWriteReader;
import mips.processor.Memory;

/**
 *
 * @author parke
 */
public class ASMCompiler {

    private static ArrayList<Object[]> currentInstructions;
    private static final List<Byte> memory = new ArrayList<Byte>();
    private static ArrayList<Object[]> memoryPointers;
    private static int memoryIndex;
    private static int realIndex;
    private static boolean stopFlag = false;

    private static ArrayList<PreProcessor> preProcessor;

    public static void compile() {
        stopFlag = false;
        memoryIndex = 0;
        realIndex = 0;
        memory.clear();

        FileWriteReader.saveASMFile();
        getInstructions();

        compileAndCleanInstructions();

        FileWriteReader.saveMXNFile();
        Main_GUI.refreshAll();
    }

    public static String findAndReplacePreProcessorValue(String parameter) {
        for (PreProcessor pre : preProcessor) {
            if (pre.getType() == PreProcessor.Type.VALUE) {
                if (pre.getName().equals(parameter)) {
                    return pre.getValue();
                }
            }
        }
        return parameter;
    }

    private static void getInstructions() {
        currentInstructions = new ArrayList();
        ArrayList<String> temp = new ArrayList(FileWriteReader.getASMList());

        for (int i = 0; i < temp.size(); i++) {
            currentInstructions.add(new Object[]{temp.get(i), (int) i});
        }
    }

    public static ArrayList<String> getMemoryPointers() {

        if (memoryPointers == null) {
            return null;
        }

        ArrayList<String> temp = new ArrayList();

        for (Object[] pointer : memoryPointers) {
            temp.add((String) pointer[1]);
        }
        return temp;
    } //only used for GUI

    public static int getRelativeIndexOffset(String memoryPointer) {
        memoryPointer = memoryPointer.replaceAll(":", "");
        for (Object[] pointer : memoryPointers) {
            if (((String) pointer[1]).equals(memoryPointer)) {
                return ((int) pointer[0]) / 4 - (memoryIndex + 4) / 4;
            }
        }
        error("Memory pointer not found");
        return -1;
    }

    public static int getIndex(String memoryPointer) {
        memoryPointer = memoryPointer.replaceAll(":", "");
        for (Object[] pointer : memoryPointers) {
            if (((String) pointer[1]).equals(memoryPointer)) {
                return ((int) pointer[0]);
            }
        }
        error("Memory pointer not found");
        return -1;
    }

    private static void findAndCleanPreProcessors() {

//        preProcessorCode = new ArrayList();
//        preProcessorValues = new ArrayList();
//
//        for (int i = 0; i < currentInstructions.size(); i++) {
//
//            String line = (String) currentInstructions.get(i)[0];
//            int lineNumber = (int) currentInstructions.get(i)[1];
//
//            if (line.startsWith("#define") && line.contains("[")) {
//
//                currentInstructions.remove(i);
//                PreProcessor temp = new PreProcessor(line);
//                line = (String) currentInstructions.get(i)[0];
//                lineNumber = (int) currentInstructions.get(i)[1];
//
//                while (true) {
//                    if (line.contains("]")) {
//                        currentInstructions.remove(i);
//                        i = -1;
//                        break;
//                    }
//                    temp.addLine(line);
//
//                    currentInstructions.remove(i);
//                    try {
//                        line = (String) currentInstructions.get(i)[0];
//                        lineNumber = (int) currentInstructions.get(i)[1];
//
//                        if (line.contains("#") || line.contains(":")) {
//                            ASMCompiler.error("Define never closed");
//                        }
//
//                    } catch (Exception e) {
//                        ASMCompiler.error("Define never closed");
//                    }
//                }
//                //currentInstructions.remove(i);
//                preProcessorCode.add(temp);
//                findPreProcessors();
//            }
//            
//            if (line.startsWith("#define")) {
//                try {
//                    String name = line.split(" ")[1];
//                    String thingToReplace = line.split(" ")[2];
//                    currentInstructions.remove(i);
//                    i --;
//
//                    for (int l = 0; l < currentInstructions.size(); l++) {
//                        currentInstructions.get(l)[0] = ((String) currentInstructions.get(l)[0]).replace(name, thingToReplace);
//                    }
//                } catch (Exception e) {
//                    ASMCompiler.error("Invalid define");
//                }
//            }
//
//        }
//        
//        for(Object[] o: currentInstructions){
//        }
        preProcessor = new ArrayList();
        PreProcessor nextPreProcessor;
        while ((nextPreProcessor = PreProcessor.nextPreProcessor(currentInstructions)) != null) {
            for (PreProcessor pre : preProcessor) {
                realIndex = pre.getRealLine();
                if (pre.getName().equals(nextPreProcessor.getName())) {
                    ASMCompiler.error("Pre Processor cannot have the same name");
                }
            }
            preProcessor.add(nextPreProcessor);
        }
        realIndex = 0;

    }

//    private static void findPreProcessors() {
//
//        for (int i = 0; i < currentInstructions.size(); i++) {
//            String line = (String) currentInstructions.get(i)[0];
//            int lineNumber = (int) currentInstructions.get(i)[1];
//            //while((PreProcessor p = new PreProcessor(line)) != null){}
//
//            for (int p = 0; p < preProcessorCode.size(); p++) {
//
//                if (line.split("\\(")[0].trim().equals(preProcessorCode.get(p).getName())) {
//
//                    currentInstructions.remove(i);
//
//                    ArrayList<String> linesToAdd = preProcessorCode.get(p).getInstructionsWithArgs(line);
//
//                    ArrayList<Object[]> temp = new ArrayList();
//                    for (int l = 0; l < linesToAdd.size(); l++) {
//                        temp.add(new Object[]{linesToAdd.get(l), lineNumber});
//
//                    }
//                    if (temp.size() > 0) {
//                        currentInstructions.addAll(i, temp);
//                    } else {
//                        ASMCompiler.error("Define cannot by empty");
//                    }
//
//                }
//            }
//        }
//    }
    private static void findAndCleanMemoryPointers() {

        memoryPointers = new ArrayList();

        int index = 0;

        for (int i = 0; i < currentInstructions.size(); i++) {

            String ci = (String) currentInstructions.get(i)[0];

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

            String ci = (String) currentInstructions.get(i)[0];
            ci = ci.replaceAll("\t", "");
            ci = ci.replaceAll("\n", "");
            ci = ci.trim().replaceAll("\\s+", " ");

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
                currentInstructions.set(i, new Object[]{ci, (int) currentInstructions.get(i)[1]});
            } else {
                currentInstructions.remove(i);
            }
        }
    }

    private static void compileAndCleanInstructions() {

        cleanInstructionList();
        findAndCleanPreProcessors();
        findAndCleanMemoryPointers();
        compileInstructions();

    }

    public static void error(String string) {
        Main_GUI.infoBox("Error", "line " + (realIndex + 1) + "; " + string);
        stopFlag = true;
    }

    public static void compileInstructions() {

        for (Object[] line : currentInstructions) {

            String string = (String) line[0];
            realIndex = (int) line[1];

            if (string.startsWith(".")) { //dote codes 
                if (isDotData(string)) {

                    byte[] data = getDotData(string);

                    for (int i = memoryIndex; i < data.length + memoryIndex; i++) {
                        memory.add(i, data[i - memoryIndex]);
                    }
                    memoryIndex += data.length;
                }
                continue;
            } else {

                try {
                    int opcodeInt = stringToOpcode(string, memoryIndex);
                    byte[] opcode = intTo4ByteArray(opcodeInt);
                    memory.add(memoryIndex, opcode[0]);
                    memory.add(memoryIndex + 1, opcode[1]);
                    memory.add(memoryIndex + 2, opcode[2]);
                    memory.add(memoryIndex + 3, opcode[3]);
                    memoryIndex += 4;

                } catch (Exception e) {
                    ASMCompiler.error("Error parsing string");
                }
            }
        }
        byte[] myStuff = new byte[memory.size()];
        for (int i = 0; i < memory.size(); i++) {
            myStuff[i] = memory.get(i);
        }
        Memory.setMemory(myStuff);
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
