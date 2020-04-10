/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.PreProcessorStatements;

import Compiler.ASMCompiler;
import Compiler.DataClasses.UserLine;
import static Compiler.PreProcessor.statements;
import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class undef extends Statement {

    public undef(UserLine line) {
        super(line);
        line.line = line.line.replaceFirst("#undef ", "");
        line.line = line.line.trim();
        String identifire = line.line.trim();
        if (identifire.contains(" ")) {
            ASMCompiler.PreProcessorError("Cannot Have Spaces In Name", line.realLineNumber);
        } else {
            for (int i = 0; i < statements.size(); i++) {
                if (statements.get(i).STATEMENT_NAME.equals("define")) {
                    if ((statements.get(i)).IDENTIFIRE.equals(identifire)) {
                        statements.remove(i);
                        return;
                    }
                }
            }
            ASMCompiler.PreProcessorError("Cannot Remove Define doesnot exist", line.realLineNumber);
        }
    }

    @Override
    public ArrayList<UserLine> parseString(UserLine input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canEditUserLine() {
        return false;
    }

    @Override
    public boolean canEditStatement() {
        return false;
    }

    @Override
    protected String createIdentifire(UserLine ul) {
        return ul.line.replaceFirst("#define ", "").split(" ")[0].trim();
    }

}
