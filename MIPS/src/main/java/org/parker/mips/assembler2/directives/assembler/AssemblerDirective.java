package org.parker.mips.assembler2.directives.assembler;

import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.util.CompiledExpression;

public interface AssemblerDirective {

    void parse(CompiledExpression[] args, BaseAssembler assembler);

}
