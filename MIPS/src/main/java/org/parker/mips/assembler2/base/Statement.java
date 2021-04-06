package org.parker.mips.assembler2.base;

import org.parker.mips.assembler2.operand.Operand;
import org.parker.mips.assembler2.util.ExpressionParser;

/**
 * Any Line in assembly source that is not a comment
 */
public abstract class Statement {

    private Operand[] ops;
    protected String operandExpression;
    private boolean evaluated = false;

    protected Operand[] getOps(){
        if(evaluated) {
            return ops;
        }else{
            throw new RuntimeException("Operands have not been initialized");
        }
    }

    public boolean evaluated(){
        return this.evaluated;
    }

    public void setOperandExpression(String operandExpression){
        this.operandExpression = operandExpression;
    }

    public void evaluateOperands(ExpressionParser ep){
        if(operandExpression.matches("\\w*")){
            ops = new Operand[0];
        }else{
            Object x = ep.parse(operandExpression);
            if(x instanceof Object[]){
                ops = new Operand[((Object[]) x).length];
                for(int i = 0; i < ((Object[]) x).length; i ++){
                 if(((Object[])x)[i] instanceof Operand){
                     ops[i] = (Operand) ((Object[])x)[i];
                 }else{
                     throw new RuntimeException("bruh operands why");
                 }
                }
            }else if(x instanceof Operand){
                ops = new Operand[]{(Operand) x};
            }else{
                throw new RuntimeException("asdasdasd3o847q34875i364j87iqw4");
            }
        }
        evaluated = true;
    }


}
