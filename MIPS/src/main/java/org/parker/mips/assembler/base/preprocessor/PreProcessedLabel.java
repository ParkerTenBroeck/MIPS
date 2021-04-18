package org.parker.mips.assembler.base.preprocessor;

import org.parker.mips.assembler.util.Line;

public class PreProcessedLabel implements PreProcessedStatement{

    public final Line parentLine;
    public final String label;

    public PreProcessedLabel(Line parentLine, String label){
        this.parentLine = parentLine;
        this.label = label;
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
