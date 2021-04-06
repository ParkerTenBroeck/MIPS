package org.parker.mips.assembler2.directives;

import org.parker.mips.assembler2.base.Data;
import org.parker.mips.assembler2.util.ExpressionParser;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class Directives {


    private static Directive ASCIIZ = (operandExpression, ep, assembler) -> {

        String theString = operandExpression + (char)0;

        assembler.addDataToCurrent(new Data() {
            @Override
            public byte[] toBinary() {
                return theString.getBytes();
            }
        });
    };

    private static Directive ASCII = (operandExpression, ep, assembler) -> {

        assembler.addDataToCurrent(new Data() {
            @Override
            public byte[] toBinary() {
                return operandExpression.getBytes();
            }
        });
    };

    private static Directive WORD = ((operandExpression, ep, assembler) -> {

        ep = new ExpressionParser();

        byte[] data;
        Object x = ep.parse(operandExpression);
        if(x instanceof Object[]){
            Object[] y = (Object[])x;
            ByteBuffer bb = ByteBuffer.allocate(y.length * 4);

            for(int i = 0; i < y.length; i ++){
                bb.putInt(i*4, ((Number)x).intValue());
            }
            data = bb.array();
        }else{
            //data = new byte[1];
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(((Number)x).intValue());
            data = bb.array();
        }

        assembler.addDataToCurrent(new Data(){

            @Override
            public byte[] toBinary() {
                return data;
            }
        });
    });

    private static Directive HWORD = ((operandExpression, ep, assembler) -> {

        byte[] data;
        Object x = ep.parse(operandExpression);
        if(x instanceof Object[]){
            Object[] y = (Object[])x;
            ByteBuffer bb = ByteBuffer.allocate(y.length * 2);

            for(int i = 0; i < y.length; i ++){
                bb.putInt(i * 2, ((Number)x).shortValue());
            }
            data = bb.array();
        }else{
            //data = new byte[1];
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.putInt(((Number)x).shortValue());
            data = bb.array();
        }

        assembler.addDataToCurrent(new Data(){

            @Override
            public byte[] toBinary() {
                return data;
            }
        });
    });

    private static Directive BYTE = ((operandExpression, ep, assembler) -> {

        ep = new ExpressionParser();

        byte[] data;
        Object x = ep.parse(operandExpression);
        if(x instanceof Object[]){
            Object[] y = (Object[])x;
            ByteBuffer bb = ByteBuffer.allocate(y.length);

            for(int i = 0; i < y.length; i ++){
                bb.putInt(i, ((Number)x).byteValue());
            }
            data = bb.array();
        }else{
            data = new byte[1];
            data[0] = ((Number)x).byteValue();
            //ByteBuffer bb = ByteBuffer.allocate(1);
            //bb.putInt((Byte)((Number)x).byteValue());

        }

        assembler.addDataToCurrent(new Data(){

            @Override
            public byte[] toBinary() {
                return data;
            }
        });
    });

    private static Directive SPACE = ((operandExpression, ep, assembler) -> {

        ep = new ExpressionParser();

        Object x = ep.parse(operandExpression);
        final int size;
        if(x instanceof Integer || x instanceof Short || x instanceof Byte || x instanceof  Long){
            size = ((Number) x).intValue();
        }else{
            size = 0;
        }

        assembler.addDataToCurrent(new Data(){
            @Override
            public long getSize() {
                return size;
            }

            @Override
            public byte[] toBinary() {
                return new byte[0];
            }
        });
    });


    private static final HashMap<String, Directive> handlerMap = new HashMap<>();

    static{

        handlerMap.put("asciiz", ASCIIZ);
        handlerMap.put("ascii", ASCII);
        handlerMap.put("word", WORD);
        handlerMap.put("hword", HWORD);
        handlerMap.put("byte", BYTE);
        handlerMap.put("space", SPACE);

    }

    public static Directive getHandler(String directiveName){
        if(handlerMap.containsKey(directiveName)) {
            return handlerMap.get(directiveName);
        }else{
            throw new RuntimeException("Does not contain: " + directiveName);
        }
    }
}
