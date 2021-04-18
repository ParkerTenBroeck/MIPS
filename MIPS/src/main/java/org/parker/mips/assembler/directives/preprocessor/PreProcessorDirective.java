package org.parker.mips.assembler.directives.preprocessor;

import org.parker.mips.assembler.base.preprocessor.BasePreProcessor;
import org.parker.mips.assembler.base.preprocessor.IntermediateDirective;
import org.parker.mips.assembler.base.preprocessor.IntermediateStatement;
import org.parker.mips.assembler.util.ExpressionCompiler;

import java.util.List;

public interface PreProcessorDirective {

    void parse(IntermediateDirective s, List<IntermediateStatement> is, int index, ExpressionCompiler ec, BasePreProcessor preProcessor);

}
