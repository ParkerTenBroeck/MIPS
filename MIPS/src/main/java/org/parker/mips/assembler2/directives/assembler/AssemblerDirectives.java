package org.parker.mips.assembler2.directives.assembler;

import org.parker.mips.assembler.AssemblerLevel;
import org.parker.mips.assembler2.base.assembler.BaseAssembler;
import org.parker.mips.assembler2.util.CompiledExpression;
import org.parker.mips.util.FileUtils;
import org.parker.mips.assembler2.base.Data;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.logging.Logger;

public class AssemblerDirectives {

    private static final Logger LOGGER = Logger.getLogger(AssemblerDirectives.class.getName());
    private static final Logger ASSEMBLER_LOGGER = Logger.getLogger(AssemblerDirectives.class.getName() + "\\.Assembler");

    private static final AssemblerDirective ASCIIZ = (args, assembler) -> {

        String theString = (String) args[0].evaluate() + (char)0;

        assembler.addDataToCurrent(() -> theString.getBytes());
    };

    private static final AssemblerDirective ASCII = (args, assembler) -> {

        String theString = (String) args[0].evaluate();

        assembler.addDataToCurrent(() -> theString.getBytes());
    };

    private static final AssemblerDirective WORD = ((args, assembler) -> {

        final ByteBuffer bb = ByteBuffer.allocate(args.length * 4);
        bb.order(ByteOrder.BIG_ENDIAN);

        for(int i = 0; i < args.length; i ++){
            bb.putInt(((Number) args[i].evaluate()).intValue());
        }
        byte[] data = bb.array();
        assembler.addDataToCurrent(() -> data);
    });

    private static final AssemblerDirective SHORT = ((args, assembler) -> {

        final ByteBuffer bb = ByteBuffer.allocate(args.length * 2);
        bb.order(ByteOrder.BIG_ENDIAN);

        for(int i = 0; i < args.length; i ++){
            bb.putShort(((Number) args[i].evaluate()).shortValue());
        }
        byte[] data = bb.array();
        assembler.addDataToCurrent(() -> data);
    });

    private static final AssemblerDirective BYTE = ((args, assembler) -> {

        final ByteBuffer bb = ByteBuffer.allocate(args.length);
        bb.order(ByteOrder.BIG_ENDIAN);

        for(int i = 0; i < args.length; i ++){
            bb.put(((Number) args[i].evaluate()).byteValue());
        }
        byte[] data = bb.array();
        assembler.addDataToCurrent(() -> data);
    });

    private static final AssemblerDirective DB = (args, assembler) -> {

        String s = (String) args[0].evaluate();
        byte[] data = FileUtils.loadFileAsByteArraySafe(new File(s));
        assembler.addDataToCurrent(() -> data);

    };

    private static final AssemblerDirective SPACE = ((args, assembler) -> {

        if(args.length != 1 || args.length != 2){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }

        Object arg1;
        Object arg2;
        try {
            arg1 = args[0].evaluate();
            if(args.length == 2){
                arg2 = args[1].evaluate();
            }else{
                arg2 = null;
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate space, expected arguments are (size: Long | Integer | Short | Byte) or (size: Long | Integer | Short | Byte, fill: Long | Integer | Short | Byte)", e);
        }

        if(!(arg1 instanceof Integer || arg1 instanceof Long || arg1 instanceof Short || arg1 instanceof Byte)){
            throw new IllegalArgumentException("argument 1 found: " + arg1.getClass().getSimpleName() + " needed: Integer");
        }

        if(arg2 != null) {
            if (!(arg2 instanceof Integer || arg2 instanceof Long || arg2 instanceof Short || arg2 instanceof Byte)) {
                throw new IllegalArgumentException("argument 2 found: " + arg2.getClass().getSimpleName() + " needed: Integer");
            }
        }

        final int size = ((Number)arg1).intValue();


        assembler.addDataToCurrent(new Data(){
            @Override
            public long getSize() {
                return size;
            }

            @Override
            public byte[] toBinary() {
                if(arg2 == null) {
                    return new byte[0];
                }else{
                    byte[] tmp = new byte[size];
                    for(int i = 0; i < tmp.length; i ++){
                        tmp[i] = ((Number) arg2).byteValue();
                    }
                    return tmp;
                }
            }
        });
    });

    private static final AssemblerDirective ALIGN = (args, assembler) -> {

        if(args.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }

        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate align, expected arguments are (Long | Integer | Short | Byte)", e);
        }

        if(!(arg instanceof Integer || arg instanceof Long || arg instanceof Short || arg instanceof Byte)){
            throw new IllegalArgumentException("argument: " + arg.getClass().getSimpleName() + " found but needed: Integer");
        }
        int i = ((Number)arg).intValue();

        if(BaseAssembler.bitCount(i) != 1 || i < 1 || i > 32768){
            throw new IllegalArgumentException("Alignment must be a positive power of two from 1 to 32,768");
        }

        long offset = BaseAssembler.getAlignmentOffset(assembler.getCurrentAddress(), i);

        assembler.setCurrentAssemblyUnitAlignment(i);

        assembler.addDataToCurrent(new Data(){
            @Override
            public long getSize() {
                return offset;
            }

            @Override
            public byte[] toBinary() {
                return new byte[0];
            }
        });
    };

    private static final AssemblerDirective ASSEMBLE = (args, assembler) -> {
        String s = (String)args[0].evaluate();
    };

    private static final AssemblerDirective EMSG = (args, assembler) -> {
        if(args.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }

        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate emsg, expected arguments are String", e);
        }

        String msg = arg.toString();

        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, msg);
    };

    private static final AssemblerDirective WMSG = (args, assembler) -> {
        if(args.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }
        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate wmsg, expected arguments are String", e);
        }

        String msg = arg.toString();

        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_WARNING, msg);
    };

    private static final AssemblerDirective MMSG = (args, assembler) -> {
        if(args.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }
        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate mmsg, expected arguments are String", e);
        }

        String msg = arg.toString();

        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_MESSAGE, msg);
    };

    private static final HashMap<String, AssemblerDirective> handlerMap = new HashMap<>();

    static{
        //data
        handlerMap.put("asciiz", ASCIIZ);
        handlerMap.put("ascii", ASCII);
        handlerMap.put("word", WORD);
        handlerMap.put("short", SHORT);
        handlerMap.put("byte", BYTE);
        handlerMap.put("db", DB);
        handlerMap.put("bcd", null);

        //space
        handlerMap.put("space", SPACE);
        handlerMap.put("align", ALIGN);

        //symbols linkage and visibility
        handlerMap.put("global", null);
        handlerMap.put("def", null);
        handlerMap.put("ref", null);

        //control diagnostics
        handlerMap.put("emsg", EMSG);
        handlerMap.put("wmsg", WMSG);
        handlerMap.put("mmsg", MMSG);


    }

    public static AssemblerDirective getHandler(String directiveName){
        if(handlerMap.containsKey(directiveName)) {
            return handlerMap.get(directiveName);
        }else{
            throw new RuntimeException("Assembler directive: " + directiveName + " does not exist");
        }
    }
}
