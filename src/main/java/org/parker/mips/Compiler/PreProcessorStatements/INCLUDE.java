/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Compiler.PreProcessorStatements;

import org.parker.mips.Compiler.DataClasses.UserLine;
import org.parker.mips.Compiler.PreProcessor;
import static org.parker.mips.Compiler.PreProcessor.loadFile;
import java.util.ArrayList;
import org.parker.mips.FileHandler;
import org.parker.mips.ResourceHandler;

/**
 *
 * @author parke
 */
public class INCLUDE extends Statement {

    ArrayList<UserLine> generatedDataToAdd;

    public INCLUDE() {

    }

    public INCLUDE(UserLine line) {
        super(line);

        String path = IDENTIFIRE;

        if (path.contains(":")) {

        } else {
            path = FileHandler.getASMFilePath().substring(0, FileHandler.getASMFilePath().lastIndexOf(ResourceHandler.FILE_SEPERATOR) + 1) + path;
        }

        try {
            //System.out.println(path);
            String fileExtention = path.split("\\.")[1];
            if (fileExtention.equals("mxn") || fileExtention.equals("bin")) {
                generatedDataToAdd = new ArrayList<UserLine>();
                byte[] temp = FileHandler.loadFileAsByteArray(path);
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
            System.out.println(e);
            PreProcessor.logPreProcessorError("Failed to load included File", line.realLineNumber);
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
