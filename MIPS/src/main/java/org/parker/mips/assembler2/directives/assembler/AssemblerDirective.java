package org.parker.mips.assembler2.directives.assembler;

import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.util.CompiledExpression;
import org.parker.mips.assembler2.util.Line;

public interface AssemblerDirective {

    void parse(Line line, CompiledExpression[] args, BaseAssembler assembler);

}
