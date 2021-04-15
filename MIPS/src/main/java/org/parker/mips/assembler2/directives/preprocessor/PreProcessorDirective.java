package org.parker.mips.assembler2.directives.preprocessor;

import org.parker.mips.assembler2.base.preprocessor.BasePreProcessor;
import org.parker.mips.assembler2.base.preprocessor.IntermediateDirective;
import org.parker.mips.assembler2.base.preprocessor.IntermediateStatement;
import org.parker.mips.assembler2.util.ExpressionCompiler;
import org.parker.mips.assembler2.util.Line;

import java.util.List;

public interface PreProcessorDirective {

    void parse(IntermediateDirective s, List<IntermediateStatement> is, int index, ExpressionCompiler ec, BasePreProcessor preProcessor);

}
