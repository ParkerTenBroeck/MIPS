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

public class DirectivesError extends AssemblerError {

    public DirectivesError(String message, Line line, int s, int e, Exception ex){
        super(message,line, s, e, ex);
    }
    public DirectivesError(String message, Line line, int s, int e){
        super(message,line, s, e);
    }

    public DirectivesError(String message, Line line, int s){
        super(message,line, s);
    }

    public DirectivesError(String message, Line line, int s, Exception ex){
        super(message,line, s, ex);
    }

    public DirectivesError(String message, Line line) {
        super(message, line);
    }
}
