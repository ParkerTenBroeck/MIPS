/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilerv2;

import static Compiler.DotCodeDecoder.isDotData;
import static Compiler.StringToOpcode.stringToOpcode;
import static Compilerv2.DotCodeDecoder.getDotData;
import GUI.Main_GUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import mips.FileWriteReader;
import mips.processor.Memory;

/**
 *
 * @author parke
 */
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

public class ASMCompiler {

    public static void compile() {

        FileWriteReader.saveASMFile();
        ArrayList<UserLine> temp = getInstructions();

        temp = PreProcessor.preProcess(temp, true);
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
    
    public static void PreProcessorError(String message, int line){
        
        logCompilerError("[PreProcessor Error]: on line " + line + " " + message);
    }
    public static void DotCodeDecoderError(String message, int line){
        
        logCompilerError("[DotCode Error]: on line " + line + " " + message);
    }
    public static void OpCodeError(String message, int line){
        
        logCompilerError("[OpCode Error]: on line " + line + " " + message);
    }
    public static void MemoryLableError(String message, int line){
        
        logCompilerError("[MemoryLable Error]: on line " + line + " " + message);
    }
    private static void logCompilerError(String message){
        System.err.println("[Compiler]" + message);
    }
}
