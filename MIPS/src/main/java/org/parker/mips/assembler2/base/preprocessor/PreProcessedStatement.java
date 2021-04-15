package org.parker.mips.assembler2.base.preprocessor;

import org.parker.mips.assembler2.util.Line;

import java.io.Serializable;

public interface PreProcessedStatement extends Serializable {

    Line getLine();
}
