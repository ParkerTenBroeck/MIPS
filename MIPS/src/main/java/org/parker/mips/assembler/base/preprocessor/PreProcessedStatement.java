package org.parker.mips.assembler.base.preprocessor;

import org.parker.mips.assembler.util.Line;

import java.io.Serializable;

public interface PreProcessedStatement extends Serializable {

    Line getLine();
}
