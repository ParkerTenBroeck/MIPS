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
package org.parker.mips.assembler.exception;

import org.parker.mips.assembler.util.Line;

public class AssemblerError extends RuntimeException{

    public final Line line;
    public final int startingAddressOffset;
    public final int endingAddressOffset;

    public AssemblerError(String message, Line line, int s, int e, Exception ex){
        super(message, ex);
        this.line = line;
        this.startingAddressOffset = s;
        this.endingAddressOffset = e;
    }
    public AssemblerError(String message, Line line, int s, int e){
        super(message);
        this.line = line;
        this.startingAddressOffset = s;
        this.endingAddressOffset = e;
    }

    public AssemblerError(String message, Line line, int s){
        super(message);
        this.line = line;
        this.startingAddressOffset = s;
        this.endingAddressOffset = -1;
    }

    public AssemblerError(String message, Line line, int s, Exception ex){
        super(message, ex);
        this.line = line;
        this.startingAddressOffset = s;
        this.endingAddressOffset = -1;
    }

    public AssemblerError(String message, Line line, Exception e) {
        super(message, e);
        this.line = line;
        this.startingAddressOffset = -1;
        this.endingAddressOffset = -1;
    }

    public AssemblerError(String message, Line line) {
        super(message);
        this.line = line;
        this.startingAddressOffset = -1;
        this.endingAddressOffset = -1;
    }

    @Override
    public String getMessage() {
        String indexInfo = "";
        if(startingAddressOffset > 0 ) {
            if (startingAddressOffset < endingAddressOffset) {
                indexInfo = " at index: " + startingAddressOffset + " to: " + endingAddressOffset + " " + line.getLine().substring(startingAddressOffset, endingAddressOffset);
            } else {
                indexInfo = " at index: " + startingAddressOffset;
            }
        }
        if(line != null){
            return  "On Line: " + line.getHumanLineNumber() + " " + super.getMessage() + indexInfo;
        }else{
            return super.getMessage();
        }
    }
}
