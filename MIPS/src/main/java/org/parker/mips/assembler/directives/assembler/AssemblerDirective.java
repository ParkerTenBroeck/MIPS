package org.parker.mips.assembler.directives.assembler;

import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.util.CompiledExpression;
import org.parker.mips.assembler.util.Line;

public interface AssemblerDirective {

    void parse(Line line, CompiledExpression[] args, BaseAssembler assembler);

}
