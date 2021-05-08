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
package org.parker.mips.assembler.base.preprocessor;

import org.parker.mips.assembler.util.Line;

import java.util.regex.Matcher;

public class IntermediateLabel implements IntermediateStatement{

    public final Line parentLine;
    public final String label;
    public final int labelStartingIndex;
    public final int labelEndingIndex;

    @Deprecated
    public IntermediateLabel(Line parentLine, String label){
        this.parentLine = parentLine;
        this.label = label;

        labelStartingIndex = -1;
        labelEndingIndex = -1;

    }

    public IntermediateLabel(Line parentLine, Matcher matcher, int identifierGroup){
        this.parentLine = parentLine;
        this.label = matcher.group(identifierGroup);

        labelStartingIndex = matcher.start(identifierGroup);
        labelEndingIndex = matcher.end(identifierGroup);
    }

    @Override
    public String toString() {
        return label + ":";
    }

    @Override
    public final Line getLine() {
        return parentLine;
    }
}
