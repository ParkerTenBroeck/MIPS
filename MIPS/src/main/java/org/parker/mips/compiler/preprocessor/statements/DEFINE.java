/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.compiler.preprocessor.statements;

import org.parker.mips.compiler.data.AbstractArgumentList;
import org.parker.mips.compiler.data.UserLine;
import org.parker.mips.compiler.PreProcessor;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class DEFINE extends Statement {

    public final String arg;

    public DEFINE() {
        arg = null;
    }

    private DEFINE(UserLine input, ArrayList<Statement> statements) {
        super(input);
        input.line = input.line.replaceFirst("#define ", "");
        input.line = input.line.trim();
        String[] nameAndValue = input.line.split(" ");
        if (nameAndValue.length != 1 && nameAndValue.length != 2) {
            PreProcessor.logPreProcessorError("Cannot add Define too many/little arguments or Name has Space", input.realLineNumber);
        }
        if (nameAndValue.length > 1) {
            arg = nameAndValue[1].trim();
        } else {
            arg = "";
        }
    }

    @Override
    public ArrayList<UserLine> parseNonStatement(UserLine input) {

        if (!input.line.contains(IDENTIFIRE)) {
            ArrayList<UserLine> lines = new ArrayList<UserLine>();
            lines.add(input);
            return lines;
        }

        AbstractArgumentList aal = new AbstractArgumentList(input.line);

        aal.replaceAllFull(IDENTIFIRE, arg);
        input.line = aal.buildString();

        ArrayList<UserLine> lines = new ArrayList<UserLine>();
        lines.add(input);

        return lines;
    }

    @Override
    public boolean canModifyNonStatements() {
        return true;
    }

    @Override
    protected String createIdentifire(UserLine ul) {
        return ul.line.replaceFirst("#define ", "").split(" ")[0].trim();
    }

    @Override
    public boolean doesStatementBelongToMe(UserLine ul) {
        return ul.line.startsWith("#define");
    }

    @Override
    public boolean canModifyDefindedStatements() {
        return true;
    }

    @Override
    public boolean canGenerateAddedData() {
        return false;
    }

    @Override
    public int getSizeOfStatement() {
        return 1;
    }

    @Override
    protected void calculateSizeOfStatement(ArrayList<UserLine> data, int currentIndex) {

    }

    @Override
    public Statement generateStatement(ArrayList<UserLine> data, int currentIndex, ArrayList<Statement> statements) {
        return new DEFINE(data.get(currentIndex), statements);
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
    protected void modifyDefinedStatements(ArrayList<Statement> statements) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
};
