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
public class IF extends Statement {

    ArrayList<UserLine> generatedData;
    
    public IF() {

    }
    
    public IF(ArrayList<UserLine> data, int currentIndex, ArrayList<Statement> statements){
        
    }
    
    @Override
    public Statement generateStatement(ArrayList<UserLine> data, int currentIndex, ArrayList<Statement> statements) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String createIdentifire(UserLine ul) {
        return null;
    }

    @Override
    public boolean doesStatementBelongToMe(UserLine ul) {
        boolean temp = false;
        temp |= ul.line.startsWith("#if ");
        temp |= ul.line.startsWith("#endif ");
        temp |= ul.line.startsWith("#else ");
        temp |= ul.line.startsWith("#elseif ");
        return temp;
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
    public boolean canGenerateAddedData() {
        return true;
    }   

    @Override
    public ArrayList<UserLine> parseNonStatement(UserLine input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void modifyDefinedStatements(ArrayList<Statement> statements) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
