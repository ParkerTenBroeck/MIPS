/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Compiler.PreProcessorStatements;

import org.parker.mips.Compiler.DataClasses.UserLine;
import org.parker.mips.Compiler.PreProcessor;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class UNDEF extends Statement {

    public UNDEF() {

    }

    public UNDEF(UserLine line, ArrayList<Statement> statements) {
        super(line);
        line.line = line.line.replaceFirst("#undef ", "");
        line.line = line.line.trim();
        String identifire = line.line.trim();
        if (identifire.contains(" ")) {
            PreProcessor.logPreProcessorError("Cannot Have Spaces In Name", line.realLineNumber);
        } else {
            for (int i = 0; i < statements.size(); i++) {
                if ((statements.get(i)).IDENTIFIRE.equals(identifire)) {
                    statements.remove(i);
                    return;
                }
            }
            PreProcessor.logPreProcessorWarning(this.IDENTIFIRE + " does not exist", line.realLineNumber);
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
    public boolean canModifyDefindedStatements() {
        return false;
    }

    @Override
    protected String createIdentifire(UserLine ul) {
        return ul.line.replaceFirst("#define ", "").split(" ")[0].trim();
    }

    @Override
    public boolean doesStatementBelongToMe(UserLine ul) {
        return ul.line.startsWith("#undef ") || ul.line.equals("#undef");
    }

    @Override
    public void modifyDefinedStatements(ArrayList<Statement> statements) {
        for (int i = statements.size(); i >= 0; i--) {
            if (statements.get(i).IDENTIFIRE.equals(this.IDENTIFIRE)) {
                if ("define".equals(statements.get(i).STATEMENT_NAME)) {
                    statements.remove(i);
                }
            }
        }
    }

    @Override
    public boolean canGenerateAddedData() {
        return false;
    }

    @Override
    public Statement generateStatement(ArrayList<UserLine> data, int currentIndex, ArrayList<Statement> statements) {
        return new UNDEF(data.get(currentIndex), statements);
    }

    @Override
    protected void generateAddedData(ArrayList<UserLine> data, int currentIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<UserLine> getGeneratedAddedData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void calculateSizeOfStatement(ArrayList<UserLine> data, int currentIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSizeOfStatement() {
        return 1;
    }

}
