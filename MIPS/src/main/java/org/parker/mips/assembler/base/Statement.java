package org.parker.mips.assembler.base;

import org.parker.mips.assembler.exception.AssemblerError;
import org.parker.mips.assembler.exception.ParameterCountError;
import org.parker.mips.assembler.exception.ParameterTypeError;
import org.parker.mips.assembler.util.CompiledExpression;
import org.parker.mips.assembler.util.Line;

import java.util.Arrays;

/**
 * Any Line in assembly source that is not a comment
 */
@SuppressWarnings("unused")
public abstract class Statement<ArgType> {

    private CompiledExpression[] argExpressions = null;
    private ArgType[] args = null;
    private boolean[] evaluated = null;
    private Line line;

    public final int argsLength(){
        return argExpressions.length;
    }

    public final ArgType getArg(int index){
        if(!evaluated[index]) {
            try {
                args[index] = evaluateArgument(index, argExpressions[index].evaluate());
            }catch (Exception e){
                throw new AssemblerError("Failed to evaluate operand: " + (index + 1), argExpressions[index].line, argExpressions[index].startingAddress, argExpressions[index].endingAddress, e);
            }
            evaluated[index] = true;
        }
        return args[index];
    }

    public Line getLine(){
        return this.line;
    }

    @SuppressWarnings("unchecked")
    protected ArgType evaluateArgument(int index, Object result){
        return (ArgType) result;
    }

    @SuppressWarnings("unchecked")
    public final void setArgExpressions(CompiledExpression[] expressions, Line line){
        this.argExpressions = expressions;
        evaluated = new boolean[expressions.length];
        Arrays.fill(evaluated, false);
        args = (ArgType[]) new Object[expressions.length];
        this.line = line;
    }


    public final void throwParameterTypeError(int i, Class<?> expected) {
        throw new ParameterTypeError("Wrong type for parameter: " + (i + 1) + " expected: " + expected.getSimpleName() + " gotten: " + getArg(i).getClass().getSimpleName(), argExpressions[i].line, argExpressions[i].startingAddress, argExpressions[i].endingAddress);
    }

    public final void throwParameterCountError(int expected) {
        if(argExpressions.length == 0){
            throw new ParameterCountError("Unexpected number of arguments, expected: " + expected + " gotten: " + argExpressions.length, line, -1, -1);
        }else{
            throw new ParameterCountError("Unexpected number of arguments, expected: " + expected + " gotten: " + argExpressions.length, line, argExpressions[0].startingAddress, argExpressions[argExpressions.length - 1].endingAddress);
        }

    }

    protected void throwLinkingException(int i, Exception e){
        throw new AssemblerError("Failed to link argument: " + i, argExpressions[i].line, argExpressions[i].startingAddress, argExpressions[i].endingAddress, e);
    }
}
