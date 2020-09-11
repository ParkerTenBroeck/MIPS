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

    private int sizeOfStatement;
    private ArrayList<UserLine> inlineUserLines = new ArrayList();
    private String[] args;
    private ArrayList<String> internalMemoryLables = new ArrayList();

    private int timesUsed = 0;

    public DEFINLINE() {

    }

    private DEFINLINE(UserLine input, ArrayList<UserLine> file, int index) {
        super(input);
        calculateSizeOfStatement(file, index);

        AbstractArgumentList aal = new AbstractArgumentList(input.line.replace("#definline " + this.IDENTIFIRE, "").trim(),
                new char[]{','});

        args = Arrays.copyOf(aal.args, aal.args.length);
        for (int i = 0; i < args.length; i++) {
            if (args[i].trim().contains(" ")) {
                PreProcessor.logPreProcessorError("Argument Place holders cannot contain spaces", input.realLineNumber);
            }
        }

        for (int i = index + 1; i < file.size(); i++) {

            if (file.get(i).line.endsWith(":") || file.get(i).line.endsWith(": ")) {
                String temp = file.get(i).line.replaceAll(":", "").trim();
                if (temp.contains(" ")) {
                    //error
                }
                internalMemoryLables.add(temp);
            }

            if (file.get(i).line.startsWith("#")) {
                if (file.get(i).line.startsWith("#endinline")) {
                    break;
                } else {
                    //PreProcessor.logPreProcessorError("Cannot have Statement inside Statement", file.get(i).realLineNumber);
                }
            } else {
                inlineUserLines.add(file.get(i));
            }
            if (i == file.size() - 1) {
                PreProcessor.logPreProcessorError("Reached end of file #definline never terminated use #endinline", file.get(i).realLineNumber);
            }
        }
    }

    @Override
    public ArrayList<UserLine> parseNonStatement(UserLine input) {

        ArrayList<UserLine> parsedInlineLines = new ArrayList();

        if (input.line.startsWith(this.IDENTIFIRE + " ") || input.line.equals(this.IDENTIFIRE)) {

            AbstractArgumentList inputAal = new AbstractArgumentList(input.line.replace(IDENTIFIRE, "").trim(), new char[]{','});

            for (int i = 0; i < args.length; i++) {
                if (args[i].trim().contains(" ")) {
                    PreProcessor.logPreProcessorError("Arguments cannot contain spaces", input.realLineNumber);
                }
            }

            if (inputAal.args.length != this.args.length) {
                PreProcessor.logPreProcessorError("number of arguments do not match", input.realLineNumber);
            }

            for (int i = 0; i < inlineUserLines.size(); i++) {
                if (inlineUserLines.get(i).line.endsWith(":") || inlineUserLines.get(i).line.endsWith(": ")) {
                    String temp = inlineUserLines.get(i).line;
                    temp = temp.replaceAll(":", "").trim();
                    temp = temp + this.IDENTIFIRE + this.timesUsed;
                    temp = temp + ":";
                    temp = temp.trim();
                    parsedInlineLines.add(new UserLine(temp, inlineUserLines.get(i).realLineNumber));
                    continue;
                }
                AbstractArgumentList currentInlineAal = new AbstractArgumentList(inlineUserLines.get(i).line, new char[]{',', ' '});
                for (int j = 0; j < currentInlineAal.args.length; j++) {

                    for (int s = 0; s < internalMemoryLables.size(); s++) { //for custom memory lables
                        if (currentInlineAal.args[j].equals(internalMemoryLables.get(s))) {
                            currentInlineAal.args[j] = internalMemoryLables.get(s) + this.IDENTIFIRE + this.timesUsed;
                        }
                    }

                    for (int s = 0; s < args.length; s++) {                 //for regular expressions
                        if (currentInlineAal.args[j].equals(this.args[s])) {
                            currentInlineAal.args[j] = inputAal.args[s];
                        }
                        if (currentInlineAal.args[j].contains(":")) { //this is if a value has a byte vlaue thing attached to it :HH, :LH, :LB
                            String idekWhatThisIsCalled = currentInlineAal.args[j].split(":")[1];
                            if (currentInlineAal.args[j].split(":")[0].equals(this.args[s])) {
                                currentInlineAal.args[j] = inputAal.args[s] + ":" + idekWhatThisIsCalled;
                            }
                        }
                    }
                }
                parsedInlineLines.add(new UserLine(currentInlineAal.buildString(), inlineUserLines.get(i).realLineNumber));
            }
            timesUsed++;
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
        return ul.line.replace("#definline ", "").split(" ")[0];
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
