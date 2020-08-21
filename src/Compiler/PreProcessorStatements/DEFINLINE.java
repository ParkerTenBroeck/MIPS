/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.PreProcessorStatements;

import Compiler.DataClasses.AbstractArgumentList;
import Compiler.DataClasses.UserLine;
import Compiler.PreProcessor;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author parke
 */
public class DEFINLINE extends Statement {

    int sizeOfStatement;
    ArrayList<UserLine> inlineUserLines = new ArrayList();
    String[] args;

    public DEFINLINE() {

    }

    private DEFINLINE(UserLine input, ArrayList<UserLine> file, int index) {
        super(input);
        calculateSizeOfStatement(file, index);

        AbstractArgumentList aal = new AbstractArgumentList(input.line.replace("#definline ", ""),
                new char[]{',', '(', ')'});

        args = Arrays.copyOf(aal.args, aal.args.length);

        for (int i = index + 1; i < file.size(); i++) {

            if (file.get(i).line.startsWith("#")) {
                if (file.get(i).line.startsWith("#endinline")) {
                    break;
                } else {
                    PreProcessor.logPreProcessorError("Cannot have Statement inside Statement", file.get(i).realLineNumber);
                }
            } else {
                inlineUserLines.add(file.get(i));
            }
        }
    }

    @Override
    public ArrayList<UserLine> parseNonStatement(UserLine input) {

        ArrayList<UserLine> parsedInlineLines = new ArrayList();

        if (input.line.startsWith(this.IDENTIFIRE + " ") || input.line.equals(this.IDENTIFIRE)) {

            AbstractArgumentList inputAal = new AbstractArgumentList(input.line.replace(IDENTIFIRE + " ", ""), new char[]{',', ' '});
            if(inputAal.args.length != this.args.length){
                //error
            }
            

            for (int i = 0; i < inlineUserLines.size(); i++) {
                AbstractArgumentList currentInlineAal = new AbstractArgumentList(inlineUserLines.get(i).line, new char[]{',', ' '});
                for (int j = 0; j < currentInlineAal.args.length; j++) {
                    for (int s = 0; s < args.length; s++) {
                        if (currentInlineAal.args[j].equals(this.args[s])) {
                            currentInlineAal.args[j] = inputAal.args[s];
                        }
                    }
                }
                parsedInlineLines.add(new UserLine(currentInlineAal.buildString(),inlineUserLines.get(i).realLineNumber));
            }

        } else {
            parsedInlineLines.add(input);
        }
        return parsedInlineLines;

    }
    @Override
    public boolean canModifyNonStatements() {
        return true;
    }

    @Override
    public boolean canModifyDefindedStatements() {
        return true;
    }

    @Override
    protected String createIdentifire(UserLine ul) {
        return ul.line.replace("#inline ", "").split(" ")[0];
    }

    @Override
    public boolean doesStatementBelongToMe(UserLine ul) {
        return ul.line.startsWith("#definline") || ul.line.startsWith("#endinline");
    }

    @Override
    public void modifyDefinedStatements(ArrayList<Statement> statements) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canGenerateAddedData() {
        return false;
    }

    @Override
    public Statement generateStatement(ArrayList<UserLine> data, int currentIndex, ArrayList<Statement> statements) {
        return new DEFINLINE(data.get(currentIndex), data, currentIndex);
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

        this.sizeOfStatement = 1;
        int recursiveIndex = 0; //basically makes sure that it deosnt stop on a endinline on an inline defined in itsself

        for (int i = currentIndex + 1; i < data.size(); i++) {
            this.sizeOfStatement++;
            UserLine currentLine = data.get(i);
            if (currentLine.line.startsWith("#definline")) {
                recursiveIndex++;
                continue;
            } else if (currentLine.line.startsWith("#endinline") && recursiveIndex > 0) {
                recursiveIndex--;
                continue;
            }
            if (currentLine.line.startsWith("#endinline") && recursiveIndex == 0) {
                return;
            }
            if (recursiveIndex > 0) {

            }
        }
    }

    @Override
    public int getSizeOfStatement() {
        return sizeOfStatement;
    }

};
