/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.PreProcessorStatements;

import Compiler.ASMCompiler;
import Compiler.DataClasses.AbstractArgumentList;
import Compiler.DataClasses.UserLine;
import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author parke
 */
public class definline extends Statement {

    ArrayList<UserLine> inlineUserLines = new ArrayList();
    String[] args;

    public definline(UserLine input, ArrayList<UserLine> file, int index) {
        super(input);
        
        AbstractArgumentList aal = new AbstractArgumentList(input.line.replace("#definline ", ""), 
                new char[]{',', '(', ')'});
        
        args = Arrays.copyOf(aal.args, aal.args.length);

        for (int i = index; i < file.size(); i++) {

            if (file.get(i).line.startsWith("#")) {
                if (file.get(i).line.startsWith("#endinline")) {
                    break;
                } else {
                    ASMCompiler.PreProcessorError("Cannot have Statement inside Statement", file.get(i).realLineNumber);
                }
            } else {
                inlineUserLines.add(file.get(i));
            }
        }
    }

    @Override
    public ArrayList<UserLine> parseString(UserLine input) {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //StringUtils.substringBetween(input.line, "(", ")");
        
//        AbstractArgumentList aal = new AbstractArgumentList(input.line, new char[]{' ', ',', '(', ')'});
//
//        if (!aal.args[0].equals(this.IDENTIFIRE)) { //
//            ArrayList<UserLine> temp = new ArrayList();
//            temp.add(input);
//            return temp;
//        }
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
