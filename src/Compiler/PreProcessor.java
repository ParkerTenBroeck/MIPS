/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import mips.FileWriteReader;

/**
 *
 * @author parke
 */
abstract class Define {

    enum Type {
        DATA, INLINE
    }

    String name;

    abstract Type getType();

    abstract ArrayList<UserLine> parseString(UserLine input);

    String getName() {
        return this.name;
    }
}

class DefineData extends Define {

    String value;

    @Override
    Type getType() {
        return Type.DATA;
    }

    @Override
    ArrayList<UserLine> parseString(UserLine input) {

        if (!input.line.contains(name)) {
            ArrayList<UserLine> lines = new ArrayList();
            lines.add(input);
            return lines;
        }

        String start;
        String[] args;

        try {
            start = input.line.split(" ")[0].trim();
        } catch (Exception e) {
            start = input.line;
        }
        try {
            args = input.line.replace(start, "").split(",");
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }
        } catch (Exception e) {
            args = new String[]{""};
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(this.name)) {
                args[i] = this.value;
            }
        }
        input.line = start + " " + String.join(",", args);

        ArrayList<UserLine> lines = new ArrayList();
        lines.add(input);

        return lines;
    }
};

class InlineData extends Define {

    @Override
    Type getType() {
        return Type.INLINE;
    }

    @Override
    ArrayList<UserLine> parseString(UserLine input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
};

public class PreProcessor {

    private static ArrayList<Define> defines;

    private static ArrayList<UserLine> importFile(UserLine line) {
        line.line = line.line.replaceFirst("#include ", "");
        line.line = line.line.trim();
        return loadFile(line.line, line.realLineNumber);
    }

    private static ArrayList<UserLine> loadFile(String filePath, int realLineOfFilePath) {
        BufferedReader reader;

        filePath = filePath.replaceAll("\"", "");
        
        File tempFile;
        if (filePath.contains(":")) {
            tempFile = new File(filePath);
        } else {
            tempFile = new File(FileWriteReader.getASMFilePath().substring(0, FileWriteReader.getASMFilePath().lastIndexOf("\\") + 1) + filePath);
        }

        ArrayList<UserLine> file = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(tempFile));
            String line = reader.readLine();
            while (line != null) {
                file.add(new UserLine(line, realLineOfFilePath));
                line = reader.readLine();
            }
            ASMCompiler.logCompilerMessage("[Pre Processor] Included File: " + tempFile.getAbsolutePath());
        } catch (Exception e) {
            ASMCompiler.PreProcessorError("Cannot read file Specified" + " " + e.getMessage(), realLineOfFilePath);
        }
        return file;
    }

    private static void addToDefineData(UserLine line) {
        line.line = line.line.replaceFirst("#define ", "");
        line.line = line.line.trim();
        String[] nameAndValue = line.line.split(" ");
        if (nameAndValue.length != 2) {
            ASMCompiler.PreProcessorError("Cannot add Define too many/little arguments or Name has Space", line.realLineNumber);
        } else {
            DefineData dd = new DefineData();
            dd.name = nameAndValue[0].trim();
            dd.value = nameAndValue[1].trim();
            defines.add(dd);
        }
    }

    private static void removeFromDefineData(UserLine line) {
        line.line = line.line.replaceFirst("#undef ", "");
        line.line = line.line.trim();
        String name = line.line.trim();
        if (name.contains(" ")) {
            ASMCompiler.PreProcessorError("Cannot Have Spaces In Name", line.realLineNumber);
        } else {
            for (int i = 0; i < defines.size(); i++) {
                if (defines.get(i).name.equals(name)) {
                    defines.remove(i);
                    return;
                }
            }
            ASMCompiler.PreProcessorError("Cannot Remove Define doesnot exist", line.realLineNumber);
        }
    }

    private static void sortDefineData() { //sorts from larges name to smalles name
        Comparator c = new Comparator<Define>() {
            public int compare(Define s1, Define s2) {
                return Integer.compare(s2.name.length(), s1.name.length());
            }
        };
        Collections.sort(defines, c);
    }

    private static ArrayList<UserLine> preProcessLine(UserLine line) {

        ArrayList<UserLine> lines = new ArrayList();

        for (Define def : defines) {
            if (def.getType() == Define.Type.DATA) {
                line = def.parseString(line).get(0);//DATA should only return a size of one 

            } else if (def.getType() == Define.Type.INLINE) {

            } else {
                ASMCompiler.PreProcessorError("Invalid # value", line.realLineNumber);
            }
        }
        lines.add(line);
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
        defines = new ArrayList();

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

            if (currentLine.line.startsWith("#include")) {
                file.remove(i);
                file.addAll(i, importFile(currentLine));
                i--;

            } else if (currentLine.line.startsWith("#define")) {
                file.remove(i);
                addToDefineData(currentLine);
                sortDefineData();
                i--;

            } else if (currentLine.line.startsWith("#undef")) {
                file.remove(i);
                removeFromDefineData(currentLine);
                sortDefineData();
                i--;

                // } else if (currentLine.line.startsWith("#defineinline")) { //maybe
            } else {
                file.remove(i);
                ArrayList<UserLine> temp = preProcessLine(currentLine);
                if (temp.size() == 0) {
                    i--;
                } else {
                    file.addAll(i, temp);
                }
            }
        }

        if (outputFile) {
            writePreProcessedFile(file);
        }
        defines.clear();
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
