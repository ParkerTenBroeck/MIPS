package org.parker.mips.assembler.base.preprocessor;

import org.parker.mips.assembler.util.Line;

import java.util.regex.Matcher;

public class IntermediateAssemblyStatement implements IntermediateStatement{

    public final Line parentLine;
    public final String identifier;
    public final int identifierStartingIndex;
    public final int identifierEndingIndex;
    public final String expressionString;
    public final int expressionStartingIndex;
    public final int expressionEndingIndex;


    @Deprecated
    public IntermediateAssemblyStatement(Line parentLine, String identifier, String expressionString){
        this.parentLine = parentLine;
        this.identifier = identifier;
        this.expressionString = expressionString;

        identifierStartingIndex = -1;
        identifierEndingIndex = -1;
        expressionStartingIndex = -1;
        expressionEndingIndex = -1;
    }


    public IntermediateAssemblyStatement(Line parentLine, Matcher matcher, int identifierGroup, int expressionGroup){
        this.parentLine = parentLine;
        this.identifier = matcher.group(identifierGroup);
        this.expressionString = matcher.group(expressionGroup);

        identifierStartingIndex = matcher.start(identifierGroup);
        identifierEndingIndex = matcher.end(identifierGroup);
        expressionStartingIndex =  matcher.start(expressionGroup);
        expressionEndingIndex =  matcher.end(expressionGroup);
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
