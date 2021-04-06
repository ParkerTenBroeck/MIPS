package org.parker.mips.assembler2;

import org.parker.mips.MIPS;
import org.parker.mips.assembler.InstructionToString;
import org.parker.mips.assembler2.base.*;
import org.parker.mips.assembler2.directives.Directives;
import org.parker.mips.assembler2.instruction.InstructionParser;
import org.parker.mips.assembler2.instruction.mips.parser.MipsInstructionParser;
import org.parker.mips.assembler2.util.*;
import org.parker.mips.emulator.Memory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembler {

    private static final Logger LOGGER = Logger.getLogger(Assembler.class.getName());

    protected ArrayList<AssemblyUnit> assemblyUnits = new ArrayList();
    protected HashMap<String, Label> globalLabelMap = new HashMap<String, Label>();

    private static final Pattern namePattern = Pattern.compile("[^a-zA-Z_$]([a-zA-Z_$][a-zA-Z_$0-9]*).*");
    private static final Pattern mnemonicPattern = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*)((\\s+.*)*)");
    private static final Pattern directivePattern = Pattern.compile("\\s*\\.([a-zA-Z_$][a-zA-Z_$0-9]*)\\s*(.*)");
    private static final Pattern labelPattern = Pattern.compile("\\s*([a-zA-Z_$][a-zA-Z_$0-9]*):\\s*(.*)");

    private static final InstructionParser instructionParser = new MipsInstructionParser();

    public static void main(String... args){
        MIPS.main(args);
        Assembler a = new Assembler();
        org.parker.mips.assembler.Assembler.assemble(new File("C:\\Users\\parke\\OneDrive\\Documents\\MIPS\\Examples\\Games\\snake2.asm"));
        InstructionToString.decompile();
        a.assemble(new File("C:\\Users\\parke\\OneDrive\\Documents\\MIPS\\Examples\\Games\\snake2.asm"));
        InstructionToString.decompile();
    }

    protected File currentFile;
    protected HashMap<String, Label> fileLabelMap = new HashMap<>();
    protected ArrayList<Data> fileDataList = new ArrayList<>();
    protected long startingAddress = 0;
    protected long currentAddress = 0;
    protected long size = 0;

    public void assemble(File input){

        currentFile = input;
        fileLabelMap = new HashMap<>();
        fileDataList = new ArrayList<>();

        ExpressionParser ep = new DataOperandExpressionParser(fileLabelMap);

        List<Line> lines = PreProcessor.PreProcessFile(input);

        startingAddress = 0;
        currentAddress = 0;
        size = 0;

        for(int i = 0; i < lines.size(); i ++){
            Line line = lines.get(i);
            Data data = null;

            if(mnemonicPattern.matcher(line.getLine()).matches()){
                Matcher m = mnemonicPattern.matcher(line.getLine());
                m.find();
                String mnemonic = m.group(1);
                String args = m.group(2);
                args = preProcessArguments(args);

                data = instructionParser.newInstance(mnemonic);
                ((Statement)data).setOperandExpression(args);

                //System.out.println("mnemonic: " + mnemonic + " args: " + args);

            }else if(directivePattern.matcher(line.getLine()).matches()){
                Matcher m = directivePattern.matcher(line.getLine());
                m.find();
                String directive = m.group(1);
                String args = m.group(2);
                //System.out.println("directive: " + directive + " args: " + args);
                Directives.getHandler(directive).parse(args, ep, this);

            }else if(labelPattern.matcher(line.getLine()).matches()){
                Matcher m = labelPattern.matcher(line.getLine());
                m.find();
                String label = m.group(1);
                //System.out.println("label: " + label);

                fileLabelMap.put(label, new Label(currentAddress, label, line));

            }else{
                System.err.println("error: " + line.getLine());
            }

            if(data != null){
                if(data instanceof OperandDependentData){
                    ((OperandIndependentData)data).evaluateOperands(ep);
                }
                currentAddress += data.getSize();
                size += data.getSize();

                fileDataList.add(data);

            }

        }

        AssemblyUnit au = new AssemblyUnit(startingAddress, size, fileDataList, fileLabelMap);

        assemblyUnits.add(au);
        globalLabelMap.putAll(fileLabelMap);

        linkGlobal();
    }

    public void addDataToCurrent(Data data){
        currentAddress += data.getSize();
        size += data.getSize();
        fileDataList.add(data);
    }

    protected String preProcessArguments(String arguments){
        Matcher m = namePattern.matcher(arguments);


        int start = 0;
        int end = 0;
        while(m.find(end)){
                String name = m.group(1);
                start = m.start(1);
                end = m.end(1);
                //System.out.println(name);
        }
        return arguments;
    }

    protected ArrayList<Line> tokenize(Line line){
        ArrayList<Line> tokens = new ArrayList<>();


        return tokens;
    }

public PagedMemory linkGlobal(){

        PagedMemory pMemory = new PagedMemory();

        assemblyUnits.sort((o1, o2) -> {
            if(o1.startingAddress < o2.startingAddress){
                return -1;
            }else if(o1.startingAddress > o2.startingAddress){
                return 1;
            }else{
                return 0;
            }
        });

        long address = 0;
        long size = 0;
        for(AssemblyUnit au: assemblyUnits){

            ExpressionParser ep = new DataOperandExpressionParser(au.asuLabelMap);

            if(au.startingAddress == -1){

                for(Data d:au.data){
                    if(d instanceof OperandIndependentData){
                        ((OperandIndependentData)d).evaluateOperands(ep);
                    }
                    if(d instanceof LinkableData){
                        ((LinkableData) d).link(globalLabelMap, address);
                    }
                    byte[] bytes = d.toBinary();
                    pMemory.add((int)address, bytes);
                    address += d.getSize();
                    size += d.getSize();
                }

            }else{
                if(au.startingAddress < address){
                    //error
                }
                address = au.startingAddress;

                for(Data d:au.data){
                    if(d instanceof OperandIndependentData){
                        ((OperandIndependentData)d).evaluateOperands(ep);
                    }
                    if(d instanceof LinkableData){
                        ((LinkableData) d).link(globalLabelMap, address);
                    }
                    byte[] bytes = d.toBinary();
                    pMemory.add((int)address, bytes);
                    address += d.getSize();
                    size += d.getSize();
                }
            }
            if(size != au.size){
                //throw new AssemblerError();
            }
        }
        Memory.setMemory(pMemory.getPage(0));
        return pMemory;
}

}
