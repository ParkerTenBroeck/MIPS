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
package org.parker.mips.assembler.util;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
@SuppressWarnings("all")
public class ExpressionParser {

    private static final Logger LOGGER = Logger.getLogger(ExpressionParser.class.getName());

    public ExpressionParser() {
    }

    private String str;

    public Object parse(String str) {
        this.str = str;
        return parse();
    }


    int pos = -1, ch;

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
        while (ch == ' ') nextChar();
        if (str.startsWith(stringToEat, pos)) {
            for (int i = 0; i < stringToEat.length(); i++) {
                nextChar();
            }
            return true;
        }
        return false;
    }

    private boolean next(String next) {
        while (ch == ' ') nextChar();
        return str.startsWith(next, pos);
    }

    private Object parse() {
        pos = -1;
        ch = 0;
        nextChar();
        Object x;
        try {
            x = parseLevel15();
        } catch (Exception e) {
            throw new RuntimeException("Error At index: " + pos + ": " + e.getMessage(), e);
        }

        if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch + " at index: " + pos);
        return x;
    }

    private Object parseLevel15() {
        Object x = parseLevel14();

        List<Object> list = null;
        for (; ; ) {
            if (eat(",")) {
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(x);
                }
                list.add(parseLevel14());

            } else {
                if (list == null) {
                    return x;
                } else {
                    return list.toArray();
                }
            }
        }
    }

    private Object parseLevel14() {
        return parseLevel13();
    }

    private Object parseLevel13() {
        Object x = parseLevel12();
        if(eat("?")){
            Object o1 = parseLevel15();
            if(eat(":")){
                Object o2 = parseLevel15();
                return (Boolean)x?o1:o2;
            }else{
                throw new IllegalArgumentException("idl");
            }
        }else{
            return x;
        }
    }

    private Object parseLevel12() {
        Object x = parseLevel11();

        for (; ; ) {
            if (eat("||")) {
                Object y = parseLevel11();
                if (x instanceof Boolean && y instanceof Boolean) {
                    return (Boolean) x || (Boolean) y;
                } else {
                    throw new IllegalArgumentException("Cannot preform Logical OR between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }

    private Object parseLevel11() {
        Object x = parseLevel10();

        for (;;) {
            if (eat("&&")) {
                Object y = parseLevel10();
                if (x instanceof Boolean && y instanceof Boolean) {
                    return (Boolean) x && (Boolean) y;
                } else {
                    throw new IllegalArgumentException("Cannot preform Logical AND between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }

    private Object parseLevel10() {
        Object x = parseLevel9();

        for (; ; ) {
            if (!next("||") && eat("|")) {
                Object y = parseLevel9();
                if (x instanceof Number && y instanceof Number) {
                    return bitwiseOr((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot preform bitwise OR between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }

    private Object parseLevel9() {
        Object x = parseLevel8();

        for (; ; ) {
            if (eat("^")) {
                Object y = parseLevel8();
                if (x instanceof Number && y instanceof Number) {
                    return bitwiseXor((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot preform bitwise XOR between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }

    private Object parseLevel8() {
        Object x = parseLevel7();

        for (; ; ) {
            if (!next("&&") && eat("&")) {
                Object y = parseLevel7();
                if (x instanceof Number && y instanceof Number) {
                    return bitwiseAnd((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot preform bitwise AND between: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }

    private Object parseLevel7() {
        Object x = parseLevel6();

        if (eat("==")) {
            Object y = parseLevel6();
            if(x instanceof Number && y instanceof Number){
                return NUMBER_COMPARATOR.compare(x,y) == 0;
            }else{
                throw new IllegalArgumentException("Cannot use '==' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
            }
        } else if (eat("!=")) {
            Object y = parseLevel6();
            if(x instanceof Number && y instanceof Number){
                return NUMBER_COMPARATOR.compare(x,y) != 0;
            }else{
                throw new IllegalArgumentException("Cannot use '!=' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
            }
        } else {
            return x;
        }

    }

    private Object parseLevel6() {
        Object x = parseLevel5();

        if (eat("<")) {
            Object y = parseLevel5();
            if(x instanceof Number && y instanceof  Number){
                return NUMBER_COMPARATOR.compare(x,y) < 0;
            }else{
                throw new IllegalArgumentException("Cannot use '<' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
            }
        } else if (eat("<=")) {
            Object y = parseLevel5();
            if(x instanceof Number && y instanceof Number){
                return NUMBER_COMPARATOR.compare(x,y) <= 0;
            }else{
                throw new IllegalArgumentException("Cannot use '<=' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
            }
        } else if (eat(">")) {
            Object y = parseLevel5();
            if(x instanceof Number && y instanceof Number){
                return NUMBER_COMPARATOR.compare(x,y) > 0;
            }else{
                throw new IllegalArgumentException("Cannot use '>' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
            }
        } else if (eat(">=")) {
            Object y = parseLevel5();
            if(x instanceof Number && y instanceof Number){
                return NUMBER_COMPARATOR.compare(x,y) >= 0;
            }else{
                throw new IllegalArgumentException("Cannot use '>=' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
            }
        } else {
            return x;
        }
    }

    private Object parseLevel5() {
        Object x = parseLevel4();
        for (; ; ) {
            if (eat("<<")) {
                Object y = parseLevel4();
                if(x instanceof Number && y instanceof  Number){
                    return shiftLeft((Number)x,(Number)y);
                }else{
                    throw new IllegalArgumentException("Cannot use '<<' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else if (eat(">>")) {
                Object y = parseLevel4();
                if(x instanceof Number && y instanceof Number){
                    return shiftRight((Number)x,(Number)y);
                }else{
                    throw new IllegalArgumentException("Cannot use '>>' on: " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }

    /**
     * Level 4 used for + / - operators
     * (addition / subtraction)
     *
     * @return
     */
    private Object parseLevel4() {
        Object x = parseLevel3();
        for (; ; ) {
            if (eat("+")) {
                Object y = parseLevel3();
                if (x instanceof String || y instanceof String) {
                    x = x.toString() + y.toString();
                } else if (x instanceof Number || y instanceof Number) {
                    x = add((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot use '+' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else if (eat("-")) {
                Object y = parseLevel3();
                if (x instanceof Number || y instanceof Number) {
                    x = subtract((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot use '-' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }

    /**
     * level 3 is responsible for multiplication division and mods
     *
     * @return
     */
    private Object parseLevel3() {
        Object x = parseLevel2();
        for (; ; ) {
            if (eat("*")) {
                Object y = parseLevel2();
                if (x instanceof Number || y instanceof Number) {
                    x = multiply((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot use '*' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else if (eat("/")) {
                Object y = parseLevel2();
                if (x instanceof Number || y instanceof Number) {
                    x = divide((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot use '/' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else if (eat("%")) {
                Object y = parseLevel2();
                if (x instanceof Number || y instanceof Number) {
                    x = mod((Number) x, (Number) y);
                } else {
                    throw new IllegalArgumentException("Cannot use '%' token between a " + x.getClass().getSimpleName() + " and " + y.getClass().getSimpleName());
                }
            } else {
                return x;
            }
        }
    }


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
    }
    /**
     * Level 2 used for + / - (right to left)
     *
     * @return
     */
    private Object parseLevel2() {
        for (; ; ) {
            if (eat("+")) {
                Object x = parseLevel1();
                if (x instanceof Number) {
                    return x;
                } else {
                    throw new IllegalArgumentException("cannot use '+' modifier on: " + x.getClass().getSimpleName());
                }
            } else if (eat("-")) {
                {
                    Object x = parseLevel1();
                    if (x instanceof Number) {
                        return subtract(0, (Number) x);
                    } else {
                        throw new IllegalArgumentException("Cannot use '-' modifier on: " + x.getClass().getSimpleName());
                    }
                }
            } else if (eat("~")) {
                Object x = parseLevel1();
                if (x instanceof Number) {
                    return invert((Number) x);
                } else {
                    throw new IllegalArgumentException("Cannot use '~' modifier on: " + x.getClass().getSimpleName());
                }
            } else if (eat("!")) {
                Object x = parseLevel1();
                if (x instanceof Boolean) {
                    return !(Boolean) x;
                } else {
                    throw new IllegalArgumentException("Cannot use '!' modifier on: " + x.getClass().getSimpleName());
                }
            } else {

                Matcher m = castPattern.matcher(str.substring(pos));
                if(m.find()){
                    String cast = m.group(1);
                    String cName = m.group(2);
                    eat(cast);
                    Object x = parseLevel1();

                    if(primitiveCastMap.containsKey(cName)){
                        Class c = primitiveCastMap.get(cName);
                        if(c.getSuperclass() == Number.class){
                            if(x instanceof Number) {
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
                                }
                            }else{
                                throw new IllegalArgumentException(x.getClass().getSimpleName() + " cannot be cast to a: " + cName);
                            }
                        }
                    }else if(castMap.containsKey(cName)){
                        x = castMap.get(cName).cast(x);
                    }else{
                        throw new IllegalArgumentException("Cannot find class: " + cName);
                    }
                    return x;
                }

                return parseLevel1();
            }
        }
    }


    /**
     * level 1 is responsible for variable, constant, and function access
     *
     * @return
     */
    private Object parseLevel1() {

        Object x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
            x = parseLevel15();
            eat(')');
        } else if ((ch >= '0' && ch <= '9')) { // numbers

            while ((ch >= '0' && ch <= '9') || ch == '.' || (ch > 'a' && ch < 'z')|| (ch > 'A' && ch < 'Z')) nextChar();
            if (str.substring(startPos, this.pos).contains(".")) {
                x = Double.parseDouble(str.substring(startPos, this.pos));
            } else {
                x = parseInt(str.substring(startPos, this.pos));
            }
        } else if (Character.isAlphabetic(ch) || ch == '_' || ch == '$') { // functions
            while (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_'|| ch == '$') nextChar();
            String token = str.substring(startPos, this.pos);

            switch (token) {
                case "true":
                case "TRUE":
                    return true;
                case "false":
                case "FALSE":
                    return false;
                case "pi":
                case "PI":
                    return Math.PI;
            }

            if (next("(")) {
                x = parseLevel15();
                x = parseFunction(token, x);
            } else {
                x = parseVariable(token);
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
            x = str.substring(startPos + 1, this.pos - 1);
        } else {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }

        return x;
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

    private static final HashMap<String, FunctionParser> FUNCTION_MAP = new HashMap<>();

    private static abstract class FunctionParser {
        abstract public Object parse(Object parms);
    }

    static {
        FUNCTION_MAP.put("sqrt", new FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.sqrt(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("Sqrt function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("sin", new FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.sin(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("sin function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("cos", new FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.cos(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("cos function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("tan", new FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.tan(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("tan function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("toRadians", new FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.toRadians(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("toRadians function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("toDegrees", new FunctionParser() {
            @Override
            public Object parse(Object parms) {
                if (parms instanceof Number) {
                    return Math.toDegrees(((Number) parms).doubleValue());
                } else {
                    throw new IllegalArgumentException("toDegrees function cannot take: " + parms.getClass().getSimpleName());
                }
            }
        });

        FUNCTION_MAP.put("pow", new FunctionParser() {
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

    private static final NumberComparator NUMBER_COMPARATOR = new NumberComparator();

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
