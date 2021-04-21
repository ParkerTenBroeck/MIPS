package org.parker.mips.assembler.directives.assembler;

import org.parker.mips.assembler.util.linking.GlobalLabel;
import org.parker.mips.assembler.util.AssemblerLevel;
import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.base.Data;
import org.parker.mips.assembler.exception.ParameterCountError;
import org.parker.mips.assembler.util.linking.ReferencedLabel;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Logger;

public class AssemblerDirectives {

    private static final Logger LOGGER = Logger.getLogger(AssemblerDirectives.class.getName());
    private static final Logger ASSEMBLER_LOGGER = Logger.getLogger(AssemblerDirectives.class.getName() + "\\.Assembler");

    private static final AssemblerDirective ASCIIZ = (line, args, assembler) -> {

        if(args.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }

        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate wmsg, expected arguments are String", e);
        }

        String msg = arg.toString() + (char)0;

        assembler.addDataToCurrent(() -> msg.getBytes());
    };

    private static final AssemblerDirective ASCII = (line, args, assembler) -> {

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

        assembler.addDataToCurrent(() -> msg.getBytes());
    };

    private static final AssemblerDirective LONG = ((line, args, assembler) -> {

        if(args.length <= 0){
            throw new IllegalArgumentException("Wrong number of arguments must have 1 or more");
        }

        final ByteBuffer bb = ByteBuffer.allocate(args.length * 8);
        bb.order(assembler.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);


        for(int i = 0; i < args.length; i ++){
            Object arg =  args[i].evaluate();

            if(!(arg instanceof Number)){
                throw new ParameterCountError("Wrong type of argument expected: Number found: " + arg.getClass().getSimpleName(), line, args[i].startingAddress, args[i].endingAddress);
            }

            if(((Number) arg).longValue() > Long.MAX_VALUE || ((Number) arg).longValue() < Long.MIN_VALUE){
                ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_WARNING, "On Line: " + line.getHumanLineNumber()
                        + " argument: " + i + " index: " + args[i].startingAddress + " to: " + args[i]
                        + " value overflows");
            }

            bb.putLong(((Number)arg).longValue());
        }

        byte[] data = bb.array();
        assembler.addDataToCurrent(() -> data);
    });

    private static final AssemblerDirective WORD = ((line, args, assembler) -> {

        if(args.length <= 0){
            throw new IllegalArgumentException("Wrong number of arguments must have 1 or more");
        }

        final ByteBuffer bb = ByteBuffer.allocate(args.length * 4);
        bb.order(assembler.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);


        for(int i = 0; i < args.length; i ++){
            Object arg =  args[i].evaluate();

            if(!(arg instanceof Number)){
                throw new ParameterCountError("Wrong type of argument expected: Number found: " + arg.getClass().getSimpleName(), line, args[i].startingAddress, args[i].endingAddress);
            }

            if(((Number) arg).longValue() > Integer.MAX_VALUE || ((Number) arg).longValue() < Integer.MIN_VALUE){
                ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_WARNING, "On Line: " + line.getHumanLineNumber()
                        + " argument: " + i + " index: " + args[i].startingAddress + " to: " + args[i]
                        + " value overflows");
            }

            bb.putInt(((Number)arg).intValue());
        }

        byte[] data = bb.array();
        assembler.addDataToCurrent(() -> data);
    });

    private static final AssemblerDirective SHORT = ((line, args, assembler) -> {

        if(args.length <= 0){
            throw new IllegalArgumentException("Wrong number of arguments must have 1 or more");
        }

        final ByteBuffer bb = ByteBuffer.allocate(args.length * 2);
        bb.order(assembler.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);


        for(int i = 0; i < args.length; i ++){
            Object arg =  args[i].evaluate();

            if(!(arg instanceof Number)){
                throw new ParameterCountError("Wrong type of argument expected: Number found: " + arg.getClass().getSimpleName(), line, args[i].startingAddress, args[i].endingAddress);
            }

            if(((Number) arg).longValue() > Short.MAX_VALUE || ((Number) arg).longValue() < Short.MIN_VALUE){
                ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_WARNING, "On Line: " + line.getHumanLineNumber()
                        + " argument: " + i + " index: " + args[i].startingAddress + " to: " + args[i]
                        + " value overflows");
            }

            bb.putShort(((Number)arg).shortValue());
        }

        byte[] data = bb.array();
        assembler.addDataToCurrent(() -> data);
    });

    private static final AssemblerDirective BYTE = ((line, args, assembler) -> {

        if(args.length <= 0){
            throw new IllegalArgumentException("Wrong number of arguments must have 1 or more");
        }

        final ByteBuffer bb = ByteBuffer.allocate(args.length);
        bb.order(assembler.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);


        for(int i = 0; i < args.length; i ++){
            Object arg =  args[i].evaluate();

            if(!(arg instanceof Number)){
                throw new ParameterCountError("Wrong type of argument expected: Number found: " + arg.getClass().getSimpleName(), line, args[i].startingAddress, args[i].endingAddress);
            }

            if(((Number) arg).longValue() > Byte.MAX_VALUE || ((Number) arg).longValue() < Byte.MIN_VALUE){
                ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_WARNING, "On Line: " + line.getHumanLineNumber()
                        + " argument: " + i + " index: " + args[i].startingAddress + " to: " + args[i]
                        + " value overflows");
            }

            bb.put(((Number)arg).byteValue());
        }

        byte[] data = bb.array();
        assembler.addDataToCurrent(() -> data);
    });

    private static final AssemblerDirective DB = (line, args, assembler) -> {

        if(args.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }

        Object arg1 = args[0].evaluate();
        if(!(arg1 instanceof  String)){
            throw new IllegalArgumentException("Wrong type of argument expected: String found: " + arg1.getClass().getSimpleName());
        }

        String s = (String) arg1;
        try {
            byte[] data = Files.readAllBytes(new File(s).toPath());
            assembler.addDataToCurrent(() -> data);

        }catch (Exception e) {
            throw new IllegalArgumentException("Failed to load file", e);
        }
    };

    private static final AssemblerDirective SPACE = ((line, args, assembler) -> {

        if(!((args.length == 1) || (args.length == 2))){
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

    private static final AssemblerDirective ALIGN = (line, args, assembler) -> {

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

        long offset = BaseAssembler.getAlignmentOffset(assembler.getCurrentAssemblyUnitAddress(), i);

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

    private static final AssemblerDirective ASSEMBLE = (line, args, assembler) -> {
        String s = (String)args[0].evaluate();
    };

    private static final AssemblerDirective EMSG = (line, args, assembler) -> {
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

    private static final AssemblerDirective WMSG = (line, args, assembler) -> {
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

    private static final AssemblerDirective MMSG = (line, args, assembler) -> {
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


    private static final AssemblerDirective ORG = (line, args, assembler) -> {
        if(args.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + args.length);
        }

        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate org, expected arguments are (Long | Integer | Short | Byte)", e);
        }

        if(!(arg instanceof Integer || arg instanceof Long || arg instanceof Short || arg instanceof Byte)){
            throw new IllegalArgumentException("argument: " + arg.getClass().getSimpleName() + " found but needed: Integer");
        }
        long i = ((Number)arg).longValue();

        if(i < 0){
            throw new IllegalArgumentException("value cannot be negative");
        }

        if(assembler.getCurrentAssemblyUnitAddress() == 0){
            assembler.getCurrentAssemblyUnit().setStartingAddress(i);
        }else{
            long off =  i - assembler.getCurrentAssemblyUnitAddress();
            if(off < 0){
                throw new IllegalArgumentException("Cannot move assembly unit memory pointer backwards current address: " + assembler.getCurrentAssemblyUnitAddress() + " wanted: " + i);
            }else if(off == 0){
                return;
            }else{
                assembler.addDataToCurrent(new Data() {
                    @Override
                    public byte[] toBinary() {
                        return new byte[0];
                    }

                    @Override
                    public long getSize() {
                        return off;
                    }
                });
            }
        }
    };

    public static final AssemblerDirective GLOBAL = (line, args, assembler) -> {
        if(args.length != 1){
            throw new IllegalArgumentException("Wrong number of arguments expected: 1 found: " + args.length);
        }

        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate global, expected arguments are String", e);
        }

        if(!(arg instanceof String)){
            throw new IllegalArgumentException("Failed to evaluate global, expected arguments are String");
        }
        String msg = (String)arg;

        assembler.addGlobalLabel(new GlobalLabel(assembler.getCurrentAssemblyUnit(), msg.trim(), line));
    };

    public static final AssemblerDirective REF = (line, args, assembler) -> {
        if(args.length != 1){
            throw new IllegalArgumentException("Wrong number of arguments expected: 1 found: " + args.length);
        }

        Object arg;
        try {
            arg = args[0].evaluate();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to evaluate ref, expected arguments are String", e);
        }

        if(!(arg instanceof String)){
            throw new IllegalArgumentException("Failed to evaluate ref, expected arguments are String");
        }
        String msg = (String)arg;

        assembler.getCurrentAssemblyUnit().addLabel(new ReferencedLabel(assembler, msg, line));
    };

    private static final HashMap<String, AssemblerDirective> handlerMap = new HashMap<>();

    static{

        //data
        handlerMap.put("asciiz", ASCIIZ);
        handlerMap.put("ascii", ASCII);
        handlerMap.put("long", LONG);
        handlerMap.put("word", WORD);
        handlerMap.put("short", SHORT);
        handlerMap.put("byte", BYTE);
        handlerMap.put("db", DB);
        handlerMap.put("bcd", null);

        //space
        handlerMap.put("space", SPACE);
        handlerMap.put("align", ALIGN);

        //sorta in between
        handlerMap.put("org", ORG);

        //symbols linkage and visibility
        handlerMap.put("global", GLOBAL);
        handlerMap.put("def", null);
        handlerMap.put("ref", REF);

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
