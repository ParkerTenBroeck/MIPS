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
public abstract class Statement {

    public final String STATEMENT_NAME = this.getClass().getSimpleName().toLowerCase();
    public final String IDENTIFIRE;

    public Statement(UserLine ul) {
        this.IDENTIFIRE = createIdentifire(ul);
    }
    
    protected abstract String createIdentifire(UserLine ul);

    public abstract boolean canEditUserLine();

    public abstract boolean canEditStatement();

    public abstract ArrayList<UserLine> parseString(UserLine input);

}
