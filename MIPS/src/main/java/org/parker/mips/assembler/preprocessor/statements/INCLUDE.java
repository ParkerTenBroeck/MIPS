/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.assembler.preprocessor.statements;

import org.parker.mips.FileUtils;
import org.parker.mips.assembler.AssemblerLevel;
import org.parker.mips.assembler.AssemblerLogger;
import org.parker.mips.assembler.data.UserLine;
import org.parker.mips.gui.editor.EditorHandler;

import java.io.File;
import java.util.ArrayList;

import static org.parker.mips.assembler.PreProcessor.loadFile;

/**
 *
 * @author parke
 */
public class INCLUDE extends Statement {

    private static final AssemblerLogger LOGGER = new AssemblerLogger(INCLUDE.class.getName());

    ArrayList<UserLine> generatedDataToAdd;

    public INCLUDE() {

    }

    public INCLUDE(UserLine line) {
        super(line);

        String path = IDENTIFIRE;

        if (path.contains(":")) {

        } else {
            File file = EditorHandler.getFalseFileFromLastFocused();
            path = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(FileUtils.FILE_SEPARATOR) + 1) + path;
        }

        try {

                generatedDataToAdd = loadFile(path, line);
        } catch (Exception e) {
            LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR,"Failed to load included File", line, e);
        }

    }

    @Override
    public ArrayList<UserLine> parseNonStatement(UserLine input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canModifyNonStatements() {
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

    @Override
    public boolean doesStatementBelongToMe(UserLine ul) {
        return ul.line.startsWith("#include");
    }

    @Override
    public boolean canModifyDefindedStatements() {
        return false;
    }

    @Override
    public void modifyDefinedStatements(ArrayList<Statement> statements) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canGenerateAddedData() {
        return true;
    }

    @Override
    public Statement generateStatement(ArrayList<UserLine> data, int currentIndex, ArrayList<Statement> statements) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new INCLUDE(data.get(currentIndex));
    }

    @Override
    public ArrayList<UserLine> getGeneratedAddedData() {
        return generatedDataToAdd;
    }

    @Override
    protected void calculateSizeOfStatement(ArrayList<UserLine> data, int currentIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSizeOfStatement() {
        return 1;
    }

    @Override
    protected void generateAddedData(ArrayList<UserLine> data, int currentIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
