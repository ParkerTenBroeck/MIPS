package org.parker.mips.assembler2.directives;

import org.parker.mips.assembler2.Assembler;
import org.parker.mips.assembler2.util.ExpressionParser;

public interface Directive {

    void parse(String operandExpression, ExpressionParser ep, Assembler assembler);

}
