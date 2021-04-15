package org.parker.mips.assembler2.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ExpressionCompilerTest {

    @Test
    void parse(){


        /*
        Pattern p = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)((\\s+.*)?)");

        Matcher m = p.matcher("define gf kjgh");
        boolean temp = m.matches();
        m = p.matcher("define gf kjgh");
        m.find();
        String g1 = m.group(1);
        String g2 = m.group(2);
        System.out.println(m.group(1));
         */

        HashMap<Object, String> resultToExpressionStringMap= Expressions.resultExpressionMap;


        HashMap<Object, CompiledExpression> resultToExpressionMap= new HashMap<>();


        ExpressionCompiler ec = new ExpressionCompiler();
        //ec.compileExpression(" true || true || false || 1 || false", null, 0).evaluate();


        for(Map.Entry<Object, String> ex: resultToExpressionStringMap.entrySet()){
            resultToExpressionMap.put(ex.getKey(), ec.compileExpression(ex.getValue(), null, 0));
        }

        for(Map.Entry<Object, CompiledExpression> ex: resultToExpressionMap.entrySet()){
            Object val = ex.getValue().evaluate();
            //System.out.println(ex.getKey() + " " + val);
            Assert.assertEquals(ex.getKey(), val);
        }


    }

}