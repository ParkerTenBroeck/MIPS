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
package org.parker.mips.assembler.debugger;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.parker.mips.assembler.util.Line;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Debugger implements Serializable {
    private final RangeMap<Long, Line> rangeToLine = TreeRangeMap.create();
    private final Map<Line, Range<Long>> lineToRange = new HashMap<>();
    private final Map<Long, FinalizedLabel> addressToLabel = new HashMap<>();
    private final Map<FinalizedLabel, Long> labelToAddress = new HashMap<>();


    public void clear(){
        rangeToLine.clear();
        lineToRange.clear();
        addressToLabel.clear();
        labelToAddress.clear();
    }

    public void addDataRange(long beginning, long ending, Line line){
        rangeToLine.put(Range.closed(beginning, ending), line);
        lineToRange.put(line, Range.closed(beginning, ending));
    }

    public void addLabel(FinalizedLabel label){
        addressToLabel.put(label.address, label);
        labelToAddress.put(label, label.address);
    }

    public FinalizedLabel getLabelFromAddress(long address){
        return addressToLabel.get(address);
    }

    public long getAddressFromLine(String label, File file){
        return labelToAddress.get(label + ": " + file.getAbsolutePath());
    }

    public Line getDataLineFromAddress(long address) {
        return rangeToLine.get(address);
    }
}


