/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.compiler;

import org.parker.mips.FileUtils;
import org.parker.mips.OptionsHandler;
import org.parker.mips.ResourceHandler;
import org.parker.mips.compiler.data.MemoryLable;
import org.parker.mips.compiler.data.UserLine;
import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.editor.EditorHandler;
import org.parker.mips.processor.Memory;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

class ByteP {

    public byte b;

    public ByteP() {

    }

    public ByteP(byte b) {
        this.b = b;
    }

};

abstract class CompileTimeUserLine {

    public final UserLine ul;
    public final ByteP bytes[];
    int startingByteAddress;

    protected CompileTimeUserLine(UserLine ul, ByteP[] bytes) {
        this.ul = ul;
        this.bytes = bytes;
    }

    public final int getByteSize() {
        return this.bytes.length;
    }

    public abstract void finalCompilePass() throws CompilationException;
};

class Directive extends CompileTimeUserLine {//must have instruction and data preented at the time of creation

    public Directive(UserLine ul, byte[] _bytes) {
        super(ul, new ByteP[_bytes.length]);
        for (int i = 0; i < _bytes.length; i++) {
            this.bytes[i] = new ByteP(_bytes[i]);
        }
    }

    @Override
    public void finalCompilePass() { //possibly add error checking 

    }

};

class asmInstruction extends CompileTimeUserLine {

    public asmInstruction(UserLine ul) {
        super(ul, new ByteP[StringToOpcode.getInstructionSize(ul)]);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = new ByteP((byte) 0xcd);
        }
    }

    @Override
    public void finalCompilePass() throws CompilationException{
        byte[] temp = StringToOpcode.stringToOpcode(this);

        if (temp == null) {
            throw new CompilationException();
        }

        if (bytes.length != temp.length) {
            throw new CompilationException();
        }

        for (int i = 0; i < temp.length; i++) {
            bytes[i].b = temp[i];
        }
    }

};

class MemoryChunk {

    final int byteAlignment;
    final MemoryLable startLable;
    final ArrayList<CompileTimeUserLine> chunkData = new ArrayList<CompileTimeUserLine>();

    public MemoryChunk(MemoryLable lable) {
        this.startLable = lable;
        this.byteAlignment = 0;
    }

    public MemoryChunk(int byteAlignment, MemoryLable lable){
        this.byteAlignment = byteAlignment;
        this.startLable = lable;
    }

    public MemoryChunk(UserLine line) {
        this.startLable = null;

        int tempAll = ASMCompiler.parseInt(line.line.split(" ")[1]);

        if (tempAll < 0) {
            this.byteAlignment = 0;
            throw new DirectiveException("Alignment cannot be lass than zero", line);
        } else {
            this.byteAlignment = (int) Math.pow(2, tempAll);
        }
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
        memoryChunks = new ArrayList<MemoryChunk>();
    }

    public Origin(UserLine line) {
        this.byteOrigin = ASMCompiler.parseInt(line.line.split(" ")[1]);
        memoryChunks = new ArrayList<MemoryChunk>();
    }

    public void addMemoryChunk(MemoryChunk mc) {
        memoryChunks.add(mc);
    }
};

public class ASMCompiler {

    static private ArrayList<MemoryLable> memoryLables = new ArrayList<MemoryLable>();
    static private ArrayList<Origin> origins = new ArrayList<Origin>();
    static private ArrayList<ByteP> memoryByteList = new ArrayList<ByteP>();

    private static final CompilationLogger LOGGER = new CompilationLogger(ASMCompiler.class.getName());

    public static void compile(File file) {

        memoryLables = new ArrayList<MemoryLable>();
        memoryByteList = new ArrayList<ByteP>();
        origins = new ArrayList<Origin>();

        if (file == null) {
            LOGGER.log(Level.SEVERE, "File is null");
            return;
        }
        if (!file.exists() || file.isDirectory()) {
            LOGGER.log(Level.SEVERE, "File does not exist or is a directory: " + file.getAbsolutePath());
            return;
        }

        if (!FileUtils.getExtension(file).equals("asm")) {
            LOGGER.log(Level.WARNING, "This file is not an assembly file trying to compile it may result in errors");
        }

        //LogFrame.clearDisplay();
        LOGGER.log(Level.INFO, "Started Compilated of file: " + file.getAbsolutePath());

        ArrayList<UserLine> temp = getInstructions(file);

        temp = PreProcessor.preProcess(temp);

        findMemoryPointersAndChunkifyMemory(temp);

        addOriginsToByteList(); //adds instructions to bytelist in order according to file and origins and sets the respecting addresses of memory lables

        runFinalCompilePass(); //compiles code once memory lable locations are known

        byte[] memByteArray = createByteArrayFromByteList();

        Memory.setMemory(memByteArray);

        if (OptionsHandler.saveCompilationInfo.val()) {
            saveOriginsToFile(file.getAbsolutePath());
        }

        temp.clear();
        memoryByteList.clear();
        origins.clear();

        MainGUI.refreshAll();

        LOGGER.log(CompilationLevel.COMPILATION_MESSAGE, "Compilation of file: " + file.getAbsolutePath() + " has finished\n");
    }

    public static void saveOriginsToFile(String compiledFileName) {

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
                    if (ctul.ul.line.length() > maxSizeInstruction) {
                        maxSizeInstruction = ctul.ul.line.length();
                    }
                }

            }
        }
        maxSizeInstruction++;

        File file = new File(ResourceHandler.COMPILER_PATH + FileUtils.FILE_SEPARATOR + "CompilationInfo.txt");

        try (PrintWriter out = new PrintWriter(file)) {

            out.println("Compilation Info of File: " + compiledFileName);
            out.println();

            for (Origin org : origins) {

                if (org.byteOrigin == 0 && org.memoryChunks.isEmpty()) {//skips pre made origin if it is not in ise
                    continue;
                }

                out.println("Origin at: " + String.format("%08X", org.byteOrigin));
                out.println();

                for (MemoryChunk mc : org.memoryChunks) {

                    if (mc.startLable != null) {
                        if (mc.startLable.name.equals("") && mc.chunkData.isEmpty()) { //skips pre made lable if it is empty
                            continue;
                        }
                        if (!mc.startLable.name.isEmpty()) {
                            out.println("   " + "Memory Lable: "
                                    + String.format("%-" + maxSizeMemoryLable + "s", mc.startLable.name + ",")
                                    + " Memory Adress: " + String.format("%08X", mc.startLable.getByteAddress()));
                            out.println();
                        }
                    }
                    out.print("   " + "Aligned to: " + mc.byteAlignment);
                    out.println();

                    for (CompileTimeUserLine ctul : mc.chunkData) {

                        out.print("       " + "Instruction/Data: "
                                + String.format("%-" + maxSizeInstruction + "s", ctul.ul.line)
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

//            ASMCompiler.logCompilerMessage("Compilation Info File Wrote to: " + file.getAbsolutePath());
//            LOGGER.log(Level.CONFIG, "Compilation Info ");
            out.flush();
        } catch (Exception e) {
            //ASMCompiler.logCompilerError("Unable to write Pre Processed File to: " + file.getAbsolutePath() + "\n" + LogFrame.getFullExceptionMessage(e));
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

        String sig = "";
        if (memoryLable.contains(":")) {
            String temp[] = memoryLable.split(":");
            memoryLable = temp[0];
            sig = temp[1];
        }

        for (int i = 0; i < memoryLables.size(); i++) {
            if (memoryLables.get(i).name.equals(memoryLable)) {
                int val = memoryLables.get(i).getByteAddress();
                if (!sig.isEmpty()) {
                    return ASMCompiler.handleUserGeneratedSignificants(val, sig);
                } else {
                    return val;
                }
            }
        }
        LOGGER.log(CompilationLevel.COMPILATION_ERROR, "Memory Lable does not exist line: " + realLineNumberOfOpCode);
        //ASMCompiler.MemoryLableError("Memory Lable does not exist", realLineNumberOfOpCode);
        return -1;
    }

    private static void runFinalCompilePass() { //for every CompileTimeUserLine (instruction) run the final compilation pass 
        for (Origin org : origins) {
            for (MemoryChunk mc : org.memoryChunks) {
                for (CompileTimeUserLine ctul : mc.chunkData) {
                    try {
                        ctul.finalCompilePass();
                    }catch(CompilationException e){
                        LOGGER.log(CompilationLevel.COMPILATION_ERROR, e);
                    }
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
                if (mc.byteAlignment != 0) {
                    currentByteIndex = (currentByteIndex & ~(mc.byteAlignment - 1)) + mc.byteAlignment;
                }

                if (mc.startLable != null) {
                    mc.startLable.setByteAddress(currentByteIndex); //sets the byte address of the chunks memory lable
                }

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

        int startingIndex = 0; //if theres an origin at the begining skip first line

        Origin org;
        try {
            if (file.get(0).line.startsWith(".org")) {
                org = new Origin(file.get(0));
                startingIndex = 1;
            } else {
                org = new Origin(0);
            }
        } catch (Exception e) {
            org = new Origin(0);
        }

        int memoryLableIndex = 0;
        MemoryLable ml;
        MemoryChunk mc = new MemoryChunk(new MemoryLable(new UserLine("", -1), -1));

        for (int i = startingIndex; i < file.size(); i++) {

            UserLine currentLine = file.get(i);

            if (currentLine.line.endsWith(":")) { //if theres a new memoryLable add last memory chunk to memoryChunks and create a new current memoryChunk and add the new memory lable to memoryLables
                org.addMemoryChunk(mc);
                ml = new MemoryLable(currentLine, memoryLableIndex);
                mc = new MemoryChunk(ml);
                addMemoryLable(ml);
                memoryLableIndex++;
            } else if (currentLine.line.startsWith(".align")) {
                try {
                    MemoryChunk temp = new MemoryChunk(currentLine);
                    org.addMemoryChunk(mc);
                    mc = temp;
                }catch(Exception e){
                }
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
                LOGGER.log(CompilationLevel.COMPILATION_ERROR, null, new MemoryLableException("Cannot have duplicate memory lables", ml.line));
                //ASMCompiler.MemoryLableError("Cannot have Duplicate Memory Lables", ml.line.realLineNumber);
                return;
            }
        }
        memoryLables.add(ml);
    }

    private static CompileTimeUserLine userLineToCompileTimeUserLine(UserLine line) {

        if (line.line.startsWith(".")) {
            try {
                return new Directive(line, DirectivesDecoder.getDirectivesData(line)); //
            }catch(Exception e){
                //LOGGER.log()
                return new Directive(line, new byte[0]);
            }
        } else {
            return new asmInstruction(line);
        }
    }

    private static ArrayList<UserLine> getInstructions(File file) {
        int lineNumber = 0;

        ArrayList<UserLine> loadedFile = new ArrayList<UserLine>();
        List<String> temp = FileUtils.loadFileAsStringList(file);

        for (String line : temp) {
            loadedFile.add(new UserLine(line, lineNumber + 1));
            lineNumber++;
        }
        return loadedFile;
    }

    public static int parseInt(String string) { //to add functionality later

        int temp;

        if (string.startsWith("0b")) {
            temp = (int) Long.parseLong(string.split("b")[1], 2);
        } else if (string.startsWith("0x")) {
            temp = (int) Long.parseLong(string.split("x")[1], 16);
        } else if (string.contains("x")) {
            temp = (int) Long.parseLong(string.split("x")[1], Integer.parseInt(string.split("x")[0]));
        } else {
            temp = (int) Long.parseLong(string.trim());
        }
        if (string.contains(":")) {
            temp = handleUserGeneratedSignificants(temp, string.split(":")[1]);
        }
        return temp;

    }

    public static int handleUserGeneratedSignificants(int val, String ugSig) {
        ugSig = ugSig.trim();
        if (ugSig.equals("LH")) {
            val = val & 0xFFFF;
        } else if (ugSig.equals("HH")) {
            val = (val >> 16) & 0xFFFF;
        } else if (ugSig.equals("LB")) {
            val = val & 0xFF;
        }
        return val;
    }

    public static ArrayList<MemoryLable> getMemoryLables() {
        if (memoryLables == null) {
            return new ArrayList<MemoryLable>();
        } else {
            try {
                return (ArrayList<MemoryLable>) memoryLables.clone();
            } catch (Error e) {
                return new ArrayList<MemoryLable>();
            }
        }
    }

    public static void compileDefault() {
        compile(EditorHandler.getFalseFileFromLastFocused());
    }
}
