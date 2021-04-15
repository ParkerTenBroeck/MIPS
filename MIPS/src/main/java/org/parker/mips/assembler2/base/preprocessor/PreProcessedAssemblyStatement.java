package org.parker.mips.assembler2.base.preprocessor;

import org.parker.mips.assembler2.util.CompiledExpression;
import org.parker.mips.assembler2.util.Line;

public class PreProcessedAssemblyStatement implements PreProcessedStatement{

    public final Line parentLine;
    public final String identifier;
    public final String expressionString;
    public final CompiledExpression[] args;


    public PreProcessedAssemblyStatement(Line parentLine, String identifier, String expressionString, CompiledExpression[] args){
        this.parentLine = parentLine;
        this.identifier = identifier;
        this.expressionString = expressionString;
        this.args = args;
    }

    @Override
    public String toString() {
        return identifier + " " + expressionString;
    }

    @Override
    public final Line getLine() {
        return parentLine;
    }
}
