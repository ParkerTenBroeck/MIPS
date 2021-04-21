/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.assembler_old;

import org.parker.mips.assembler.util.AssemblerLevel;
import org.parker.mips.util.FileUtils;
import org.parker.mips.util.ResourceHandler;
import org.parker.mips.assembler_old.data.UserLine;
import org.parker.mips.assembler_old.preprocessor.statements.*;
import org.parker.mips.preferences.Preferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class PreProcessor {

    public static final Statement[] validStatements = {new DEFINE(), new DEFINLINE(), new IF(), new INCLUDE(), new UNDEF()};

    private static ArrayList<Statement> statements;

    private static final AssemblerLogger LOGGER = new AssemblerLogger(PreProcessor.class.getName());

    private static final Preferences ppPref = Preferences.ROOT_NODE.getNode("system/assembler");

    public static ArrayList<UserLine> loadFile(String filePath, UserLine us) {
        BufferedReader reader;

        filePath = filePath.replaceAll("\"", "");

        File tempFile = new File(filePath);

        ArrayList<UserLine> file = new ArrayList<UserLine>();
        try {
            reader = new BufferedReader(new FileReader(tempFile));
            String line = reader.readLine();
            while (line != null) {
                file.add(new UserLine(line, us.realLineNumber));
                line = reader.readLine();
            }
            LOGGER.log(AssemblerLevel.ASSEMBLER_MESSAGE, "Loaded File: " + tempFile.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR,"Cannot read file Specified", us, e);
        }
        return file;
    }

    private static Statement generateStatement(ArrayList<UserLine> loadedFile, int index) {

        UserLine currentLine = loadedFile.get(index);

        for (Statement validStatement : validStatements) {
            if (validStatement.doesStatementBelongToMe(currentLine)) {
                Statement temp = validStatement.generateStatement(loadedFile, index, statements);

                if (!temp.IDENTIFIRE.isEmpty()) {

                    for (int i = 0; i < statements.size(); i++) {
                        if (statements.get(i).IDENTIFIRE.equals(temp.IDENTIFIRE)) {
                            statements.remove(i);
                            LOGGER.log(AssemblerLevel.ASSEMBLER_WARNING,temp.IDENTIFIRE + " Has already been defined use undef to undefine the value before creating a new value overwritting existing value",  currentLine);
                        }
                    }

                    statements.add(temp);
                }

                return temp;
            }
        }
        LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR,"Not a valid Statement", currentLine);
        return null;
    }

    private static ArrayList<UserLine> preProcessLine(UserLine line) {

        ArrayList<UserLine> oldLines = new ArrayList<UserLine>();
        ArrayList<UserLine> newLines = new ArrayList<UserLine>();
        oldLines.add(line);

        for (int r = 0; r < 5; r++) {

            for (int i = 0; i < oldLines.size(); i++) {
                ArrayList<UserLine> temp = new ArrayList<UserLine>();
                temp.add(oldLines.get(i));

                for (Statement statement : statements) {
                    if (statement.canModifyNonStatements()) {

                        UserLine currentLine;
                        for (int j = 0; j < temp.size(); j++) {
                            currentLine = temp.get(j);
                            temp.remove(j);
                            //ArrayList<UserLine> secondTemp = statement.parseNonStatement(currentLine);
                            //temp.ensureCapacity(temp.size() + secondTemp.size());
                            temp.addAll(j, statement.parseNonStatement(currentLine));
                        }
                    }
                }
                //oldLines.remove(i);
                newLines.addAll(temp);
            }
            oldLines = newLines;
            newLines = new ArrayList<UserLine>();
        }

//        ArrayList<UserLine> lines = new ArrayList();
//        lines.add(line);
//
//        for (Statement stat : statements) {
//
//            for (int i = 0; i < lines.size(); i++) {
//
//                UserLine currentLine = lines.get(i);
//
//                if (stat.canModifyNonStatements() && !line.line.startsWith("#")) {
//                    currentLine = stat.parseNonStatement(currentLine).get(0);
//                } else if (stat.canModifyDefindedStatements() && line.line.startsWith("#")) {
//                    currentLine = stat.parseNonStatement(currentLine).get(0);
//                }
//                lines.set(i, currentLine);
//            }
//        }
        return oldLines;
    }

    private static String cleanLine(String line) {

        line = line.trim(); //gets the full user line

        if (line.equals("")) {
            return "";
        }

        try {
            if (line.contains(";") /*|| line.contains("#")*/) {
                line = line.split(";")[0];//strips line of any comments
                //line = line.split("#")[0];
            }
        } catch (Exception e) {
            return "";
        }
        if (line.equals("")) {
            return "";
        }
        line = line.replaceAll("\t", " "); //replaces indents with space

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

    public static ArrayList<UserLine> preProcess(ArrayList<UserLine> file) {

        if (file != null) {
            if ((Boolean)ppPref.getPreference("includeRegDef", true)) {
                file.add(0, new UserLine("#include \"" + ResourceHandler.REG_DEF_HEADER_FILE + "\"", -2));
                LOGGER.log(AssemblerLevel.ASSEMBLER_MESSAGE,"Included regdef.asm");
            }
            if ((Boolean)ppPref.getPreference("includeSysCallDef", true)) {
                file.add(0, new UserLine("#include \"" + ResourceHandler.SYS_CALL_DEF_HEADER_FILE + "\"", -3));
                LOGGER.log(AssemblerLevel.ASSEMBLER_MESSAGE,"Included syscalldef.asm");
            }
        }

        //file = loadFile("C:\\Users\\parke\\OneDrive\\Documents\\GitHub\\MIPS\\Examples\\snake 2.asm", 1);
        statements = new ArrayList<Statement>();

        ArrayList<UserLine> preProcessedFile = subPreProcess(file, true);

        statements.clear();
        return preProcessedFile;
    }

    private static ArrayList<UserLine> subPreProcess(ArrayList<UserLine> data, boolean firstLayer) { //pre processes data in included data or nested is statements

        ArrayList<UserLine> cleanedData = new ArrayList<UserLine>();
        ArrayList<UserLine> preProcessedData = new ArrayList<UserLine>();

        if (data == null) {
            return preProcessedData;
        }

        for (int i = 0; i < data.size(); i++) {

            UserLine currentLine = data.get(i);

            try {
                currentLine.line = cleanLine(currentLine.line);
            } catch (Exception e) {
                LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Cannot Clean Line: " + currentLine, e); //cleans the line (fixes spacing and removes comments)
            }

            if (currentLine.line.equals("") || currentLine.line.equals("/n")) { //if line is empty delete it and move on
                continue;
            }

            cleanedData.add(currentLine);

        }
        if ((Boolean)ppPref.getPreference("saveCleanedFile", true) && firstLayer) {
            writeCleanedFile(cleanedData);
        }

        for (int i = 0; i < cleanedData.size(); i++) {
            UserLine currentLine = cleanedData.get(i);

            if (currentLine.line.startsWith("#")) { //handles new preprossesor statemtns

                Statement statement = generateStatement(cleanedData, i);

                if (statement.canGenerateAddedData()) {
                    preProcessedData.addAll(subPreProcess(statement.getGeneratedAddedData(), false));
                }
                i += statement.getSizeOfStatement() - 1;
                continue;
            }

            preProcessedData.addAll(preProcessLine(currentLine)); //preprocesses line and adds resulting line / lines to pre processed file

        }
        if ((Boolean)ppPref.getPreference("savePreProcessedFile", true)) {
            writePreProcessedFile(preProcessedData);
        }

        return preProcessedData;
    }

    private static void writePreProcessedFile(ArrayList<UserLine> fileInfo) {
        File file = new File(ResourceHandler.COMPILER_PATH + FileUtils.FILE_SEPARATOR + "PreProcessedFile.asm");
        writeArrayListOfUserLinesToFile(fileInfo, file);
    }

    private static void writeCleanedFile(ArrayList<UserLine> fileInfo) {
        File file = new File(ResourceHandler.COMPILER_PATH + FileUtils.FILE_SEPARATOR + "CleanedFile.asm");
        writeArrayListOfUserLinesToFile(fileInfo, file);
    }

    private static void writeArrayListOfUserLinesToFile(ArrayList<UserLine> data, File file) {

        PrintWriter out = null;

        try {

            out = new PrintWriter(file);
            for (int i = 0; i < data.size(); i++) {

                out.println(data.get(i).line);
            }
            LOGGER.log(AssemblerLevel.ASSEMBLER_MESSAGE, file.getName() + " File Wrote to:" + file.getAbsolutePath());
            out.flush();
        } catch (Exception e) {
            LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Unable to write " + file.getName() + " File to:" + file.getAbsolutePath(), e);
        } finally {
            try {

            } catch (Exception e) {
                out.flush();

            }
            try {
                out.close();
            } catch (Exception e) {

            }
        }
    }
}
