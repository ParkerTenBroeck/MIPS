package org.parker.mips.assembler2.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.parker.mips.assembler.util.ExpressionParser;

import java.util.Map;

class ExpressionParserTest {

    @Test
    void parse() {
        ExpressionParser ep = new ExpressionParser();

        for(Map.Entry<Object, String> ex : Expressions.resultExpressionMap.entrySet()){
            Assert.assertEquals(ex.getKey(), ep.parse(ex.getValue()));
        }
    }
}