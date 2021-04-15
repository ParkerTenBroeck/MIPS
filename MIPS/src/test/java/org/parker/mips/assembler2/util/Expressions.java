package org.parker.mips.assembler2.util;

import java.util.HashMap;

public class Expressions {

    public static final HashMap<Object, String> resultExpressionMap = new HashMap<>();

    static{
        resultExpressionMap.put(1, "2-1");

        resultExpressionMap.put(1.0, "(Double)1");

        resultExpressionMap.put("This is true", "true || false && false?\"This \" + \"is \" + \"true\":\"This \" + \"is \" + \"false\"");
        resultExpressionMap.put(false?1:2, "false?1:2");

        resultExpressionMap.put(5, "+5");
        resultExpressionMap.put(-5, "-5");
        resultExpressionMap.put(2.5, "+2.5");
        resultExpressionMap.put(-2.5, "-2.5");
        resultExpressionMap.put(15, "+0xF");
        resultExpressionMap.put(-15, "-0xF");
        resultExpressionMap.put(false, "!true");
        resultExpressionMap.put(true, "!false");
        resultExpressionMap.put(-1, "~0b0");

        resultExpressionMap.put(-1, "2+-3");
        resultExpressionMap.put(-1, "2+(-3)");

        resultExpressionMap.put(17.0, "(5+4+(4*(5/2.5)))");
        resultExpressionMap.put(2, "12%5");

        resultExpressionMap.put(16, "8 << 1");
        resultExpressionMap.put(4, "8 >> 1");

        resultExpressionMap.put(true, "8 > 2");
        resultExpressionMap.put(false, "2 > 8");

        resultExpressionMap.put(false, "8 < 2");
        resultExpressionMap.put(true, "2 < 8");

        resultExpressionMap.put(1.0, "sin(toRadians(90))");
        resultExpressionMap.put(1.0, "sin(toRadians(90))");

        resultExpressionMap.put(1.0, "sin(toRadians(90))");
        resultExpressionMap.put(1.0, "sin(toRadians(90))");

        resultExpressionMap.put(true, "8 == 8");
        resultExpressionMap.put(false, "2 == 8");

        resultExpressionMap.put(false, "8 != 8");
        resultExpressionMap.put(true, "2 != 8");

        resultExpressionMap.put(Math.sin(Math.PI/6.0), "sin(PI/6)");
        resultExpressionMap.put(Math.cos(Math.PI*2.0/6.0), "cos(PI*2/6)");
        resultExpressionMap.put(Math.tan(Math.PI/4.0), "tan(PI/4)");

        resultExpressionMap.put(32.0, "pow(2,5)");
        resultExpressionMap.put(5.0, "sqrt(25)");
        resultExpressionMap.put(1.0, "sin(toRadians(90))");
        resultExpressionMap.put(1.0, "sin(toRadians(toDegrees(PI/2))");
    }

}
