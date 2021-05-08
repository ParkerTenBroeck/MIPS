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

public class IntermediateAssemblyStatement implements IntermediateStatement{

    public final Line parentLine;
    public final String identifier;
    public final int identifierStartingIndex;
    public final int identifierEndingIndex;
    public final String expressionString;
    public final int expressionStartingIndex;
    public final int expressionEndingIndex;


    @Deprecated
    public IntermediateAssemblyStatement(Line parentLine, String identifier, String expressionString){
        this.parentLine = parentLine;
        this.identifier = identifier;
        this.expressionString = expressionString;

        identifierStartingIndex = -1;
        identifierEndingIndex = -1;
        expressionStartingIndex = -1;
        expressionEndingIndex = -1;
    }


    public IntermediateAssemblyStatement(Line parentLine, Matcher matcher, int identifierGroup, int expressionGroup){
        this.parentLine = parentLine;
        this.identifier = matcher.group(identifierGroup);
        this.expressionString = matcher.group(expressionGroup);

        identifierStartingIndex = matcher.start(identifierGroup);
        identifierEndingIndex = matcher.end(identifierGroup);
        expressionStartingIndex =  matcher.start(expressionGroup);
        expressionEndingIndex =  matcher.end(expressionGroup);
    }

    @Override
    public String toString() {
        return identifier + " " + expressionString;
    }

    @Override
    public final Line getLine() {
        return parentLine;
    }
}
