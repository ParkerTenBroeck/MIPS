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
package org.parker.retargetableassembler.directives.preprocessor;

import org.parker.retargetableassembler.base.preprocessor.BasePreProcessor;
import org.parker.retargetableassembler.base.preprocessor.IntermediateDirective;
import org.parker.retargetableassembler.base.preprocessor.IntermediateStatement;
import org.parker.retargetableassembler.exception.DirectivesError;
import org.parker.retargetableassembler.util.CompiledExpression;
import org.parker.retargetableassembler.util.ExpressionCompiler;
import org.parker.assembleride.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreProcessorDirectives {

    private static final HashMap<String, PreProcessorDirective> handlerMap = new HashMap<>();

    private static final PreProcessorDirective INCLUDE = (s, is, index, ec, preProcessor) -> {

        CompiledExpression[] ecs = ec.compileExpressionsAsArray(s.expressionString, s.parentLine, s.expressionStartingIndex);

        if(ecs.length != 1){
            throw new IllegalArgumentException("wrong number of arguments found expected: 1 found: " + ecs.length);
        }

        Object arg1 = ecs[0].evaluate();
        if(!(arg1 instanceof String)){
            throw new IllegalArgumentException("wrong type found expected: String found: " + arg1.getClass().getSimpleName());
        }

        String path = (String) arg1;

        File file = new File(path);
        if(!file.exists()){
            file = new File(s.parentLine.getFile().getParentFile().getAbsolutePath() + FileUtils.FILE_SEPARATOR + path);
            if(!file.exists()){
                throw new IllegalArgumentException("File not found");
            }
        }
        List<IntermediateStatement> include = preProcessor.preProcessToIntermediate(file);
        for(IntermediateStatement as: include){
            as.getLine().setParent(s.parentLine);
        }
        is.addAll(index + 1, include);

    };

    private static final PreProcessorDirective IF = (s, is, index, ec, preProcessor) -> {

        index ++;
        int ifLevel = 0;
        int stage = 0;
        boolean statementCondition = (Boolean)ec.compileExpression(s.expressionString, s.parentLine, s.expressionStartingIndex).evaluate();
        boolean completedStatement = false;

        while(true){
            if(is.size() <= index){
                throw new DirectivesError("reached end of file before .endif", s.parentLine);
            }

            IntermediateStatement statement = is.get(index);

            if(statement instanceof IntermediateDirective){
                String identifier = ((IntermediateDirective) statement).identifier;

                if(identifier.equals("if")){
                    ifLevel++;
                }else if(identifier.equals("endif")){
                    if(ifLevel == 0){
                        is.remove(index);
                        break;
                    }
                    ifLevel--;
                }

                if(ifLevel == 0) {
                    if (identifier.equals("elseif")) {

                        if (stage <= 1) {
                            if(statementCondition){
                                completedStatement = true;
                                //continue;
                            }else {
                                    statementCondition = (Boolean) ec.compileExpression(
                                            ((IntermediateDirective) statement).expressionString,
                                            ((IntermediateDirective) statement).parentLine,
                                            ((IntermediateDirective) statement).expressionStartingIndex).evaluate();
                            }
                        } else {
                            throw new DirectivesError("elseif directive cannot follow else directive", s.parentLine);
                        }

                        stage = 1;
                        is.remove(index);
                        continue;
                    } else if (identifier.equals("else")) {

                        if (stage < 2) {
                            if(statementCondition){
                                completedStatement = true;
                                //continue;
                            }else {
                                    statementCondition = true;
                            }
                        } else {
                            throw new DirectivesError("else directive cannot follow else directive", s.parentLine);
                        }

                        stage = 2;
                        is.remove(index);
                        continue;
                    }
                }
            }

            if((!statementCondition) || completedStatement) {
                is.remove(index);
            }else{
                index ++;
            }
        }
    };

    public static final Pattern defineArgPattern = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)((\\s+(\\S.*))){0,1}");

    private static final PreProcessorDirective DEFINE = (s, is, index, ec, preProcessor) -> {
        Matcher m = defineArgPattern.matcher(s.expressionString);
        if(m.find()){
            String defineName = m.group(1);
            String mnemonicReplacement = m.group(4);

            if(mnemonicReplacement == null) {
                preProcessor.define(defineName);
            }else{
                preProcessor.addDirectMnemonicReplacement(defineName, mnemonicReplacement);
            }
        }
    };

    private static final PreProcessorDirective MACRO = (s, is, index, ec, preProcessor) -> {
        index ++;
        List<IntermediateStatement> myStatements = new ArrayList<>();
        while(true){
            if(is.size() <= index){
                throw new DirectivesError("reached end of file before endm", s.parentLine);
            }

            IntermediateStatement statement = is.get(index);
            is.remove(index);

            if(statement instanceof IntermediateDirective){
                String identifier = ((IntermediateDirective) statement).identifier;

                if(identifier.equals("endm")){
                    break;
                }
            }else{
                statement.getLine().setParent(s.parentLine);
                myStatements.add(statement);
            }

            index ++;
        }
    };

    static{

        //define symbols
        handlerMap.put("define", DEFINE);
        handlerMap.put("asg", null);
        handlerMap.put("unasg", null);
        handlerMap.put("undefine", null);

        //macros
        handlerMap.put("macro", MACRO);
        handlerMap.put("endm", new ConservedDirectiveSpot("Found endm directive without endm"));

        //file reference
        handlerMap.put("include", INCLUDE);

        //conditional statements
        handlerMap.put("if", IF);
        handlerMap.put("else", new ConservedDirectiveSpot("Found else directive without if"));
        handlerMap.put("elseif", new ConservedDirectiveSpot("Found elseif directive without if"));
        handlerMap.put("endif", new ConservedDirectiveSpot("Found endif directive without if"));

        //loop
        handlerMap.put("loop", null);
        handlerMap.put("break", new ConservedDirectiveSpot("Found break directive without loop"));
        handlerMap.put("endloop", new ConservedDirectiveSpot("Found endloop directive without loop"));
    }

    public static PreProcessorDirective getHandler(String directiveName){
        if(handlerMap.containsKey(directiveName)) {

            return handlerMap.get(directiveName);
        }else{
            throw new RuntimeException("Does not contain: " + directiveName);
        }
    }

    public static boolean hasDirective(String directiveName){
        return handlerMap.containsKey(directiveName);
    }

    private static class ConservedDirectiveSpot implements PreProcessorDirective {

        private final String message;

        public ConservedDirectiveSpot(String message){
            this.message = message;
        }

        @Override
        public void parse(IntermediateDirective s, List<IntermediateStatement> is, int index, ExpressionCompiler ec, BasePreProcessor preProcessor) {
            throw new DirectivesError(message, s.parentLine);
        }
    }
}
