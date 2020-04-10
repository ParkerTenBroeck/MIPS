/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.PreProcessorStatements;

import Compiler.DataClasses.UserLine;
import static Compiler.PreProcessor.loadFile;
import java.util.ArrayList;
import mips.FileWriteReader;

/**
 *
 * @author parke
 */
public class include extends Statement {

    public include(UserLine line, ArrayList<UserLine> file, int index) {
        super(line);

        String path = IDENTIFIRE;

        if (path.contains(":")) {

        } else {
            path = FileWriteReader.getASMFilePath().substring(0, FileWriteReader.getASMFilePath().lastIndexOf("\\") + 1) + path;
        }

        file.addAll(index, loadFile(path, line.realLineNumber));
    }

    @Override
    public ArrayList<UserLine> parseString(UserLine input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canEditUserLine() {
        return false;
    }

    @Override
    public boolean canEditStatement() {
        return false;
    }

    @Override
    protected String createIdentifire(UserLine ul) {
        String temp;
        try {
            temp = ul.line.replace("#include ", "").trim();
        } catch (Exception e) {
            temp = "";
        }
        return temp;
    }

}
