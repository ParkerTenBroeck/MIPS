/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.compiler.preprocessor.statements;

import java.io.File;
import org.parker.mips.compiler.data.UserLine;
import org.parker.mips.compiler.PreProcessor;
import static org.parker.mips.compiler.PreProcessor.loadFile;
import java.util.ArrayList;
import org.parker.mips.FileHandler;
import org.parker.mips.Log;

/**
 *
 * @author parke
 */
public class INCLUDE extends Statement {

    ArrayList<UserLine> generatedDataToAdd;

    public INCLUDE() {

    }

    public INCLUDE(UserLine line, File file) {
        super(line);

        String path = IDENTIFIRE;

        if (path.contains(":")) {

        } else {
            path = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(FileHandler.FILE_SEPERATOR) + 1) + path;
        }

        try {
            //System.out.println(path);
            String fileExtention = path.split("\\.")[1];
            if (fileExtention.equals("mxn") || fileExtention.equals("bin")) {
                generatedDataToAdd = new ArrayList<UserLine>();
                byte[] temp = FileHandler.loadFileAsByteArray(new File(path));
                String data = ".byte ";
                for (int i = 0; i < temp.length; i++) {
                    data = data + temp[i];
                    if (i != temp.length - 1) {
                        data = data + ",";
                    }
                }
                //System.out.println(data);
                generatedDataToAdd.add(new UserLine(data, line.realLineNumber));
            } else {
                generatedDataToAdd = loadFile(path, line.realLineNumber);
            }
        } catch (Exception e) {
            PreProcessor.logPreProcessorError("Failed to load included File:\n" + Log.getFullExceptionMessage(e), line.realLineNumber);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //return new INCLUDE(data.get(currentIndex));
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
