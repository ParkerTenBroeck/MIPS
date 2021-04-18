package org.parker.mips.assembler.base.preprocessor;

import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.util.ExpressionCompiler;
import org.parker.mips.assembler.util.linking.Label;

public class BaseExpressionCompiler<A extends BaseAssembler, P extends BasePreProcessor> extends ExpressionCompiler {

    protected A assembler;
    protected P preProcessor;

    public BaseExpressionCompiler(){
    }

    public void setPreProcessor(P preProcessor){
        this.preProcessor = preProcessor;
    }
    public void setAssembler(A assembler){
        this.assembler = assembler;
    }

    @Override
    protected Object parseVariable(String token) {
        if(true){
            return assembler.getLabel(token);
        }else {
            return super.parseVariable(token);
        }
    }

    @Override
    protected String preProcessVariableMnemonic(String token) {
        if(preProcessor.isDefinedValue(token)){
            Object d = preProcessor.getDefinedValue(token);
            if(d instanceof String){
                return (String) d;
            }else{
                throw new IllegalArgumentException("Cannot insert: " + d.getClass().getSimpleName());
            }
        }else{
            return super.preProcessVariableMnemonic(token);
        }
    }

    @Override
    protected Object parseMemberAccess(Object variable, String memberAccess) {
        if(variable instanceof Label){
            if(memberAccess.equals("byteAddress")){
                return ((Label) variable).getAddress();
            }
        }
        return super.parseMemberAccess(variable, memberAccess);
    }
}
