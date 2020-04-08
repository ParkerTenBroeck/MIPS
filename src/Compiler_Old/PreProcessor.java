/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler_Old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author parke
 */
public class PreProcessor {

    private String[] args;
    private String name;
    private int realLine;

    private String value;
    private ArrayList<String> lines = new ArrayList();

    public enum Type {
        CODE, VALUE
    };

    private final Type type;

    public Type getType() {
        return type;
    }

    public int getRealLine() {
        return realLine;
    }

    public PreProcessor(Object[] line) {

        String constructorString = (String) line[0];
        realLine = (int) line[1];

        if (constructorString.contains("(")) {
            type = Type.CODE;
            initCodeType(constructorString);
        } else {
            type = Type.VALUE;
            initValueType(constructorString);
        }

    }

    private void initCodeType(String constructorString) {
        try {
            name = constructorString.split(" ")[1].split("\\(")[0];
            if (!constructorString.split(" ")[1].contains("(")) {
                ASMCompiler.error("Invalid name for define");
            }
        } catch (Exception e) {
            name = "";
            ASMCompiler.error("Invalid name for define");
        }
        args = getArgs(constructorString);

    }

    private void initValueType(String constructorString) {
        try {
            name = constructorString.split(" ")[1];
            value = constructorString.split(" ")[2];//carrot haha funny
        } catch (Exception e) {
            name = "";
            ASMCompiler.error("Invalid name for define");
        }
    }

    public String[] getArgs(String line) {
        String[] args = new String[0];

        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
        try {
            while (m.find()) {
                args = m.group(1).split(",");
            }

        } catch (Exception e) {
            ASMCompiler.error("Invalid arguments");
        }
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }
        } else {
            ASMCompiler.error("Invalid arguments");
        }
        return args;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Object[]> getInstructionsWithArgs(Object[] line) {

        String instructionLine = (String) line[0];
        int lineNumber = (int) line[1];

        ArrayList<Object[]> temp = new ArrayList();

        String[] instructionArgs = getArgs(instructionLine);

        if (this.args.length != instructionArgs.length) {
            ASMCompiler.error("Wrong number of arguments");
            return null;
        }

        for (int l = 0; l < lines.size(); l++) {

            String currentLine = lines.get(l);

            for (int a = 0; a < args.length; a++) {

                currentLine = currentLine.replace(args[a], instructionArgs[a]);

            }
            temp.add(new Object[]{currentLine, (int) lineNumber});

        }

        return temp;
    }

    public static PreProcessor nextPreProcessor(ArrayList<Object[]> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            String line = (String) instructions.get(i)[0];
            if (line.contains("#define")) {
                return createPreProcessorAtIndex(i, instructions);
            }
        }
        return null;
    }

    public static PreProcessor createPreProcessorAtIndex(int index, ArrayList<Object[]> instructions) {

        Object[] line = instructions.get(index);
        instructions.remove(index);

        if (!((String) line[0]).contains("(")) {

            PreProcessor temp = new PreProcessor(line);
            findAndReplacePreProcessors(instructions, temp);
            return temp;

        } else {
            PreProcessor temp = new PreProcessor(line);
            line = instructions.get(index);

            try {
                while (!((String) line[0]).startsWith("]")) {
                    temp.addLine((String) line[0]);
                    instructions.remove(index);
                    line = instructions.get(index);
                }
                instructions.remove(index);
                findAndReplacePreProcessors(instructions, temp);
                return temp;
            } catch (UnsupportedOperationException e) {
                ASMCompiler.error("Define never closed");
            } catch (Exception e) {
                ASMCompiler.error("Error parsing string");
            }
        }
        return null;
    }

    public static void findAndReplacePreProcessors(ArrayList<Object[]> instructions, PreProcessor preProcessor) {

        Object[] o;
        String line;
        int lineNumber;

        for (int i = instructions.size() - 1; i >= 0; i--) {
            o = instructions.get(i);
            line = (String) o[0];
            lineNumber = (int) o[1];
            
            try{
                
            }catch(Exception e){
                
            }
            //lineNumber = 0;

            if (preProcessor.getType() == Type.CODE) {

                if (line.split("\\(")[0].trim().equals(preProcessor.getName())) {
                    instructions.remove(i);
                    instructions.addAll(i, preProcessor.getInstructionsWithArgs(o));
                    for (Object[] temp : instructions) {
                    }
                }

            } else if (preProcessor.getType() == Type.VALUE) { //string parcer decodes this 
                
                

//                if (line.contains(preProcessor.getName())) {
//                    
//                    int temp = line.indexOf(preProcessor.getName());
//                    
//                    Object[] tempO = new Object[]{line.replace(preProcessor.getName(), preProcessor.getValue()), (int) lineNumber};
//                    instructions.set(i, tempO);
//
//                }

            } else {
                ASMCompiler.error("PreProcessor " + lineNumber + " invalid type");
            }
        }
    }

    public String getValue() {
        return value;
    }
}
