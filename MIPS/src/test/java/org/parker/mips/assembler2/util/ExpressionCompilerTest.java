package org.parker.mips.assembler2.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.parker.retargetableassembler.util.CompiledExpression;
import org.parker.retargetableassembler.util.ExpressionCompiler;

import java.util.HashMap;
import java.util.Map;

public class ExpressionCompilerTest {

    @Test
    void parse(){

        HashMap<Object, String> resultToExpressionStringMap= Expressions.resultExpressionMap;
        HashMap<Object, CompiledExpression> resultToExpressionMap= new HashMap<>();

        ExpressionCompiler ec = new ExpressionCompiler();

        for(Map.Entry<Object, String> ex: resultToExpressionStringMap.entrySet()){
            CompiledExpression compiled = ec.compileExpression(ex.getValue(), null, 0);
            Assert.assertEquals(compiled.evaluate(), ec.compileExpression(compiled.toString(), null, 0).evaluate());
            resultToExpressionMap.put(ex.getKey(), compiled);
        }

        for(Map.Entry<Object, CompiledExpression> ex: resultToExpressionMap.entrySet()){
            Object val = ex.getValue().evaluate();
            Assert.assertEquals(ex.getKey(), val);
        }


    }

}