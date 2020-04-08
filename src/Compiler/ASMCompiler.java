/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

import GUI.Main_GUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import mips.FileWriteReader;
import mips.Log;
import mips.processor.Memory;

class ByteP {

    public byte b;

    public ByteP() {

    }

    public ByteP(byte b) {
        this.b = b;
    }

};

abstract class CompileTimeUserLine {

    UserLine line;
    ByteP bytes[];
    int startingByteAddress;

    public abstract int getByteSize();

    public abstract void finalCompilePass();
};

class dotData extends CompileTimeUserLine {//must have instruction and data preented at the time of creation

    public dotData(UserLine us, byte[] _bytes) {
        this.line = us;
        this.bytes = new ByteP[_bytes.length];
        for (int i = 0; i < _bytes.length; i++) {
            this.bytes[i] = new ByteP(_bytes[i]);
        }
    }

    @Override
    public int getByteSize() {
        return this.bytes.length;
    }

    @Override
    public void finalCompilePass() { //possibly add error checking 

    }

};

class asmInstruction extends CompileTimeUserLine {

    public asmInstruction(UserLine us) {
        this.line = us;
        bytes = new ByteP[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = new ByteP((byte) 0xcd);
        }
    }

    @Override
    public int getByteSize() {
        return 4; //instruction size is always 4
    }

    @Override
    public void finalCompilePass() {
        byte[] temp = StringToOpcode.stringToOpcode(this);

        if (bytes.length != temp.length) {
            //error
            return;
        }

        for (int i = 0; i < temp.length; i++) {
            bytes[i].b = temp[i];
        }
    }

};

class UserLine {

    public String line;
    public int realLineNumber;

    public UserLine(String line) {
        this.line = line;
    }

    public UserLine(int realLineNumber) {
        this.realLineNumber = realLineNumber;
    }

    public UserLine(String line, int realLineNumber) {
        this.line = line;
        this.realLineNumber = realLineNumber;
    }

    public UserLine() {
    }
}

class MemoryChunk {

    MemoryLable startLable;
    ArrayList<CompileTimeUserLine> chunkData;

    public MemoryChunk(MemoryLable lable) {
        this.startLable = lable;
        this.chunkData = new ArrayList();
    }

    public void addData(CompileTimeUserLine ctul) {
        this.chunkData.add(ctul);
    }

    public int getSize() {
        int size = 0;
        for (int i = 0; i < chunkData.size(); i++) {
            size += chunkData.get(i).getByteSize();
        }
        return size;
    }

}

class Origin {

    final int byteOrigin;
    ArrayList<MemoryChunk> memoryChunks;

    public Origin(int byteOrigin) {
        this.byteOrigin = byteOrigin;
        memoryChunks = new ArrayList();
    }

    public Origin(UserLine line) {
        this.byteOrigin = ASMCompiler.parseInt(line.line.split(" ")[1]);
        memoryChunks = new ArrayList();
    }

    public void addMemoryChunk(MemoryChunk mc) {
        memoryChunks.add(mc);
    }
};

public class ASMCompiler {

    static private ArrayList<MemoryLable> memoryLables;
    static private ArrayList<Origin> origins;
    static private ArrayList<ByteP> memoryByteList;

    public static void compile() {

        memoryLables = new ArrayList();
        memoryByteList = new ArrayList();
        origins = new ArrayList();

        FileWriteReader.saveASMFile();
        ArrayList<UserLine> temp = getInstructions();

        temp = PreProcessor.preProcess(temp, Main_GUI.savePreProcessedFile());

        findMemoryPointersAndChunkifyMemory(temp);

        addOriginsToByteList(); //adds instructions to bytelist in order according to file and origins and sets the respecting addresses of memory lables

        runFinalCompilePass(); //compiles code once memory lable locations are known

        Memory.setMemory(createByteArrayFromByteList());

        if (Main_GUI.saveCompilationInfo()) {
            saveOriginsToFile();
        }

        FileWriteReader.saveMXNFile();
        Main_GUI.refreshAll();

        temp.clear();
        memoryByteList.clear();
        origins.clear();
    }

    public static void saveOriginsToFile() {

        int maxSizeMemoryLable = 0;
        int maxSizeInstruction = 0;

        for (MemoryLable ml : memoryLables) {
            if (ml.name.length() > maxSizeMemoryLable) {
                maxSizeMemoryLable = ml.name.length();
            }
        }
        maxSizeMemoryLable++;

        for (Origin org : origins) {
            for (MemoryChunk mc : org.memoryChunks) {
                for (CompileTimeUserLine ctul : mc.chunkData) {
                    if (ctul.line.line.length() > maxSizeInstruction) {
                        maxSizeInstruction = ctul.line.line.length();
                    }
                }

            }
        }
        maxSizeInstruction++;

        File file = new File("CompilationInfo.txt");

        try (PrintWriter out = new PrintWriter(file)) {

            out.println("Compilation Info of File: " + FileWriteReader.getASMFilePath());
            out.println();

            for (Origin org : origins) {

                if (org.byteOrigin == 0 && org.memoryChunks.isEmpty()) {//skips pre made origin if it is not in ise
                    continue;
                }

                out.println("Origin at: " + String.format("%08X", org.byteOrigin));
                out.println();

                for (MemoryChunk mc : org.memoryChunks) {

                    if (mc.startLable.name.equals("") && mc.chunkData.isEmpty()) { //skips pre made lable if it is empty
                        continue;
                    }

                    out.println("   " + "Memory Lable: "
                            + String.format("%-" + maxSizeMemoryLable + "s", mc.startLable.name + ",")
                            + " Memory Adress: " + String.format("%08X", mc.startLable.byteAddress));
                    out.println();

                    for (CompileTimeUserLine ctul : mc.chunkData) {

                        out.print("       " + "Instruction/Data: "
                                + String.format("%-" + maxSizeInstruction + "s", ctul.line.line)
                                + " Starting Memory Adress: "
                                + String.format("%08X", ctul.startingByteAddress) + " Bytes: ");

                        for (ByteP b : ctul.bytes) {
                            out.print(String.format("%02X", ((int) b.b) & 0xFF) + " ");
                        }
                        out.println();
                    }
                    out.println();
                }

                out.println();

            }

            ASMCompiler.logCompilerMessage("Compilation Info File Wrote to: " + file.getAbsolutePath());
            out.flush();
        } catch (Exception e) {
            ASMCompiler.logCompilerError("Unable to write Pre Processed File to: " + file.getAbsolutePath() + " " + e.getMessage());
        }
    }

    public static byte[] createByteArrayFromByteList() {
        byte[] temp = new byte[memoryByteList.size()];

        for (int i = 0; i < memoryByteList.size(); i++) {
            try {
                temp[i] = memoryByteList.get(i).b;
            } catch (Exception e) {
                temp[i] = (byte) 0xcd;
            }
        }
        return temp;
    }

    public static int getByteIndexOfMemoryLable(String memoryLable, int realLineNumberOfOpCode) {
        for (int i = 0; i < memoryLables.size(); i++) {
            if (memoryLables.get(i).name.equals(memoryLable)) {
                return memoryLables.get(i).byteAddress;
            }
        }
        ASMCompiler.MemoryLableError("Memory Lable does not exist", realLineNumberOfOpCode);
        return -1;
    }

    private static void runFinalCompilePass() { //for every CompileTimeUserLine (instruction) run the final compilation pass 
        for (Origin org : origins) {
            for (MemoryChunk mc : org.memoryChunks) {
                for (CompileTimeUserLine ctul : mc.chunkData) {
                    ctul.finalCompilePass();
                }
            }
        }
    }

    private static void addOriginsToByteList() {

        int currentByteIndex;

        for (Origin org : origins) { //for all origins
            currentByteIndex = org.byteOrigin; //sets the starting byte address

            for (MemoryChunk mc : org.memoryChunks) { //for all memory chunks inside origin

                try {
                    while (memoryByteList.get(currentByteIndex) != null) { //keeps looking for a new locaiton
                        currentByteIndex++;
                    }
                } catch (Exception e) {
                    //does not matter if it doesnt exist
                }
                mc.startLable.byteAddress = currentByteIndex; //sets the byte address of the chunks memory lable

                for (CompileTimeUserLine ctul : mc.chunkData) {

                    try {
                        while (memoryByteList.get(currentByteIndex) != null) { //keeps looking for a new locaiton
                            currentByteIndex++;
                        }
                    } catch (Exception e) {
                        //does not matter if it doesnt exist
                    }
                    ctul.startingByteAddress = currentByteIndex;

                    for (ByteP b : ctul.bytes) { //for every byte insize the CompuleTimeUserLine add to byteList

                        try {
                            while (memoryByteList.get(currentByteIndex) != null) { //keeps looking for a new locaiton
                                currentByteIndex++;
                            }
                        } catch (Exception e) {
                            //does not matter if it doesnt exist
                        }
                        while (memoryByteList.size() < currentByteIndex + 1) {
                            memoryByteList.add(null);
                        }
                        memoryByteList.set(currentByteIndex, b);
                        currentByteIndex++;
                    }
                }
            }
        }
    }
//    private static int getSizeOfMemoryChunks() { //out dated should not use
//        int size = 0;
//
//        for (int i = 0; i < memoryChunks.size(); i++) {
//            size += memoryChunks.get(i).getSize();
//        }
//        return size;
//    }

    private static void findMemoryPointersAndChunkifyMemory(ArrayList<UserLine> file) { //finds all memory pointers and splits the file into memory chunks

        int memoryLableIndex = 0;
        MemoryLable ml;
        MemoryChunk mc = new MemoryChunk(new MemoryLable(new UserLine("", -1), -1));
        Origin org = new Origin(0);

        for (int i = 0; i < file.size(); i++) {

            UserLine currentLine = file.get(i);

            if (currentLine.line.contains(":")) { //if theres a new memoryLable add last memory chunk to memoryChunks and create a new current memoryChunk and add the new memory lable to memoryLables
                org.addMemoryChunk(mc);
                ml = new MemoryLable(currentLine, memoryLableIndex);
                mc = new MemoryChunk(ml);
                addMemoryLable(ml);
                memoryLableIndex++;
            } else if (currentLine.line.startsWith(".org")) { //adds origin to list of origins and 
                org.addMemoryChunk(mc);
                addOrigin(org);
                org = new Origin(currentLine);
                mc = new MemoryChunk(new MemoryLable(new UserLine("", -1), -1));
            } else {
                mc.addData(userLineToCompileTimeUserLine(currentLine));
            }
        }
        org.addMemoryChunk(mc); //adds last data
        addOrigin(org);
    }

    private static void addOrigin(Origin org) {
        origins.add(0, org);
    }

    private static void addMemoryLable(MemoryLable ml) {

        for (int i = 0; i < memoryLables.size(); i++) {
            if (ml.name.equals(memoryLables.get(i).name)) {
                ASMCompiler.MemoryLableError("Cannot have Duplicate Memory Lables", ml.line.realLineNumber);
                return;
            }
        }
        memoryLables.add(ml);
    }

    private static CompileTimeUserLine userLineToCompileTimeUserLine(UserLine line) {

        if (line.line.startsWith(".")) {
            return new dotData(line, DotCodeDecoder.getDotData(line)); //
        } else {
            return new asmInstruction(line);
        }
    }

    private static ArrayList<UserLine> getInstructions() {
        BufferedReader reader;
        int lineNumber = 0;

        ArrayList<UserLine> file = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(FileWriteReader.getASMFilePath()));
            String line = reader.readLine();
            while (line != null) {

                file.add(new UserLine(line, lineNumber));
                lineNumber++;
                line = reader.readLine();
            }
        } catch (Exception e) {
            //error
        }
        return file;
    }

    public static void PreProcessorError(String message, int line) {

        logCompilerError("[PreProcessor]: on line " + line + " " + message);
    }

    public static void DotCodeDecoderError(String message, int line) {
        logCompilerError("[DotCode]: on line " + line + " " + message);
    }

    public static void OpCodeError(String message, int line) {
        logCompilerError("[OpCode]: on line " + line + " " + message);
    }

    public static void MemoryLableError(String message, int line) {
        logCompilerError("[MemoryLable]: on line " + line + " " + message);
    }

    public static void ArgumentError(String message, int line) {
        logCompilerError("[Argument]: on line " + line + " " + message);
    }

    public static void logCompilerError(String message) {
        Log.logError("[Compiler]" + message);
    }

    public static void logCompilerMessage(String message) {
        Log.logMessage("[Compiler] " + message);
    }

    public static int parseInt(String string) { //to add functionality later

        if (string.contains("x")) {
            return Integer.parseInt(string.split("x")[1], Integer.parseInt(string.split("x")[0]));
        }

        return Integer.parseInt(string.trim());
    }

    public static ArrayList<MemoryLable> getMemoryLables() {
        return (ArrayList<MemoryLable>) memoryLables.clone();
    }
}
