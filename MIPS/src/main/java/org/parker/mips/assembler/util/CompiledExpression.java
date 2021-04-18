package org.parker.mips.assembler.util;

import java.io.Serializable;

public abstract class CompiledExpression implements Serializable {
    public final int startingAddress;
    public final int endingAddress;
    public final Line line;

    public CompiledExpression(Line line, int s, int e){
        this.line = line;
        this.startingAddress = s;
        this.endingAddress = e;
    }

    public abstract Object evaluate();
}
