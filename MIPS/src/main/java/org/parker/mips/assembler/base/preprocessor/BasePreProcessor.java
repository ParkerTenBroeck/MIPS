package org.parker.mips.assembler.base.preprocessor;

import org.parker.mips.assembler.base.assembler.BaseAssembler;
import org.parker.mips.assembler.directives.preprocessor.PreProcessorDirectives;
import org.parker.mips.assembler.util.Line;
import org.parker.mips.assembler_old.AssemblerLevel;
import org.parker.mips.util.ResourceHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public abstract class BasePreProcessor<A extends BaseAssembler> {

    private static final Logger LOGGER = Logger.getLogger(BasePreProcessor.class.getName());

    public static final Pattern namePattern = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*");
    public static final Pattern mnemonicPattern = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)((\\s+.*)?)");
    public static final Pattern directivePattern = Pattern.compile("\\s*\\.([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(.*)");
    public static final Pattern labelPattern = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*):\\s*");

    protected Map<String, String> definedValues = new HashMap<>();

    protected final Map<String, String> preDefinedValues = new HashMap<>();

    protected Number n;
    protected final A assembler;
    protected final BaseExpressionCompiler ec;

    public BasePreProcessor(A assembler){
        this.assembler = assembler;
        ec = getExpressionCompiler();
        ec.setAssembler(assembler);
        ec.setPreProcessor(this);

        //preprocess(new File(ResourceHandler.REG_DEF_HEADER_FILE));
        //preDefinedValues.putAll(definedValues);
        preprocess(new File(ResourceHandler.SYS_CALL_DEF_HEADER_FILE));
        preDefinedValues.putAll(definedValues);
    }

    public List<PreProcessedStatement> preprocess(File file){
        definedValues = new HashMap<>();
        definedValues.putAll(preDefinedValues);
        return preProcessStage3(preProcessToIntermediate(file));
    }

    public List<IntermediateStatement> preProcessToIntermediate(File file){
        List<Line> loadedLines = preProcessStage1(file);
        return preProcessStage2(loadedLines);
    }

    private List<Line> preProcessStage1(File file){

        BufferedReader br = null;

        List<Line> list = null;
        try {

            br = new BufferedReader(new FileReader(file));

            String line;

            list = new ArrayList<>();
            int index = 0;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                Line currentLine = new Line();
                currentLine.setParentFile(file);
                currentLine.setStartingLine(lineNumber);
                currentLine.setStartingIndex(index);
                index += line.length();

                if(line.contains(";")){
                    if(line.split(";").length < 2){
                        //continue;
                    }
                    line = line.split(";")[0];
                }

                currentLine.setLine(line);
                currentLine.setEndingIndex(index);
                list.add(currentLine);
                lineNumber++;
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, ": Failed to load file for PreProcessing", e);
            //e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            }catch(IOException ignored){

            }
        }

        list = removeFullLineComments(list);
        list = removeEmptySpace(list);

        return list;
    }

    private List<Line> removeFullLineComments(List<Line> list){
        for(int i = list.size() - 1; i >= 0; i -- ){
            Line line = list.get(i);
            line.trim();
            if(line.getLine().startsWith(";")){
                list.remove(i);
            }
        }

        return list;
    }

    private List<Line> removeEmptySpace(List<Line> list){
        for(int i = list.size() - 1; i >= 0; i -- ){
            list.get(i).trim();
            if(list.get(i).getLine().isEmpty()){
                list.remove(i);
            }
        }
        return list;
    }

    private List<IntermediateStatement> preProcessStage2(List<Line> lines){

        List<IntermediateStatement> generatedPreProcessedStatement = new ArrayList<>();

        for(int i = 0; i < lines.size(); i ++){

            Line line = lines.get(i);
            IntermediateStatement s = null;
            Matcher m;

            try {
                if ((labelPattern.matcher(line.getLine())).matches()) {

                    m = labelPattern.matcher(line.getLine());
                    m.find();
                    s = new IntermediateLabel(line, m, 1);

                } else if ((directivePattern.matcher(line.getLine())).matches()) {

                    m = directivePattern.matcher(line.getLine());
                    m.find();
                    s = new IntermediateDirective(line, m, 1, 2);

                } else if ((mnemonicPattern.matcher(line.getLine())).matches()) {

                    m = mnemonicPattern.matcher(line.getLine());
                    m.find();
                    s = new IntermediateAssemblyStatement(line, m, 1, 2);

                } else {
                    LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Illegal expression on line: " + line.getHumanLineNumber());
                }
            }catch (Exception e){
                LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Failed to PreProcess line: " + line.getHumanLineNumber(), e);
            }

            if(s != null) {
                generatedPreProcessedStatement.add(s);
            }
        }
        return generatedPreProcessedStatement;
    }


    private List<PreProcessedStatement> preProcessStage3(List<IntermediateStatement> statements){

        List<PreProcessedStatement> generatedPreProcessedStatement = new ArrayList<>();

        for(int i = 0; i < statements.size(); i ++){

            IntermediateStatement statement = statements.get(i);
            PreProcessedStatement s = null;

            try {

                if (statement instanceof IntermediateDirective) {

                    Line parentLine = ((IntermediateDirective) statement).parentLine;
                    String identifier = ((IntermediateDirective) statement).identifier;
                    String args = ((IntermediateDirective) statement).expressionString;
                    int expressionStartingIndex = ((IntermediateDirective) statement).expressionStartingIndex;

                    if (PreProcessorDirectives.hasDirective(identifier)) {
                        PreProcessorDirectives.getHandler(identifier).parse((IntermediateDirective) statement, statements, i, ec, this);
                        continue;
                    } else {
                        s = new PreProcessedAssemblyDirective(parentLine, identifier, args,
                                ec.compileExpressionsAsArray(args, parentLine, expressionStartingIndex));
                    }

                }else if (statement instanceof IntermediateLabel) {

                    s = new PreProcessedLabel(
                            ((IntermediateLabel) statement).parentLine,
                            ((IntermediateLabel) statement).label);

                } else if (statement instanceof IntermediateAssemblyStatement) {

                    Line line = ((IntermediateAssemblyStatement) statement).parentLine;
                    String mnemonic = ((IntermediateAssemblyStatement) statement).identifier;
                    String args = ((IntermediateAssemblyStatement) statement).expressionString;
                    int expressionStartingIndex = ((IntermediateAssemblyStatement) statement).expressionStartingIndex;

                    args = preProcessAssemblyArguments(mnemonic, args);

                    s = new PreProcessedAssemblyStatement(line, mnemonic, args,
                            ec.compileExpressionsAsArray(args, line, expressionStartingIndex));

                } else {
                    LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Illegal expression on line: " + statement.getLine().getHumanLineNumber());
                }
            }catch (Exception e){
                LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Failed to PreProcess line: " + statement.getLine().getHumanLineNumber(), e);
            }

            if(s != null) {
                generatedPreProcessedStatement.add(s);
            }
        }
        return generatedPreProcessedStatement;
    }

    public void addDirectMnemonicReplacement(String mnemonic, String replacement){
        if(namePattern.matcher(mnemonic).matches()){
            definedValues.put(mnemonic, replacement);
        }else{
            throw new IllegalArgumentException("mnemonic: " + mnemonic + " doesnt match naming pattern");
        }
    }

    public boolean isDefined(String mnemonic){
        if(definedValues.containsKey(mnemonic)){
            return true;
        }
        return false;
    }

    public String getDefinedValue(String mnemonic){
        if(definedValues.containsKey(mnemonic)){
            return definedValues.get(mnemonic);
        }else{
            throw new IllegalArgumentException();
        }
    }

    protected BaseExpressionCompiler<A, BasePreProcessor> getExpressionCompiler(){
        return new BaseExpressionCompiler();
    }

    protected abstract String preProcessAssemblyArguments(String instructionMnemonic, String arguments);

    public void define(String token){
        definedValues.put(token, null);
    }

    public void setValue(String token, String value){
        definedValues.put(token, value);
    }

    public boolean isDefinedValue(String token){
        if(isDefined(token)){
            return getDefinedValue(token) != null;
        }
        return false;
    }
}
