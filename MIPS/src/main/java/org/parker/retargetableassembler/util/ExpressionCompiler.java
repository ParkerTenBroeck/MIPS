/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.retargetableassembler.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionCompiler {


    private static final Logger LOGGER = Logger.getLogger(ExpressionParser.class.getName());


    public static int countTopLevelExpressions(String expression){
        if(expression.trim().isEmpty())return 0;

        char[] chs = expression.toCharArray();
        int index = 0;

        int count = 1;


        int into = 0;
        boolean string = false;

        while (index >= 0 && index < chs.length) {
            if (chs[index] == '(' && !string) {
                into++;
            } else if (chs[index] == ')' && !string) {
                into--;
                if (into == 0) {
                    break;
                }
            } else if (chs[index] == '"') {
                string = !string;
            } else if (chs[index] == '\\' && string) {
                if (chs[index + 1] == '"') {
                    index += 2;
                    continue;
                }
            }else if(chs[index] == ',' && into == 0){
                count ++;
            }

            index++;
        }

        return count;
    }

    public ExpressionCompiler() {
    }


    public static void main(String... args){

        ExpressionCompiler ec = new ExpressionCompiler();
        CompiledExpression e = ec.compileExpression("(5+4+(4*(5/2.5)))", null, 0);

        Expression en = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(e);
            out.flush();
            byte[] yourBytes = bos.toByteArray();

            try{
                ByteArrayInputStream bin = new ByteArrayInputStream(yourBytes);
                ObjectInputStream in = new ObjectInputStream(bin);
                en = (Expression) in.readObject();
            }catch(Exception ee){
                System.out.println(ee);
            }

        }catch(Exception eee){
            System.out.println(eee);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        System.out.println(en.evaluate() + " " + e.evaluate());
    }

    public synchronized CompiledExpression compileExpression(String str, Line line, int offset) {
        PrivateExpressionCompiler ec = new PrivateExpressionCompiler(str, line, offset);
        return ec.compileExpression();
    }

    public final synchronized CompiledExpression[] compileExpressionsAsArray(String str, Line line, int offset){
        PrivateExpressionCompiler ec = new PrivateExpressionCompiler(str, line, offset);
        return ec.compileTopLevelExpressions();
    }

    private static final Pattern namePattern = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*");
    private static final Pattern castPattern = Pattern.compile("\\s*([(][\\s]*([a-zA-Z_$][a-zA-Z_$0-9]*)[\\s]*[)])\\s*[a-zA-Z_$0-9]+");
    private static final HashMap<String, Class> castMap = new HashMap<>();
    private static final HashMap<String, Class> primitiveCastMap = new HashMap<>();

    static{
        primitiveCastMap.put("Double", Double.class);
        primitiveCastMap.put("double", Double.class);
        primitiveCastMap.put("Float", Float.class);
        primitiveCastMap.put("float", Float.class);

        primitiveCastMap.put("Long", Long.class);
        primitiveCastMap.put("long", Long.class);
        primitiveCastMap.put("Integer", Integer.class);
        primitiveCastMap.put("int", Integer.class);
        primitiveCastMap.put("Short", Short.class);
        primitiveCastMap.put("short", Short.class);
        primitiveCastMap.put("Byte", Byte.class);
        primitiveCastMap.put("byte", Byte.class);

        primitiveCastMap.put("Character", Character.class);
        primitiveCastMap.put("char", Character.class);
    }

    private interface Expression{
        Object evaluate();
    }

    private class PrivateExpressionCompiler{

        class CompilationError extends org.parker.retargetableassembler.exception.ExpressionError {

            public CompilationError(String message, final int s, final int e){
                super(message, expressionLine, s, e);
            }

            public CompilationError(String message, final int s, final int e, final Exception ex){
                super(message, expressionLine, s, e, ex);
            }

            public CompilationError(String message, final int s){
                super(message, expressionLine, s);
            }
        }


        private class ExpressionError extends org.parker.retargetableassembler.exception.ExpressionError {

            public ExpressionError(String message, final int s, final int e){
                super(message,expressionLine, s + expressionLineIndexOffset, e + expressionLineIndexOffset);
            }

            public ExpressionError(String message, final int s, final int e, final Exception ex){
                super(message,expressionLine, s + expressionLineIndexOffset, e + expressionLineIndexOffset, ex);
            }
        }

        @Deprecated
        public PrivateExpressionCompiler(String str){
            this(str, null, -1);
        }

        public PrivateExpressionCompiler(String str, Line line, int offset){
            this.str = str;
            this.expressionLine = line;
            this.expressionLineIndexOffset = offset;
        }

        private final Line expressionLine;
        private final int expressionLineIndexOffset;
        private final String str;
        private int pos = -1, ch;

        private void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        private boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        private boolean eat(String stringToEat) {
            removeWhiteSpace();
            if (str.startsWith(stringToEat, pos)) {
                for (int i = 0; i < stringToEat.length(); i++) {
                    nextChar();
                }
                return true;
            }
            return false;
        }

        private int removeWhiteSpace(){
            while(ch == ' ')nextChar();
            return pos;
        }

        private boolean next(String next) {
            removeWhiteSpace();
            return str.startsWith(next, pos);
        }

        public CompiledExpression compileExpression() {
            if(str.trim().isEmpty())return null;

            pos = -1;
            ch = 0;
            nextChar();
            CompiledExpression x;
            try {
                Expression e = parseLevel15();
                x = new CompiledExpression(expressionLine,  expressionLineIndexOffset, pos + expressionLineIndexOffset) {
                    @Override
                    public Object evaluate() {
                        return e.evaluate();
                    }
                    @Override
                    public String toString() {
                        return e.toString();
                    }
                };
            } catch (Exception e) {
                throw new RuntimeException("Error At index: " + pos + ": " + e.getMessage(), e);
            }

            if (pos < str.length()) throw new CompilationError("Unexpected: " + (char) ch, pos + expressionLineIndexOffset);
            return x;
        }

        public CompiledExpression[] compileTopLevelExpressions() {
            if(str.trim().isEmpty())return new CompiledExpression[0];

            pos = -1;
            ch = 0;
            nextChar();
            CompiledExpression[] x;
            x = parseTopLevel15();

            if (pos < str.length()) throw new CompilationError("Unexpected: " + (char) ch, pos + expressionLineIndexOffset);
            return x;
        }

        private synchronized CompiledExpression[] parseTopLevel15() {
            int start = removeWhiteSpace();
            Expression xEM = parseLevel14();
            int end = pos;

            final List<CompiledExpression> list = new ArrayList<>();

            list.add(new CompiledExpression(expressionLine, start + expressionLineIndexOffset, end + expressionLineIndexOffset) {
                @Override
                public Object evaluate() {
                    return xEM.evaluate();
                }

                @Override
                public String toString() {
                    return xEM.toString();
                }
            });

            for (; ; ) {
                //start = removeWhiteSpace();
                if (eat(",")) {
                    Expression xEMT = parseLevel14();
                    end = pos;
                    list.add(new CompiledExpression(expressionLine, start + expressionLineIndexOffset + 1, end + expressionLineIndexOffset) {
                        @Override
                        public Object evaluate() {
                            return xEMT.evaluate();
                        }

                        @Override
                        public String toString() {
                            return xEMT.toString();
                        }
                    });
                } else {
                    return list.toArray(new CompiledExpression[0]);
                }
                start = end;
            }
        }

        private synchronized Expression parseLevel15() {
            Expression xEM = parseLevel14();

            final List<Expression> list = new ArrayList<>();
            for (; ; ) {
                if (eat(",")) {
                    if (list.size()  == 0) {
                        list.add(xEM);
                    }
                    list.add(parseLevel14());

                } else {
                    if (list.size() == 0) {
                        return xEM;
                    } else {
                        return new Expression() {
                            @Override
                            public Object evaluate() {
                                Object[] x = new Object[list.size()];
                                for (int i = 0; i < list.size(); i++) {
                                    x[i] = list.get(i).evaluate();
                                }
                                return x;
                            }

                            @Override
                            public String toString() {
                                List<String> a = new ArrayList<>();
                                for(Expression e: list){
                                    a.add(e.toString());
                                }
                                String temp = "";

                                for(int i = 0; i < a.size(); i ++){
                                    temp += a.get(i);
                                    if(i < a.size() - 1){
                                        temp += ", ";
                                    }
                                }

                                return temp;
                            }
                        };
                    }
                }
            }
        }

        private Expression parseLevel14() {
            return parseLevel13();
        }

        private Expression parseLevel13() {
            final int s = removeWhiteSpace();
            Expression xEM = parseLevel12();

            if(eat("?")){
                Expression EM1 = parseLevel15();
                if(eat(":")){
                    Expression EM2 = parseLevel15();
                    final int e = pos;

                    return new Expression() {
                        @Override
                        public Object evaluate() {
                            Object d = xEM.evaluate();
                            Object o1 = EM1.evaluate();
                            Object o2 = EM2.evaluate();
                            if (d instanceof Boolean) {
                                return (Boolean) d ? o1 : o2;
                            } else {
                                throw new ExpressionError("Expected Boolean before ? got: " + d.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEM.toString() + " ? " + EM1.toString() + " : " + EM2.toString();
                        }
                    };
                }else{
                    throw new IllegalArgumentException("idl");
                }
            }else{
                return xEM;
            }
        }

        private Expression parseLevel12() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel11();

            for (; ; ) {
                final int s = si;
                if (eat("||")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel11();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Boolean && y instanceof Boolean) {
                                return (Boolean) x || (Boolean) y;
                            } else {
                                throw new ExpressionError("Cannot preform Logical OR between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " || " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        private Expression parseLevel11() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel10();

            for (;;) {
                final int s = si;
                if (eat("&&")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel10();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Boolean && y instanceof Boolean) {
                                return (Boolean) x && (Boolean) y;
                            } else {
                                throw new ExpressionError("Cannot preform Logical AND between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " && " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        private Expression parseLevel10() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel9();

            for (; ; ) {
                final int s = si;
                if (!next("||") && eat("|")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel9();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number && y instanceof Number) {
                                return bitwiseOr((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot preform bitwise OR between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " | " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        private Expression parseLevel9() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel8();

            for (; ; ) {
                final int s = si;
                if (eat("^")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel8();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number && y instanceof Number) {
                                return bitwiseXor((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot preform bitwise XOR between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " ^ " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        private Expression parseLevel8() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel7();

            for (; ; ) {
                final int s = si;
                if (!next("&&") && eat("&")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel7();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number && y instanceof Number) {
                                return bitwiseAnd((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot preform bitwise AND between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " & " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        private Expression parseLevel7() {
            final int s = removeWhiteSpace();
            Expression xEM = parseLevel6();

            if (eat("==")) {
                Expression xEP = xEM, yEP = parseLevel6();
                final int e = pos;
                return new Expression() {
                    @Override
                    public Object evaluate() {
                        Object x = xEP.evaluate();
                        Object y = yEP.evaluate();
                        if (x instanceof Number && y instanceof Number) {
                            return NUMBER_COMPARATOR.compare(x, y) == 0;
                        } else {
                            throw new ExpressionError("Cannot use '==' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                        }
                    }

                    @Override
                    public String toString() {
                        return xEP.toString() + " == " + yEP.toString();
                    }
                };
            } else if (eat("!=")) {
                Expression xEP = xEM, yEP = parseLevel6();
                final int e = pos;

                return new Expression() {
                    @Override
                    public Object evaluate() {
                        Object x = xEP.evaluate();
                        Object y = yEP.evaluate();
                        if (x instanceof Number && y instanceof Number) {
                            return NUMBER_COMPARATOR.compare(x, y) != 0;
                        } else {
                            throw new ExpressionError("Cannot use '!=' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                        }
                    }

                    @Override
                    public String toString() {
                        return xEP.toString() + " != " + yEP.toString();
                    }
                };
            } else {
                return xEM;
            }

        }

        private Expression parseLevel6() {
            final int s = removeWhiteSpace();
            Expression xEM = parseLevel5();

            if (eat("<")) {
                Expression xEP = xEM, yEP = parseLevel5();
                final int e = pos;

                return new Expression() {
                    @Override
                    public Object evaluate() {
                        Object x = xEP.evaluate();
                        Object y = yEP.evaluate();
                        if (x instanceof Number && y instanceof Number) {
                            return NUMBER_COMPARATOR.compare(x, y) < 0;
                        } else {
                            throw new ExpressionError("Cannot use '<' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                        }
                    }

                    @Override
                    public String toString() {
                        return xEP.toString() + " < " + yEP.toString();
                    }
                };
            } else if (eat("<=")) {
                Expression xEP = xEM, yEP = parseLevel5();
                final int e = pos;

                return new Expression() {
                    @Override
                    public Object evaluate() {
                        Object x = xEP.evaluate();
                        Object y = yEP.evaluate();
                        if (x instanceof Number && y instanceof Number) {
                            return NUMBER_COMPARATOR.compare(x, y) <= 0;
                        } else {
                            throw new ExpressionError("Cannot use '<=' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                        }
                    }

                    @Override
                    public String toString() {
                        return xEP.toString() + " <= " + yEP.toString();
                    }
                };
            } else if (eat(">")) {
                Expression xEP = xEM, yEP = parseLevel5();
                final int e = pos;

                return new Expression() {
                    @Override
                    public Object evaluate() {
                        Object x = xEP.evaluate();
                        Object y = yEP.evaluate();
                        if (x instanceof Number && y instanceof Number) {
                            return NUMBER_COMPARATOR.compare(x, y) > 0;
                        } else {
                            throw new ExpressionError("Cannot use '>' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                        }
                    }

                    @Override
                    public String toString() {
                        return xEP.toString() + " > " + yEP.toString();
                    }
                };
            } else if (eat(">=")) {
                Expression xEP = xEM, yEP = parseLevel5();
                final int e = pos;

                return new Expression() {
                    @Override
                    public Object evaluate() {
                        Object x = xEP.evaluate();
                        Object y = yEP.evaluate();
                        if (x instanceof Number && y instanceof Number) {
                            return NUMBER_COMPARATOR.compare(x, y) >= 0;
                        } else {
                            throw new ExpressionError("Cannot use '>=' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                        }
                    }

                    @Override
                    public String toString() {
                        return xEP.toString() + " >= " + yEP.toString();
                    }
                };
            } else {
                return xEM;
            }
        }

        private Expression parseLevel5() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel4();

            for (; ; ) {
                final int s = si;
                if (eat("<<")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel4();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number && y instanceof Number) {
                                return shiftLeft((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot use '<<' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " << " + yEP.toString();
                        }
                    };
                } else if (eat(">>")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel4();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number && y instanceof Number) {
                                return shiftRight((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot use '>>' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " >> " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        /**
         * Level 4 used for + / - operators
         * (addition / subtraction)
         *
         */
        private Expression parseLevel4() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel3();
            for (; ; ) {
                final int s = si;
                if (eat("+")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel3();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof String || y instanceof String) {
                                return x.toString() + y.toString();
                            } else if (x instanceof Number || y instanceof Number) {
                                return add((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot use '+' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " + " + yEP.toString();
                        }
                    };
                } else if (eat("-")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel3();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number || y instanceof Number) {
                                return subtract((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot use '-' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " - " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        /**
         * level 3 is responsible for multiplication division and mods
         *
         */
        private Expression parseLevel3() {
            int si = removeWhiteSpace();
            Expression xEM = parseLevel2();
            for (; ; ) {
                final int s = si;
                if (eat("*")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel2();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number || y instanceof Number) {
                                return multiply((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot use '*' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " * " + yEP.toString();
                        }
                    };
                } else if (eat("/")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel2();
                    final int e = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number || y instanceof Number) {
                                return divide((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot use '/' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " / " + yEP.toString();
                        }
                    };
                } else if (eat("%")) {
                    si = removeWhiteSpace();
                    Expression xEP = xEM, yEP = parseLevel2();
                    final int e = pos;
                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEP.evaluate();
                            Object y = yEP.evaluate();
                            if (x instanceof Number || y instanceof Number) {
                                return mod((Number) x, (Number) y);
                            } else {
                                throw new ExpressionError("Cannot use '%' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return xEP.toString() + " % " + yEP.toString();
                        }
                    };
                } else {
                    return xEM;
                }
            }
        }

        /**
         * Level 2 used for + / - (right to left)
         *
         */
        private Expression parseLevel2() {
            int si = removeWhiteSpace();
            for (; ; ) {
                final int s = si;
                if (eat("+")) {
                    si = removeWhiteSpace();
                    Expression xEM = parseLevel1();
                    final int e = pos;
                    return new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEM.evaluate();
                            if (x instanceof Number) {
                                return x;
                            } else {
                                throw new ExpressionError("cannot use '+' modifier on: " + x.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return " +" + xEM.toString();
                        }
                    };
                } else if (eat("-")) {
                    {
                        si = removeWhiteSpace();
                        final Expression xEM = parseLevel1();
                        final int e = pos;
                        return new Expression() {
                            @Override
                            public Object evaluate() {
                                Object x = xEM.evaluate();
                                if (x instanceof Number) {
                                    return subtract(0, (Number) x);
                                } else {
                                    throw new ExpressionError("Cannot use '-' modifier on: " + x.getClass().getSimpleName(), s, e);
                                }
                            }

                            @Override
                            public String toString() {
                                return " -" + xEM.toString();
                            }
                        };
                    }
                } else if (eat("~")) {
                    si = removeWhiteSpace();
                    final Expression xEM = parseLevel1();
                    final int e = pos;
                    return () -> new Expression() {
                            @Override
                            public Object evaluate() {
                                Object x = xEM.evaluate();
                                if (x instanceof Number) {
                                    return invert((Number) x);
                                } else {
                                    throw new ExpressionError("Cannot use '~' modifier on: " + x.getClass().getSimpleName(), s, e);
                                }
                            }
                            @Override
                            public String toString() {
                                return " ~" + xEM.toString();
                            }
                        };
                } else if (eat("!")) {
                    si = removeWhiteSpace();
                    final Expression xEM = parseLevel1();
                    final int e = pos;
                    return new Expression() {
                        @Override
                        public Object evaluate() {
                            Object x = xEM.evaluate();
                            if (x instanceof Boolean) {
                                return !(Boolean) x;
                            } else {
                                throw new ExpressionError("Cannot use '!' modifier on: " + x.getClass().getSimpleName(), s, e);
                            }
                        }

                        @Override
                        public String toString() {
                            return " !" + xEM.toString();
                        }
                    };
                } else {
                    si = removeWhiteSpace();
                    Matcher m = castPattern.matcher(str.substring(pos));
                    if(m.find()){
                        final String cast = m.group(1);
                        final String cName = m.group(2);
                        eat(cast);

                        final Expression xEM = parseLevel1();
                        final int e = pos;
                        return new Expression() {
                            @Override
                            public Object evaluate() {
                                Object x = xEM.evaluate();
                                if (primitiveCastMap.containsKey(cName)) {
                                        Class c = primitiveCastMap.get(cName);
                                        if (x instanceof Number) {
                                            if (c == Long.class) {
                                                x = new Long(((Number) x).longValue());
                                            } else if (c == Integer.class) {
                                                x = new Integer(((Number) x).intValue());
                                            } else if (c == Short.class) {
                                                x = new Short(((Number) x).shortValue());
                                            } else if (c == Byte.class) {
                                                x = new Byte(((Number) x).byteValue());
                                            } else if (c == Double.class) {
                                                x = new Double(((Number) x).doubleValue());
                                            } else if (c == Float.class) {
                                                x = new Float(((Number) x).floatValue());
                                            } else if (c == Character.class){
                                                if(x instanceof Long || x instanceof Integer || x instanceof Short || x instanceof Byte){
                                                    x = new Character((char)((Number)x).longValue());
                                                }else{
                                                    throw new ClassCastException("Cannot cast: " + x.getClass().getSimpleName() + " cannot be cast to char || Character");
                                                }
                                        } else {
                                            throw new ExpressionError(x.getClass().getSimpleName() + " cannot be cast to a: " + cName, s, e);
                                        }
                                    }
                                } else if (castMap.containsKey(cName)) {
                                    x = castMap.get(cName).cast(x);
                                } else {
                                    throw new ExpressionError("Cannot find class: " + cName, s, e);
                                }
                                return x;
                            }

                            @Override
                            public String toString() {
                                return '(' + cName + ')' + xEM.toString();
                            }
                        };
                    }

                    return parseLevel1();
                }
            }
        }


        /**
         * level 1 is responsible for variable, constant, and function access
         *
         */
        private Expression parseLevel1() {

            final int s = removeWhiteSpace();

            final Expression xEM;
            final int startPos = this.pos;
            if (eat('(')) { // parentheses
                Expression internal = parseLevel15();
                xEM = new Expression() {
                    @Override
                    public Object evaluate() {
                        return internal.evaluate();
                    }

                    @Override
                    public String toString() {
                        return '(' + internal.toString() + ')';
                    }
                };
                eat(')');
            } else if ((ch >= '0' && ch <= '9')) { // numbers

                while ((ch >= '0' && ch <= '9') || ch == '.' || (ch >= 'a' && ch <= 'z')|| (ch >= 'A' && ch <= 'Z')) nextChar();
                if (str.substring(startPos, this.pos).contains(".")) {
                    final int end = this.pos;
                    final String string = str;
                    final int e = pos;
                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            try {
                                return Double.parseDouble(string.substring(startPos, end));
                            } catch (Exception ex) {
                                throw new ExpressionError("Failed to parse double: " + string.substring(startPos, end), s, e, ex);
                            }
                        }

                        @Override
                        public String toString() {
                            return string.substring(startPos, end);
                        }
                    };
                } else {
                    final int end = this.pos;
                    final String string = str;
                    final int e = pos;
                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            try {
                                return parseInt(string.substring(startPos, end));
                            } catch (Exception ex) {
                                throw new ExpressionError("Failed to parse int: " + string.substring(startPos, end), s, e, ex);
                            }
                        }

                        @Override
                        public String toString() {
                            return string.substring(startPos, end);
                        }
                    };
                }
            } else if (Character.isAlphabetic(ch) || ch == '_' || ch == '$') { // functions
                while (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_'|| ch == '$') nextChar();
                final String token = str.substring(startPos, this.pos);
                final int e = pos;

                switch (token) {
                    case "true":
                    case "TRUE":
                        return new Expression() {
                            @Override
                            public Object evaluate() {
                                return true;
                            }

                            @Override
                            public String toString() {
                                return "true";
                            }
                        };
                    case "false":
                    case "FALSE":
                        return new Expression() {
                            @Override
                            public Object evaluate() {
                                return false;
                            }

                            @Override
                            public String toString() {
                                return "false";
                            }
                        };
                    case "pi":
                    case "PI":
                        return new Expression() {
                            @Override
                            public Object evaluate() {
                                return Math.PI;
                            }

                            @Override
                            public String toString() {
                                return "pi";
                            }
                        };
                }

                if (next("(")) {
                    final Expression args = parseLevel15();
                    final int endFunc = pos;

                    xEM = new Expression() {
                        @Override
                        public Object evaluate() {
                            try {
                                return parseFunction(token, args.evaluate());
                            } catch (Exception ex) {
                                throw new ExpressionError("Failed to parse function: " + token, s, endFunc, ex);
                            }
                        }

                        @Override
                        public String toString() {
                            return token + args.toString();
                        }
                    };
                } else {
                    final String ppToken = preProcessVariableMnemonic(token);
                    if(namePattern.matcher(ppToken).matches()){


                        removeWhiteSpace();
                        if(eat(".")){

                            final int memberAccessStart = pos;
                            if (Character.isAlphabetic(ch) || ch == '_' || ch == '$') { // functions
                                while (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_' || ch == '$')
                                    nextChar();
                            }
                            final int memberAccessEnd = pos;
                            final String memberAccess = str.substring(memberAccessStart, memberAccessEnd);

                            xEM = new Expression() {
                                @Override
                                public Object evaluate() {
                                    try {
                                        return parseMemberAccess(parseVariable(ppToken), memberAccess);
                                    } catch (Exception ex) {
                                        throw new ExpressionError("Failed to parse symbol", s, memberAccessEnd, ex);
                                    }
                                }

                                @Override
                                public String toString() {
                                    return ppToken + "." + memberAccess;
                                }
                            };

                        }else {
                            xEM = new Expression() {
                                @Override
                                public Object evaluate() {
                                    try {
                                        return parseVariable(ppToken);
                                    } catch (Exception ex) {
                                        throw new ExpressionError("Failed to parse symbol", s, e, ex);
                                    }
                                }

                                @Override
                                public String toString() {
                                    return ppToken;
                                }
                            };
                        }
                    }else{
                        xEM = new Expression() {
                            private CompiledExpression pp;
                            {
                                pp = new PrivateExpressionCompiler(ppToken).compileExpression();
                            }
                            @Override
                            public Object evaluate() {
                                try {
                                    return pp.evaluate();
                                } catch (Exception error) {
                                    throw new ExpressionError("Failed to parse: " + ppToken + " from value: " + token, s, e, error);
                                }
                            }

                            @Override
                            public String toString() {
                                return pp.toString();
                            }
                        };
                    }
                }
            } else if (ch == '"') {
                nextChar();
                while (ch != '"') {
                    if(ch == '\\'){
                        nextChar();
                        if(ch == '"'){
                            nextChar();
                        }
                    }
                    nextChar();
                }
                nextChar();

                final String string = str;
                final int start = startPos + 1;
                final int end = pos - 1;
                xEM = new Expression() {
                    @Override
                    public Object evaluate() {
                        return string.substring(start, end);
                    }

                    @Override
                    public String toString() {
                        return '"' + string.substring(start, end) + '"';
                    }
                };
            } else {
                throw new CompilationError("Unexpected: " + (char) ch, s, s);
            }

            return xEM;
        }

    }

    protected Object parseMemberAccess(Object parseVariable, String memberAccess) {
        throw new IllegalArgumentException("Cannot access member: " + memberAccess + " on: " + parseVariable.getClass().getSimpleName());
    }

    protected String preProcessVariableMnemonic(String token) {
        return token;
    }

    protected Object parseVariable(String token){
        throw new IllegalArgumentException("Variable: " + token + " not found");
    }

    protected Object parseFunction(String token, Object parms) {

        if (FUNCTION_MAP.containsKey(token)) {
            return FUNCTION_MAP.get(token).parse(parms);
        } else {
            throw new IllegalArgumentException("Function: " + token + " is not defined");
        }
    }

    private static final HashMap<String, ExpressionCompiler.FunctionParser> FUNCTION_MAP = new HashMap<>();

    private static abstract class FunctionParser {
        abstract public Object parse(Object parms);
    }

    static {
        FUNCTION_MAP.put("sqrt", new ExpressionCompiler.FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.sqrt(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("Sqrt function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("sin", new ExpressionCompiler.FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.sin(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("sin function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("cos", new ExpressionCompiler.FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.cos(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("cos function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("tan", new ExpressionCompiler.FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.tan(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("tan function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("toRadians", new ExpressionCompiler.FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.toRadians(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("toRadians function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("toDegrees", new ExpressionCompiler.FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.toDegrees(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("toDegrees function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("pow", new ExpressionCompiler.FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Object[]) {
                    Object[] newParms = (Object[]) parms;
                    if (newParms.length == 2) {
                        if (newParms[0] instanceof Number || newParms[1] instanceof Number) {
                            return Math.pow(((Number) newParms[0]).doubleValue(), ((Number) newParms[1]).doubleValue());
                        } else {
                            throw new IllegalArgumentException("Invalid parameters for function Pow found: " + newParms.getClass().getSimpleName() + " and " + newParms.getClass().getSimpleName() + " but not Number and Number");
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid number of parameters for function Pow requires 2 found: " + newParms.length);
                    }
                } else {
                    throw new IllegalArgumentException("Pow function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });
    }


    public static Number add(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() + b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() + b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() + b.longValue();
        } else {
            return a.intValue() + b.intValue();
        }
    }

    public static Number subtract(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() - b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() - b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() - b.longValue();
        } else {
            return a.intValue() - b.intValue();
        }
    }

    public static Number multiply(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() * b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() * b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() * b.longValue();
        } else {
            return a.intValue() * b.intValue();
        }
    }

    public static Number divide(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() / b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() / b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() / b.longValue();
        } else {
            return a.intValue() / b.intValue();
        }
    }

    public static Number mod(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            return a.doubleValue() % b.doubleValue();
        } else if (a instanceof Float || b instanceof Float) {
            return a.floatValue() % b.floatValue();
        } else if (a instanceof Long || b instanceof Long) {
            return a.longValue() % b.longValue();
        } else {
            return a.intValue() % b.intValue();
        }
    }

    public static Number shiftLeft(Number a, Number b) {
        if (a instanceof Integer) {
            return a.intValue() << b.intValue();
        } else if (a instanceof Long) {
            return a.longValue() << b.intValue();
        } else if (a instanceof Short) {
            return a.shortValue() << b.intValue();
        } else if (a instanceof Byte) {
            return a.byteValue() << b.intValue();
        } else {
            return a.intValue() << b.intValue();
        }
    }

    public static Number shiftRight(Number a, Number b) {
        if (a instanceof Integer) {
            return a.intValue() >> b.intValue();
        } else if (a instanceof Long) {
            return a.longValue() >> b.intValue();
        } else if (a instanceof Short) {
            return a.shortValue() >> b.intValue();
        } else if (a instanceof Byte) {
            return a.byteValue() >> b.intValue();
        } else {
            return a.intValue() >> b.intValue();
        }
    }

    public static Number bitwiseAnd(Number a, Number b) {
        if (a instanceof Integer) {
            return a.intValue() & b.intValue();
        } else if (a instanceof Long) {
            return a.longValue() & b.intValue();
        } else if (a instanceof Short) {
            return a.shortValue() & b.intValue();
        } else if (a instanceof Byte) {
            return a.byteValue() & b.intValue();
        } else {
            return a.intValue() & b.intValue();
        }
    }

    public static Number bitwiseOr(Number a, Number b) {
        if (a instanceof Integer) {
            return a.intValue() | b.intValue();
        } else if (a instanceof Long) {
            return a.longValue() | b.intValue();
        } else if (a instanceof Short) {
            return a.shortValue() | b.intValue();
        } else if (a instanceof Byte) {
            return a.byteValue() | b.intValue();
        } else {
            return a.intValue() | b.intValue();
        }
    }

    public static Number bitwiseXor(Number a, Number b) {
        if (a instanceof Integer) {
            return a.intValue() ^ b.intValue();
        } else if (a instanceof Long) {
            return a.longValue() ^ b.intValue();
        } else if (a instanceof Short) {
            return a.shortValue() ^ b.intValue();
        } else if (a instanceof Byte) {
            return a.byteValue() ^ b.intValue();
        } else {
            return a.intValue() ^ b.intValue();
        }
    }

    public static Number invert(Number a) {
        if (a instanceof Integer) {
            return ~a.intValue();
        } else if (a instanceof Long) {
            return ~a.longValue();
        } else if (a instanceof Short) {
            return ~a.shortValue();
        } else if (a instanceof Byte) {
            return ~a.byteValue();
        } else {
            return ~a.intValue();
        }
    }

    private static final ExpressionCompiler.NumberComparator NUMBER_COMPARATOR = new ExpressionCompiler.NumberComparator();

    private static class NumberComparator implements Comparator {
        @SuppressWarnings("unchecked")
        @Override
        public int compare(Object number1, Object number2) {
            if (number2.getClass().equals(number1.getClass())) {
                // both numbers are instances of the same type!
                if (number1 instanceof Comparable) {
                    // and they implement the Comparable interface
                    return ((Comparable) number1).compareTo(number2);
                }
            }
            // for all different Number types, let's check there double values
            if (((Number) number1).doubleValue() < ((Number) number2).doubleValue())
                return -1;
            if (((Number) number1).doubleValue() > ((Number) number2).doubleValue())
                return 1;
            return 0;
        }
    }

    public static int parseInt(String string) {

        int temp;

        if (string.startsWith("0b")) {
            temp = (int) Long.parseLong(string.split("b")[1], 2);
        } else if (string.startsWith("0x")) {
            temp = (int) Long.parseLong(string.split("x")[1], 16);
        } else if (string.contains("x")) {
            temp = (int) Long.parseLong(string.split("x")[1], Integer.parseInt(string.split("x")[0]));
        } else {
            temp = (int) Long.parseLong(string.trim());
        }
        return temp;

    }


}
