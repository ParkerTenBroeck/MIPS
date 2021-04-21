package org.parker.mips.assembler.base.assembler;

import org.parker.mips.assembler.base.Data;
import org.parker.mips.assembler.base.DataStatement;
import org.parker.mips.assembler.base.LinkableData;
import org.parker.mips.assembler.base.preprocessor.*;
import org.parker.mips.assembler.debugger.Debugger;
import org.parker.mips.assembler.debugger.FinalizedLabel;
import org.parker.mips.assembler.directives.assembler.AssemblerDirectives;
import org.parker.mips.assembler.exception.AssemblerError;
import org.parker.mips.assembler.util.CompiledExpression;
import org.parker.mips.assembler.util.linking.AssemblyUnit;
import org.parker.mips.assembler.util.linking.Label;
import org.parker.mips.assembler.util.linking.LocalLabel;
import org.parker.mips.assembler.util.AssemblerLevel;
import org.parker.mips.architectures.mips.emulator.mips.Memory;
import org.parker.mips.util.PagedMemory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class BaseAssembler<P extends BasePreProcessor> implements Assembler{


    private static final Logger ASSEMBLER_LOGGER = Logger.getLogger(BaseAssembler.class.getName() + "\\.Assembler");
    private static final Logger LINKER_LOGGER = Logger.getLogger(BaseAssembler.class.getName() + "\\.Linker");

    //global values
    protected P preProcessor;
    protected ArrayList<AssemblyUnit> assemblyUnits = new ArrayList<>();
    protected HashMap<String, Label> globalLabelMap = new HashMap<>();

    //debugging
    protected HashMap<Data, PreProcessedStatement> dataToPreProcessedStatement;

    //values specific to current assemble(File)
    protected File currentFile;
    protected PreProcessedStatement currentStatement;
    protected HashMap<String, Label> fileLabelMap;
    protected ArrayList<Data> fileDataList;
    protected AssemblyUnit currentAssemblyUnit;
    protected long currentAssemblyUnitAddress = 0;
    protected long currentAssemblyUnitSize = 0;

    protected boolean isBigEndian;

    private void assemble(File input){

        if(input == null) {
            ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Provided file is null skipping");
            return;
        }
        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_MESSAGE, "Started assembly of: " + input.getAbsolutePath());

        currentFile = input;
        fileLabelMap = new HashMap<>();
        fileDataList = new ArrayList<>();
        currentAssemblyUnit = new AssemblyUnit(fileDataList, fileLabelMap);

        List<PreProcessedStatement> lines = preProcessor.preprocess(input);

        currentAssemblyUnitAddress = 0;
        currentAssemblyUnitSize = 0;

        for(int i = 0; i < lines.size(); i ++){

            currentStatement = lines.get(i);
            Data data = null;

                if (currentStatement instanceof PreProcessedAssemblyStatement) {

                    try {
                        String mnemonic = ((PreProcessedAssemblyStatement) currentStatement).identifier;
                        CompiledExpression[] args = ((PreProcessedAssemblyStatement) currentStatement).args;

                        DataStatement instruction = getInstruction(mnemonic);
                        instruction.setArgExpressions(args, ((PreProcessedAssemblyStatement) currentStatement).parentLine);
                        data = instruction;
                    }catch (Exception e){
                        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "", new AssemblerError("Failed to evaluate Assembly Statement", ((PreProcessedAssemblyStatement) currentStatement).parentLine, e));
                        continue;
                    }

                } else if (currentStatement instanceof PreProcessedAssemblyDirective) {

                    try {
                        String directive = ((PreProcessedAssemblyDirective) currentStatement).identifier;
                        CompiledExpression[] args = ((PreProcessedAssemblyDirective) currentStatement).args;

                        AssemblerDirectives.getHandler(directive).parse(
                                ((PreProcessedAssemblyDirective) currentStatement).parentLine, args, this);
                    }catch (Exception e){
                        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "", new AssemblerError("Failed to evaluate AssemblyDirective", ((PreProcessedAssemblyDirective) currentStatement).parentLine, -1, e));
                        continue;
                    }

                } else if (currentStatement instanceof PreProcessedLabel) {
                    try {

                        Label label = new LocalLabel(currentAssemblyUnitAddress, this.currentAssemblyUnit, ((PreProcessedLabel) currentStatement).label, ((PreProcessedLabel) currentStatement).parentLine);
                        currentAssemblyUnit.addLabel(label);

                    }catch (Exception e){
                        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "", new AssemblerError("Failed to evaluate Label", ((PreProcessedLabel) currentStatement).parentLine, -1, e));
                        continue;
                    }
                }

            if(data != null){
                this.addDataToCurrent(data);
            }

        }
        currentAssemblyUnit.setSize(currentAssemblyUnitSize);

        assemblyUnits.add(currentAssemblyUnit);

        currentFile = null;
        currentStatement = null;
        fileLabelMap = null;
        fileDataList = null;
        currentAssemblyUnit = null;
        currentAssemblyUnitAddress = 0;
        currentAssemblyUnitSize = 0;
    }

    public void addDataToCurrent(Data data){
        currentAssemblyUnitAddress += data.getSize();
        currentAssemblyUnitSize += data.getSize();
        fileDataList.add(data);

        //debug
        dataToPreProcessedStatement.put(data, currentStatement);
    }

    public PagedMemory linkGlobal(){

        LINKER_LOGGER.log(AssemblerLevel.ASSEMBLER_MESSAGE, "Started linking global");

        PagedMemory pMemory = new PagedMemory();

        assemblyUnits.sort((o1, o2) -> {
            if(o1.getStartingAddress() < o2.getStartingAddress()){
                return -1;
            }else if(o1.getStartingAddress() > o2.getStartingAddress()){
                return 1;
            }else{
                return 0;
            }
        });

        if(assemblyUnits.get(0).getStartingAddress() < 0){
            assemblyUnits.get(0).setStartingAddress(0);
        }
        for(int i = 1; i < assemblyUnits.size(); i ++){
            if(assemblyUnits.get(i).getStartingAddress() < 0){
                assemblyUnits.get(i).setStartingAddress(assemblyUnits.get(i - 1).getEndingAddress());
            }else{
                if(assemblyUnits.get(i - 1).getEndingAddress() > assemblyUnits.get(i).getStartingAddress()){
                    LINKER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "Size miss mach");
                }
            }
        }

        for(AssemblyUnit au: assemblyUnits){

            currentAssemblyUnit = au;

            long address = au.getStartingAddress();
            long size = 0;

                for(Data d:au.data){
                    if(d instanceof LinkableData){
                        try {
                            ((LinkableData) d).link(this, address);
                        }catch (Exception e){
                            LINKER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, ": Failed to link data", e);
                            try {
                                address += d.getSize();
                                size += d.getSize();
                            }catch (Exception ex){
                                LINKER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "", ex);
                            }
                            continue;
                        }
                    }
                    try {
                        byte[] bytes = d.toBinary();
                        pMemory.add((int) address, bytes);
                        address += d.getSize();
                        size += d.getSize();

                        Debugger.addDataRange(address - d.getSize(), address, dataToPreProcessedStatement.get(d).getLine());
                    }catch(Exception e){
                        try{
                            address += d.getSize();
                            size += d.getSize();
                        }catch (Exception ex){
                            ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "",  ex);
                        }
                        ASSEMBLER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, "",  e);
                    }
                }
            if(size != au.getSize()){
                LINKER_LOGGER.log(AssemblerLevel.ASSEMBLER_ERROR, ": Size missmatch size of Assembler Unit does not match the linked size? expected: " + au.getSize() + " got: " + size);
            }
        }

        for(AssemblyUnit au: assemblyUnits){
            for(Map.Entry<String, Label> s:au.asuLabelMap.entrySet()){
                try {
                    Debugger.addLabel(new FinalizedLabel(s.getValue().mnemonic, s.getValue().line, s.getValue().getAddress()));
                }catch (Exception ignored){

                }
            }
        }

        Memory.setMemory(pMemory.getPage(0));
        byte[] temp = new byte[pMemory.getPageCount() * 4096];
        for(int p = 0; p < pMemory.getPageCount(); p++){
            byte[] page = pMemory.getPage(p);
            for(int i = 0; i < 4096; i ++){
                temp[i + p * 4096] = page[i];
            }
        }
        Memory.setMemory(temp);

        return pMemory;
    }

    @Override
    public void assemble(File[] files) {

        isBigEndian = true;
        this.globalLabelMap = new HashMap<>();
        this.assemblyUnits = new ArrayList<>();
        this.preProcessor = createPreProcessor();

        Debugger.clear();
        this.dataToPreProcessedStatement = new HashMap<>();

        for(File f: files){
            assemble(f);
        }

        linkGlobal();

        this.globalLabelMap = null;
        this.assemblyUnits = null;
        this.preProcessor = null;

        this.dataToPreProcessedStatement = null;
    }

    public Label getLabel(String token) {
        if(currentAssemblyUnit.getAsuLabelMap().containsKey(token)) {
            return currentAssemblyUnit.getAsuLabelMap().get(token);
        }else{
            throw new IllegalArgumentException("token: " + token + " is not declared in the current scope");
        }
    }

    public void setCurrentAssemblyUnitAlignment(int i){
        currentAssemblyUnit.setAlignment(i);
    }

    public long getCurrentAssemblyUnitAddress(){
        return currentAssemblyUnitAddress;
    }

    public AssemblyUnit getCurrentAssemblyUnit() {
        return currentAssemblyUnit;
    }

    public Map<String, Label> getGlobalLabelMap() {
        return globalLabelMap;
    }

    public void addGlobalLabel(Label label) {
        this.globalLabelMap.put(label.mnemonic, label);
    }

    public boolean isBigEndian(){
        return isBigEndian;
    }

    protected abstract P createPreProcessor();
    protected abstract DataStatement getInstruction(String mnemonic);

    public static long align(long address, long alignment){
        return address + getAlignmentOffset(address, alignment);
    }

    public static long getAlignmentOffset(long address, long alignment){
        if(bitCount(alignment) != 1){

        }else{
            long mask = alignment - 1;
            long offset = ((~(mask & address)) + 1) & mask;
            return offset;
        }
        return 0;
    }

    public static int bitCount(long number){
        int count = 0;
        for(int i = 0; i < 64; i ++){
            count += ((number >> i) & 0b1) > 0?1:0;
        }
        return count;
    }
}
