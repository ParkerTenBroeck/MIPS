/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.PreProcessorStatements;

import Compiler.DataClasses.UserLine;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class inline extends Statement {
    
    public inline(UserLine input, ArrayList<UserLine> file, int index) {       
        super(input);
    }

    @Override
    public ArrayList<UserLine> parseString(UserLine input) {
        return null;
    }

    @Override
    public boolean canEditUserLine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canEditStatement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String createIdentifire(UserLine ul) {
        return ul.line.replace("#inline ", "").split(" ")[0];
    }
};
