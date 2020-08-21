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

    public Statement(){ //only used if the statment is not needed for later (not added to list of defined statements)
        this.IDENTIFIRE = null;
    }
    
    public Statement(UserLine ul) {
        this.IDENTIFIRE = createIdentifire(ul);
    }
    public abstract Statement generateStatement(ArrayList<UserLine> data, int currentIndex, ArrayList<Statement> statements);
    
    protected abstract String createIdentifire(UserLine ul); //creates an identifier string from statement line this is set to null if the statement does not need to be remembered

    public abstract boolean doesStatementBelongToMe(UserLine ul); //checks weather the statement is used in this statment
     
    
    public abstract boolean canModifyNonStatements(); // can this statement modify non statements (like asm code or other text)
    
    public abstract ArrayList<UserLine> parseNonStatement(UserLine input); // parses a non statement line (if can modify non statements is true)

    
    public abstract boolean canModifyDefindedStatements(); // can this particular statment remove / modify other pre existing statements already defined 
    
    protected abstract void modifyDefinedStatements(ArrayList<Statement> statements); // modifies the already defined statements if allowed
    
    
    public abstract boolean canGenerateAddedData(); // tells you weather or not this statement generates data that needs to be added into the file, if set to zero the statement will be handled then removed from the file
    
    protected abstract void generateAddedData(ArrayList<UserLine> data, int currentIndex); //generates / finters the data that is going to be added into the file
    
    public abstract ArrayList<UserLine> getGeneratedAddedData(); //gets the generated data to add(may still need further pre processing)
    
    protected abstract void calculateSizeOfStatement(ArrayList<UserLine> data, int currentIndex); //how many real lines is this statement (needed for skipping over already processed data in file in multi line statements)
    
    public abstract int getSizeOfStatement(); // gets the real size of the statement

}
