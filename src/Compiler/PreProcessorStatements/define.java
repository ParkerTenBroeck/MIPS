/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.PreProcessorStatements;

import Compiler.ASMCompiler;
import Compiler.DataClasses.AbstractArgumentList;
import Compiler.DataClasses.UserLine;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class define extends Statement {
    
    public final String arg;
    
    public define(UserLine input) {
        super(input);
        input.line = input.line.replaceFirst("#define ", "");
        input.line = input.line.trim();
        String[] nameAndValue = input.line.split(" ");
        if (nameAndValue.length != 1 && nameAndValue.length != 2) {
            ASMCompiler.PreProcessorError("Cannot add Define too many/little arguments or Name has Space", input.realLineNumber);
        }
        if (nameAndValue.length > 1) {
            arg = nameAndValue[1].trim();
        } else {
            arg = "";
        }
    }
    
    @Override
    public ArrayList<UserLine> parseString(UserLine input) {
        
        if (!input.line.contains(IDENTIFIRE)) {
            ArrayList<UserLine> lines = new ArrayList();
            lines.add(input);
            return lines;
        }
        
        AbstractArgumentList aal = new AbstractArgumentList(input.line);
        
        aal.replaceAllFull(IDENTIFIRE, arg);
        input.line = aal.buildString();
        
        ArrayList<UserLine> lines = new ArrayList();
        lines.add(input);
        
        return lines;
    }
    
    @Override
    public boolean canEditUserLine() {
        return true;
    }
    
    @Override
    public boolean canEditStatement() {
        return false;
    }

    @Override
    protected String createIdentifire(UserLine ul) {
       return ul.line.replaceFirst("#define ", "").split(" ")[0].trim();
    }
};
