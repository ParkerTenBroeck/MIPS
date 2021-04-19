package org.parker.mips.assembler.debugger;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.parker.mips.assembler.util.Line;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Debugger {
    static RangeMap<Long, Line> rangeToLine = TreeRangeMap.create();
    static Map<Line, Range<Long>> lineToRange = new HashMap<>();
    static Map<Long, FinalizedLabel> addressToLabel = new HashMap<>();
    static Map<FinalizedLabel, Long> labelToAddress = new HashMap<>();


    public static void clear(){
        rangeToLine.clear();
        lineToRange.clear();
        addressToLabel.clear();
        labelToAddress.clear();
    }

    public static void addDataRange(long beginning, long ending, Line line){
        rangeToLine.put(Range.closed(beginning, ending), line);
        lineToRange.put(line, Range.closed(beginning, ending));
    }

    public static void addLabel(FinalizedLabel label){
        addressToLabel.put(label.address, label);
        labelToAddress.put(label, label.address);
    }

    public static FinalizedLabel getLabelFromAddress(long address){
        return addressToLabel.get(address);
    }

    public static long getAddressFromLine(String label, File file){
        return labelToAddress.get(label + ": " + file.getAbsolutePath());
    }

    public static Line getDataLineFromAddress(long address) {
        return rangeToLine.get(address);
    }
}


