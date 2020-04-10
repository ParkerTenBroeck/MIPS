/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

import Compiler.DataClasses.UserLine;
import Compiler.PreProcessorStatements.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import mips.FileWriteReader;

/**
 *
 * @author parke
 */
public class PreProcessor {

    public static ArrayList<Statement> statements;

//    private static ArrayList<UserLine> importFile(UserLine line) {
//        line.line = line.line.replaceFirst("#include ", "");
//        line.line = line.line.trim();
//        return loadFile(line.line, line.realLineNumber);
//    }
    public static ArrayList<UserLine> loadFile(String filePath, int realLineOfFilePath) {
        BufferedReader reader;

        filePath = filePath.replaceAll("\"", "");

        File tempFile = new File(filePath);

        ArrayList<UserLine> file = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(tempFile));
            String line = reader.readLine();
            while (line != null) {
                file.add(new UserLine(line, realLineOfFilePath));
                line = reader.readLine();
            }
            ASMCompiler.logCompilerMessage("[Pre Processor] Loaded File: " + tempFile.getAbsolutePath());
        } catch (Exception e) {
            ASMCompiler.PreProcessorError("Cannot read file Specified" + " " + e.getMessage(), realLineOfFilePath);
        }
        return file;
    }

    private static void handleStatement(ArrayList<UserLine> file, int index) {

        UserLine currentLine = file.get(index);
        String statementName = currentLine.line.replace("#", "");
        file.remove(index);

        if (statementName.startsWith("include")) {

            statements.add(new include(currentLine, file, index));

        } else if (statementName.startsWith("define")) {
            statements.add(new define(currentLine));

        } else if (statementName.startsWith("undef")) {
            new undef(currentLine);

        } else if (statementName.startsWith("inline")) {

            statements.add(new inline(currentLine, file, index));

        } else {
            ASMCompiler.PreProcessorError("Not a valid Statement", currentLine.realLineNumber);
        }

    }

    private static ArrayList<UserLine> preProcessLine(UserLine line) {

        ArrayList<UserLine> lines = new ArrayList();
        lines.add(line);

        for (Statement stat : statements) {

            for (int i = 0; i < lines.size(); i++) {

                UserLine currentLine = lines.get(i);

                if (stat.canEditUserLine() && !line.line.startsWith("#")) {
                    currentLine = stat.parseString(currentLine).get(0);
                } else if (stat.canEditStatement() && line.line.startsWith("#")) {
                    currentLine = stat.parseString(currentLine).get(0);
                }
                lines.set(i, currentLine);
            }
        }
        return lines;
    }

    private static String cleanLine(String line) {

        line = line.trim(); //gets the full user line

        if (line.equals("")) {
            return "";
        }

        try {
            if (line.contains(";")) {
                line = line.split(";")[0];//strips line of any comments
            }
        } catch (Exception e) {
            return "";
        }
        if (line.equals("")) {
            return "";
        }

        //normalfying instructions
        String start;
        String[] args;

        try {
            start = line.split(" ")[0].trim();
        } catch (Exception e) {
            start = line;
        }
        try {
            args = line.replace(start, "").split(",");
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }
        } catch (Exception e) {
            args = new String[]{""};
        }

        line = start + " " + String.join(",", args);

        return line.trim();
    }

    public static ArrayList<UserLine> preProcess(ArrayList<UserLine> file, boolean outputFile) {

        //file = loadFile("C:\\Users\\parke\\OneDrive\\Documents\\GitHub\\MIPS\\Examples\\snake 2.asm", 1);
        statements = new ArrayList();

        if (file == null) {
            return new ArrayList();
        }

        for (int i = 0; i < file.size(); i++) {

            UserLine currentLine = file.get(i);

            try {
                currentLine.line = cleanLine(currentLine.line);
            } catch (Exception e) {
                ASMCompiler.PreProcessorError("Cannot Clean Line", currentLine.realLineNumber);
            }

            if (currentLine.line.equals("") || currentLine.line.equals("/n")) { //if line is empty delete it and move on
                file.remove(i);
                i--;
                continue;
            }

            file.remove(i);
            ArrayList<UserLine> temp = preProcessLine(currentLine);
            if (temp.size() == 0) {
                i--;
            } else {
                file.addAll(i, temp);
            }

            if (currentLine.line.startsWith("#")) {
                handleStatement(file, i);
                i--;
                continue;
            }

        }

        if (outputFile) {
            writePreProcessedFile(file);
        }
        statements.clear();
        return file;
    }

    private static void writePreProcessedFile(ArrayList<UserLine> fileInfo) {
        File tempFile = new File("preProcessedFile.asm");
        try (PrintWriter out = new PrintWriter(tempFile)) {

            for (int i = 0; i < fileInfo.size(); i++) {

                out.println(fileInfo.get(i).line);
            }
            ASMCompiler.logCompilerMessage("Pre Processed File Wrote to:" + tempFile.getAbsolutePath());
            out.flush();
        } catch (Exception e) {
            ASMCompiler.logCompilerError("Unable to write Pre Processed File to:" + tempFile.getAbsolutePath() + " " + e.getMessage());
        }

    }
}
