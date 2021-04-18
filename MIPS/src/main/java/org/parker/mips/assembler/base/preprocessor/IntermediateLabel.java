package org.parker.mips.assembler.base.preprocessor;

import org.parker.mips.assembler.util.Line;

import java.util.regex.Matcher;

public class IntermediateLabel implements IntermediateStatement{

    public final Line parentLine;
    public final String label;
    public final int labelStartingIndex;
    public final int labelEndingIndex;

    @Deprecated
    public IntermediateLabel(Line parentLine, String label){
        this.parentLine = parentLine;
        this.label = label;

        labelStartingIndex = -1;
        labelEndingIndex = -1;

    }

    public IntermediateLabel(Line parentLine, Matcher matcher, int identifierGroup){
        this.parentLine = parentLine;
        this.label = matcher.group(identifierGroup);

        labelStartingIndex = matcher.start(identifierGroup);
        labelEndingIndex = matcher.end(identifierGroup);
    }

    @Override
    public String toString() {
        return label + ":";
    }

    @Override
    public final Line getLine() {
        return parentLine;
    }
}
