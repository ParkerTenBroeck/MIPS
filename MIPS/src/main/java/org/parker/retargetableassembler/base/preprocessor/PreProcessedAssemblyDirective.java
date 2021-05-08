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
package org.parker.retargetableassembler.base.preprocessor;

import org.parker.retargetableassembler.util.CompiledExpression;
import org.parker.retargetableassembler.util.Line;

public class PreProcessedAssemblyDirective implements PreProcessedStatement{

    public final Line parentLine;
    public final String identifier;
    public final String expressionString;
    public final CompiledExpression[] args;


    public PreProcessedAssemblyDirective(Line parentLine, String identifier, String expressionString, CompiledExpression[] args){
        this.parentLine = parentLine;
        this.identifier = identifier;
        this.expressionString = expressionString;
        this.args = args;
    }

    @Override
    public String toString() {
        String temp = identifier + " ";
        for(int i = 0; i < args.length; i ++){
            temp += args[i].toString();
            if(i < args.length - 1){
                temp += ", ";
            }
        }
        return temp;
    }

    @Override
    public final Line getLine() {
        return parentLine;
    }
}
